package br.com.fatecmaua.trabalho3sem.indicacao_de_jogos.model;

import java.time.LocalDate;

import jakarta.persistence.Column;
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
	private String usuario;
	@Column(unique = true)
	private String email;
	private String senha;
	private LocalDate dataNasc;
	@Lob
	private byte[] imagemUsuario;
	
	public Usuario() {}
	
	
	
	public Usuario(Long id, String nome_Completo, String usuario, String email, String senha, LocalDate dataNasc,
			byte[] imagemUsuario) {
		super();
		this.id = id;
		this.nome_Completo = nome_Completo;
		this.usuario = usuario;
		this.email = email;
		this.senha = senha;
		this.dataNasc = dataNasc;
		this.imagemUsuario = imagemUsuario;
	}

	public Long getId() {
		return id;
	}

	public String getNome_Completo() {
		return nome_Completo;
	}

	public String getUsuario() {
		return usuario;
	}

	public String getEmail() {
		return email;
	}

	public String getSenha() {
		return senha;
	}

	public LocalDate getDataNasc() {
		return dataNasc;
	}

	public byte[] getImagemUsuario() {
		return imagemUsuario;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public void setNome_Completo(String nome_Completo) {
		this.nome_Completo = nome_Completo;
	}

	public void setUsuario(String usuario) {
		this.usuario = usuario;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public void setSenha(String senha) {
		this.senha = senha;
	}

	public void setDataNasc(LocalDate dataNasc) {
		this.dataNasc = dataNasc;
	}

	public void setImagemUsuario(byte[] imagemUsuario) {
		this.imagemUsuario = imagemUsuario;
	}
	
}
