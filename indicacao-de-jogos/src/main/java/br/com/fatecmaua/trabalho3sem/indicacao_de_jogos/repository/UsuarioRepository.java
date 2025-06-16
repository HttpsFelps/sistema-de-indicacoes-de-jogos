package br.com.fatecmaua.trabalho3sem.indicacao_de_jogos.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.security.core.userdetails.UserDetails;

import br.com.fatecmaua.trabalho3sem.indicacao_de_jogos.Projection.UsuarioSubstringProjection;
import br.com.fatecmaua.trabalho3sem.indicacao_de_jogos.model.Usuario;

public interface UsuarioRepository extends JpaRepository<Usuario, Long> {
	UserDetails findByEmail(String email);

	@Query(nativeQuery = true, value = "SELECT"
			+ "    nome_completo AS Nome_completo,"
			+ "    usuario AS Nome_usuario, "
			+ "    data_nasc AS Data_nascimento, "
			+ "    email AS Email_usuario "
			+ "    cargo AS Cargo "
			+ "FROM tb_usuarios usuarios "
			+ "WHERE LOWER(nome_completo) LIKE LOWER(CONCAT('%', :sub , '%'))"
			+ "   OR LOWER(usuario) LIKE LOWER(CONCAT('%', :sub , '%'))"
			+ "   OR LOWER(email) LIKE LOWER(CONCAT('%', :sub , '%'));"
	)
	public List<UsuarioSubstringProjection> buscaPorSubstring(@Param("sub") String substring);
}
