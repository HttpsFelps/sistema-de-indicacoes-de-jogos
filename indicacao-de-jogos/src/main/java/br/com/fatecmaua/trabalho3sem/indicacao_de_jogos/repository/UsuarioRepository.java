package br.com.fatecmaua.trabalho3sem.indicacao_de_jogos.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.security.core.userdetails.UserDetails;

import br.com.fatecmaua.trabalho3sem.indicacao_de_jogos.model.Usuario;

public interface UsuarioRepository extends JpaRepository<Usuario, Long>{
	UserDetails findByEmail(String email);
	
	
}
