package com.example.demodatabasepj.models;


import jakarta.persistence.Entity;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.util.UUID;

@Entity
@Table(name = "match")
public class Match implements Serializable {



    @Id(strategy = GenerationType.AUTO)
    private UUID id;



}
