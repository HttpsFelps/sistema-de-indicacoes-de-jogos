package br.com.fatecmaua.trabalho3sem.indicacao_de_jogos.repository;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import br.com.fatecmaua.trabalho3sem.indicacao_de_jogos.Projection.JogoSubstringProjection;
import br.com.fatecmaua.trabalho3sem.indicacao_de_jogos.model.Jogo;

public interface JogoRepository extends JpaRepository<Jogo, Long> {
	
	@Query(nativeQuery = true, value = "SELECT"
			+"    jogo.nome AS Nome_jogo,"
			+"    dev.nome AS Nome_desenvolvedora, "
			+"    dev.data_fundacao AS Data_fundacao_dev, "
			+"    dev.pais_origem AS Pais_origem_dev, "
			+"    pub.nome AS Nome_publicadora, "
			+"    pub.data_fundacao AS Data_fundacao_pub, "
			+"    pub.pais_origem AS Pais_origem_pub, "
			+"    jogo.genero AS Genero_jogo, "
			+"    jogo.descricao AS Descricao_jogo, "
			+"    jogo.data_lancamento AS Data_lancamento_jogo, "
			+"    jogo.imagem AS Img_jogo "
			+"FROM tb_jogos jogo "
			+"INNER JOIN tb_empresa dev ON jogo.id_empresa_dev = dev.id "
			+"INNER JOIN tb_empresa pub ON jogo.id_empresa_pub = pub.id "
			+"WHERE LOWER(jogo.nome) LIKE LOWER(CONCAT('%', :sub , '%'))"
			+"   OR LOWER(dev.nome) LIKE LOWER(CONCAT('%', :sub , '%'))"
			+"   OR LOWER(pub.nome) LIKE LOWER(CONCAT('%', :sub , '%'));"
	)
	public List<JogoSubstringProjection> buscaPorSubstring(@Param("sub") String substring);

	public Optional<Jogo> findByNome(String nome);

	public List<Jogo> findByGeneroIn(List<String> generos);
	
	List<Jogo> findByGeneroInAndIdNotIn(List<String> generos, List<Long> idsParaExcluir);

}
