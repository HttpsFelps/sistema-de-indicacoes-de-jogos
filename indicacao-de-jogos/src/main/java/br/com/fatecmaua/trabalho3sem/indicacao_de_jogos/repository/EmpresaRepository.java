package br.com.fatecmaua.trabalho3sem.indicacao_de_jogos.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import br.com.fatecmaua.trabalho3sem.indicacao_de_jogos.Projection.EmpresaSubstringProjection;
import br.com.fatecmaua.trabalho3sem.indicacao_de_jogos.model.Empresa;

public interface EmpresaRepository extends JpaRepository<Empresa, Long> {
	Optional<Empresa> findByNome(String nome);

	
	@Query(nativeQuery = true, value = "SELECT"
			+"    nome AS Nome_empresa,"
			+"    data_fundacao AS Data_fundacao, "
			+"    pais_origem AS Pais_origem, "
			+"FROM tb_empresa"
			+"WHERE LOWER(nome) LIKE LOWER(CONCAT('%', :sub , '%'));"
	)
	public List<EmpresaSubstringProjection> buscaPorSubstring(@Param("sub") String substring);
}
