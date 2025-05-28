package br.com.fatecmaua.trabalho3sem.indicacao_de_jogos.repository;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import br.com.fatecmaua.trabalho3sem.indicacao_de_jogos.model.Usuario;

public interface UsuarioRepository extends JpaRepository<Usuario, Long>{
	Optional<Usuario> findByEmail(String email);
}
