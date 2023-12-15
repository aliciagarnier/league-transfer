package com.example.demodatabasepj.service;

import com.example.demodatabasepj.repository.MatchRepository;
import org.mockito.InjectMocks;
import org.mockito.Mock;

public class MatchService {


    @InjectMocks
    private MatchService service;

    @Mock
    private MatchRepository matchRepository;
}
