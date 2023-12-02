package com.example.demodatabasepj.controller;


import com.example.demodatabasepj.dtos.ClubRecordDTO;
import com.example.demodatabasepj.dtos.TransferRecordDTO;
import com.example.demodatabasepj.models.Club;
import com.example.demodatabasepj.models.Player;
import com.example.demodatabasepj.models.Transfer;
import com.example.demodatabasepj.service.ClubService;
import com.example.demodatabasepj.service.PlayerService;
import com.example.demodatabasepj.service.TransferService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import java.util.Optional;
import java.util.UUID;


@Controller
public class TransferController {

    private final TransferService transferService;
    private final PlayerService playerService;
    private final ClubService clubService;

    @Autowired
    public TransferController(TransferService transferService, PlayerService playerService, ClubService clubService){
        this.transferService = transferService;
        this.playerService = playerService;
        this.clubService = clubService;
    }

    @RequestMapping("/transfer")
    public ModelAndView getAllTransfersWithFilter(@Param("keyword") String keyword){
        ModelAndView mv = new ModelAndView("transfer");
        Page<Transfer> page =  transferService.getAllTransfersWithFilter(keyword, 1,
                "date", "desc");

        mv.addObject("transfers", page.getContent());
        mv.addObject("transferCount", transferService.countAllTransfersByPlayerNameOrJoinNameOrLeftName(keyword));
        mv.addObject("currentPage", 1);
        mv.addObject("totalPages", page.getTotalPages());
        mv.addObject("sortField", "data");
        mv.addObject("sortDir", "desc");

        return mv;
    }

    //transfer helper > redirect to club
    @RequestMapping("/transfer/makeTransfer")
    public ModelAndView makeTransferHelper(){
        return new ModelAndView("makeTransferHelper");
    }


    @GetMapping("/player/transfer/{player_id}")
    public ModelAndView getTransfer(@PathVariable(value = "player_id") UUID id, @Param("keyword") String keyword)
    {

        ModelAndView mv = new ModelAndView("makeTransfer");
        Optional<Player> player = playerService.findById(id);
        if(player.isEmpty()){
            return new ModelAndView("redirect:/player");
        }

        Optional<Club> currentClubOptional = playerService.getCurrentClub(id);

        Page<Club> page = clubService.getAllClubsByName(keyword, 1,"marketValue", "desc");
        mv.addObject("clubs", page.getContent());

        currentClubOptional.ifPresent(club -> mv.addObject("currentClub", club));

        mv.addObject("player", player.get());

        return mv;

    }

    @PostMapping("/transfer")
    public ModelAndView createTransfer(@Valid TransferRecordDTO transferRecordDTO, BindingResult bindingResult){
        if(bindingResult.hasErrors()){
            System.out.println(transferRecordDTO.player_id());
            return new ModelAndView("redirect:/transfer/error");
        }
        System.out.println(transferRecordDTO.player_id());

        Transfer new_transfer = transferService.addTransfer(transferRecordDTO);
        return new ModelAndView("redirect:/transfer/" + new_transfer.getId());
    }



}
