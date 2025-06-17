package br.com.fatecmaua.trabalho3sem.indicacao_de_jogos.model;

import java.time.LocalDate;

public record RegisterDTO(String nome_completo, String usuario, String email, String senha, LocalDate dataNasc, String nomeImagem, CargoUsuario cargo) {

}
