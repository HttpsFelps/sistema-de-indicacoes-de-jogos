package br.com.fatecmaua.trabalho3sem.indicacao_de_jogos.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import br.com.fatecmaua.trabalho3sem.indicacao_de_jogos.Projection.GeneroContagemProjection;
import br.com.fatecmaua.trabalho3sem.indicacao_de_jogos.model.Avaliacao;
import br.com.fatecmaua.trabalho3sem.indicacao_de_jogos.model.Jogo;
import br.com.fatecmaua.trabalho3sem.indicacao_de_jogos.model.Usuario;


public interface AvaliacaoRepository extends JpaRepository<Avaliacao, Long> {
    List<Avaliacao> findByJogo(Jogo jogo);
    Optional<Avaliacao> findByUsuarioAndJogo(Usuario usuario, Jogo jogo);
    List<Avaliacao> findByUsuario(Usuario usuario);
    
    @Query("""
    	    SELECT a.jogo.genero AS genero, COUNT(a) AS total
    	    FROM Avaliacao a
    	    WHERE a.usuario.id = :idUser AND a.recomenda = true
    	    GROUP BY a.jogo.genero
    	    ORDER BY total DESC
    	""")
    List<GeneroContagemProjection> findGenerosBemAvaliados(@Param("idUser") Long usuarioId);
    
    @Query("SELECT a.jogo.id FROM Avaliacao a WHERE a.usuario.id = :idUser")
    List<Long> findIdsJogosAvaliadosPorUsuario(@Param("idUser") Long idUser);

}