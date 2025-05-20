package br.com.fatecmaua.trabalho3sem.indicacao_de_jogos.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name="tb_favoritos")
public class Favoritos {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	@ManyToOne
	@JoinColumn(name="fk_usuario")
	private Usuario fk_usuario;
	@ManyToOne
	@JoinColumn(name="fk_jogo")
	private Jogo fk_jogo;
	
	public Favoritos() {}
	public Favoritos(Long id, Usuario fk_usuario, Jogo fk_jogo) {
		super();
		this.id = id;
		this.fk_usuario = fk_usuario;
		this.fk_jogo = fk_jogo;
	}
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public Usuario getFk_usuario() {
		return fk_usuario;
	}
	public void setFk_usuario(Usuario fk_usuario) {
		this.fk_usuario = fk_usuario;
	}
	public Jogo getFk_jogo() {
		return fk_jogo;
	}
	public void setFk_jogo(Jogo fk_jogo) {
		this.fk_jogo = fk_jogo;
	}
}
