package br.com.fatecmaua.trabalho3sem.indicacao_de_jogos.model;

import java.time.LocalDate;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Lob;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "tb_jogos")
public class Jogo {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
    @ManyToOne
    @JoinColumn(name = "id_empresa_dev")
    private Empresa desenvolvedora;
    @ManyToOne
    @JoinColumn(name = "id_empresa_pub")
    private Empresa publicadora;
	private String nome;
	@Enumerated(EnumType.STRING)
    private Genero genero;
	private String descricao;
	private LocalDate data_lancamento;
	@Lob
    private byte[] imagem;
	
	
	public Jogo() {}
	
	public Jogo(Long id, String nome, String descricao, LocalDate data_lancamento, byte[] imagem, Empresa desenvolvedora, Empresa publicadora, Genero genero) {
		super();
		this.id = id;
		this.desenvolvedora = desenvolvedora;
		this.publicadora = publicadora;
		this.nome = nome;
		this.genero = genero;
		this.descricao = descricao;
		this.data_lancamento = data_lancamento;
		this.imagem = imagem;
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
	public Genero getGenero() {
		return genero;
	}

	public void setGenero(Genero genero) {
		this.genero = genero;
	}
	public Empresa getDesenvolvedora() {
		return desenvolvedora;
	}

	public void setDesenvolvedora(Empresa desenvolvedora) {
		this.desenvolvedora = desenvolvedora;
	}

	public Empresa getPublicadora() {
		return publicadora;
	}

	public void setPublicadora(Empresa publicadora) {
		this.publicadora = publicadora;
	}
	public String getDescricao() {
		return descricao;
	}
	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}
	public LocalDate getData_lancamento() {
		return data_lancamento;
	}
	public void setData_lancamento(LocalDate data_lancamento) {
		this.data_lancamento = data_lancamento;
	}
	public byte[] getImagem() {
		return imagem;
	}
	public void setImagem(byte[] imagem) {
		this.imagem = imagem;
	}
}
