package com.example.demodatabasepj.controller;


import com.example.demodatabasepj.service.TransferService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;


@AllArgsConstructor


@Controller
public class TransferController {

    private final TransferService transferService;


}
