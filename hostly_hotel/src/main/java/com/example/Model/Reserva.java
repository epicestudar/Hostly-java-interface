package com.example.model;

import java.time.LocalDate;

import com.example.test.StatusReserva;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Reserva {
    private String id;
    private Quarto quarto;
    private Hospede hospede; 
    private Integer quantidadeDiarias; 
    private LocalDate dataCheckIn; 
    private LocalDate dataCheckOut; 
    private StatusReserva status = StatusReserva.CONFIRMADO;
    private LocalDate dataReserva; 

}
