package br.com.fatecmaua.trabalho3sem.indicacao_de_jogos.model;

import java.time.LocalDate;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;
import jakarta.persistence.Table;

@Entity
@Table(name="tb_usuarios")
public class Usuario {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	private String nome_Completo;
	private String Usuario;
	private LocalDate dataNasc;
	@Lob
	private byte[] imagemUsuario;
	
	public Usuario() {}
	
	public Usuario(Long id, String nome_Completo, String usuario, LocalDate dataNasc, byte[] imagemUsuario) {
		super();
		this.id = id;
		this.nome_Completo = nome_Completo;
		Usuario = usuario;
		this.dataNasc = dataNasc;
		this.imagemUsuario = imagemUsuario;
	}
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getNome_Completo() {
		return nome_Completo;
	}
	public void setNome_Completo(String nome_Completo) {
		this.nome_Completo = nome_Completo;
	}
	public String getUsuario() {
		return Usuario;
	}
	public void setUsuario(String usuario) {
		Usuario = usuario;
	}
	public LocalDate getDataNasc() {
		return dataNasc;
	}
	public void setDataNasc(LocalDate dataNasc) {
		this.dataNasc = dataNasc;
	}
	public byte[] getImagemUsuario() {
		return imagemUsuario;
	}
	public void setImagemUsuario(byte[] imagemUsuario) {
		this.imagemUsuario = imagemUsuario;
	}
}
