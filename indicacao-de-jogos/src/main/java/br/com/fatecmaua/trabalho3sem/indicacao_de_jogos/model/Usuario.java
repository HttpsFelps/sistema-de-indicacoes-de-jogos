package br.com.fatecmaua.trabalho3sem.indicacao_de_jogos.model;

import java.time.LocalDate;
import java.util.Collection;
import java.util.List;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;
import jakarta.persistence.Table;

@Entity
@Table(name="tb_usuarios")
public class Usuario implements UserDetails {
	
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	@Enumerated(EnumType.STRING)
	private CargoUsuario cargo;
	private String nomeCompleto;
	private String usuario;
	@Column(unique = true)
	private String email;
	private String senha;
	private LocalDate dataNasc;
    private String nomeImagem;
    @Column(nullable = true)
    private Boolean ativo = true;
	
	public Usuario() {}


	public Usuario(CargoUsuario cargo, String nomeCompleto, String usuario, String email, String senha,
			LocalDate dataNasc, String nomeImagem) {
		this.cargo = cargo;
		this.nomeCompleto = nomeCompleto;
		this.usuario = usuario;
		this.email = email;
		this.senha = senha;
		this.dataNasc = dataNasc;
		this.nomeImagem = nomeImagem;
	}



	public Usuario(Long id, String nomeCompleto, String usuario, String email, String senha, LocalDate dataNasc,
			String nomeImagem, CargoUsuario cargo) {
		super();
		this.id = id;
		this.cargo = cargo;
		this.nomeCompleto = nomeCompleto;
		this.usuario = usuario;
		this.email = email;
		this.senha = senha;
		this.dataNasc = dataNasc;
		this.nomeImagem = nomeImagem;
	}

	public Long getId() {
		return id;
	}
	
	public CargoUsuario getCargo() {
		return cargo;
	}
	
	public Boolean isAtivo() {
        return ativo;
    }

	public String getNomeCompleto() {
		return nomeCompleto;
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


	public void setId(Long id) {
		this.id = id;
	}
	
	public void setAtivo(Boolean ativo) {
        this.ativo = ativo;
    }
	
	public void setCargo(CargoUsuario cargo) {
		this.cargo = cargo;
	}

	public void setNomeCompleto(String nomeCompleto) {
		this.nomeCompleto = nomeCompleto;
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

	public String getNomeImagem() {
		return nomeImagem;
	}


	public void setNomeImagem(String nomeImagem) {
		this.nomeImagem = nomeImagem;
	}


	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		if(this.cargo == CargoUsuario.ADMIN) return List.of(new SimpleGrantedAuthority("ROLE_ADMIN"), new SimpleGrantedAuthority("ROLE_USER"));
		else return List.of(new SimpleGrantedAuthority("ROLE_USER"));
	}

	@Override
	public String getPassword() {
		return senha;
	}

	@Override
	public String getUsername() {
		return email;
	}
	
	@Override
	public boolean isAccountNonExpired() {
	    return true;
	}

	@Override
	public boolean isAccountNonLocked() {
	    return true;
	}

	@Override
	public boolean isCredentialsNonExpired() {
	    return true;
	}

	@Override
	public boolean isEnabled() {
	    return ativo;
	}

	
}
