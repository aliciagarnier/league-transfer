package com.example.demodatabasepj.service;

import com.example.demodatabasepj.models.Club;

import java.util.ArrayList;
import java.util.List;

public class ClubService {

    private List<Club> clubs;



    public ClubService(){
        clubs = new ArrayList<>();
    }

    public Club addClub(String name){

        Club new_club = new Club(name);

        return new_club;
    }
}
