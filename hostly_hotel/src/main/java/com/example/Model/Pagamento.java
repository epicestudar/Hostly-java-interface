package com.example.model;

import java.time.LocalDate;

import com.example.test.MetodoPagamento;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Pagamento {
    private String id;
    private Reserva idReserva;
    private Hospede cpfHospede;
    private LocalDate dataPagamento;
    private Double valorPago;

    private MetodoPagamento metodoPagamento = MetodoPagamento.CREDITO;

    // Método para calcular o valor total do pagamento
    public Double calcularValorPagamento() {
        // Obter o valor do quarto com base no tipo de quarto
        Double valorPorDiaria = idReserva.getCodigoQuarto().getTipoQuarto().getValorDiaria();
        Integer quantidadeDiarias = idReserva.getQuantidadeDiarias();

        // Calcular e retornar o valor total
        return valorPorDiaria * quantidadeDiarias;
    }
}
