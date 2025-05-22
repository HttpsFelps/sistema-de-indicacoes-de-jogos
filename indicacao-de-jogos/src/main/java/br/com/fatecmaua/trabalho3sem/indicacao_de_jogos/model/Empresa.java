package br.com.fatecmaua.trabalho3sem.indicacao_de_jogos.model;

import java.time.LocalDate;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name="tb_empresa")
public class Empresa {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	private String nome;
	private String pais_origem;
	private LocalDate data_fundacao;
	
	public Empresa() {}
	
	public Empresa(Long id, String nome, String pais_origem, LocalDate data_fundacao) {
		super();
		this.id = id;
		this.nome = nome;
		this.pais_origem = pais_origem;
		this.data_fundacao = data_fundacao;
	}
	
	public String getPais_origem() {
		return pais_origem;
	}
	public void setPais_origem(String pais_origem) {
		this.pais_origem = pais_origem;
	}
	public LocalDate getData_fundacao() {
		return data_fundacao;
	}
	public void setData_fundacao(LocalDate data_fundacao) {
		this.data_fundacao = data_fundacao;
	}
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getNome() {
		return nome;
	}
	public void setNome(String nome) {
		this.nome = nome;
	}
}
