package br.com.fatecmaua.trabalho3sem.indicacao_de_jogos.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name="tb_empresasfavoritas")
public class EmpresasFavoritas {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	@ManyToOne
	@JoinColumn(name="usuario")
	private Usuario usuario;
	@ManyToOne
	@JoinColumn(name="empresa")
	private Empresa empresa;
	
	public EmpresasFavoritas() {}
	public EmpresasFavoritas(Long id, Usuario usuario, Empresa empresa) {
		super();
		this.id = id;
		this.usuario = usuario;
		this.empresa = empresa;
	}
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public Usuario getusuario() {
		return usuario;
	}
	public void setusuario(Usuario usuario) {
		this.usuario = usuario;
	}
	public Empresa getempresa() {
		return empresa;
	}
	public void setempresa(Empresa empresa) {
		this.empresa = empresa;
	}

}
