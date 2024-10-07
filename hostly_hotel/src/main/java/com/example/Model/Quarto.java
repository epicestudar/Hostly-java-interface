package com.example.model;

import java.util.ArrayList;
import java.util.List;

import com.example.test.StatusQuarto;
import com.example.test.TipoQuarto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Quarto {
    private String id; 
    private String codigoQuarto;
    private TipoQuarto tipoQuarto = TipoQuarto.STANDARD;
    private Integer capacidadeQuarto;
    private Double valorQuarto;
    private StatusQuarto status = StatusQuarto.DISPONIVEL;
    private List<Reserva> reservas = new ArrayList<>();
}
