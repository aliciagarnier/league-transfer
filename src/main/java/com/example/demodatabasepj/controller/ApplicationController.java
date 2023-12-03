package com.example.demodatabasepj.controller;


import com.example.demodatabasepj.models.Player;
import com.example.demodatabasepj.models.Transfer;
import com.example.demodatabasepj.service.ClubService;
import com.example.demodatabasepj.service.PlayerService;
import com.example.demodatabasepj.service.TransferService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

import java.util.Optional;

@Controller
public class ApplicationController {

    private final TransferService transferService;
    private final PlayerService playerService;
    private final ClubService clubService;

    @Autowired
    public ApplicationController(TransferService transferService, PlayerService playerService, ClubService clubService){
        this.transferService = transferService;
        this.playerService = playerService;
        this.clubService = clubService;
    }
    @GetMapping("/")
    ModelAndView getHome(){
        ModelAndView mv = new ModelAndView("index");

        Page<Transfer> page = transferService.getAllTransfersWithFilter(null,
                1, "date", "desc", 10);

        Optional<Player> mvp = playerService.getMVP();
        mvp.ifPresent(player -> mv.addObject("mvp", player));


        mv.addObject("transfers", page.getContent());


        return mv;
    }




}
