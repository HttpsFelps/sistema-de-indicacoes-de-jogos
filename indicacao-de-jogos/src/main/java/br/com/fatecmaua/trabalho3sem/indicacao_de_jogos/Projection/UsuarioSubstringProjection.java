package br.com.fatecmaua.trabalho3sem.indicacao_de_jogos.Projection;

import java.util.Date;

public interface UsuarioSubstringProjection {
	String getNome_completo();
	String getNome_usuario();
	Date getData_nascimento();
	String getEmail_usuario();
	String getCargo();
}
