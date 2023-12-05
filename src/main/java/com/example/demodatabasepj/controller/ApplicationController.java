package com.example.demodatabasepj.controller;


import com.example.demodatabasepj.models.Match;
import com.example.demodatabasepj.models.Player;
import com.example.demodatabasepj.models.Transfer;
import com.example.demodatabasepj.service.ClubService;
import com.example.demodatabasepj.service.MatchService;
import com.example.demodatabasepj.service.PlayerService;
import com.example.demodatabasepj.service.TransferService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;
import java.util.Optional;

@AllArgsConstructor
@Controller
public class ApplicationController {

    private final TransferService transferService;
    private final PlayerService playerService;
    private final ClubService clubService;
    private final MatchService matchService;


    @GetMapping("/")
    ModelAndView getHome(){
        ModelAndView mv = new ModelAndView("index");

        Page<Transfer> page = transferService.getAllTransfersWithFilter(null,
                1, "date", "desc", 10);

        Optional<Player> mvp = playerService.getMVP();

        List<Match> latestMatches = matchService.getAllLatestMatches();
        mvp.ifPresent(player -> mv.addObject("mvp", player));


        mv.addObject("transfers", page.getContent());
        mv.addObject("latestMatches", latestMatches);

        return mv;
    }




}
