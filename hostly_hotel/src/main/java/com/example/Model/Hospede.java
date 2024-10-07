package com.example.Model;

import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

// import java.sql.Date;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class Hospede {
    private String id;
    private String nome;
    // private Date data_nascimento;
    private String cpf;
    private String email;
    private String senha;
    private String telefone;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")  // Formato da data
    private LocalDate dataNascimento;  // Adicionar como LocalDate
}
