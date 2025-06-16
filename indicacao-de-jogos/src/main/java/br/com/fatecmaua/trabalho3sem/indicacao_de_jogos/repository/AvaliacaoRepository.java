package br.com.fatecmaua.trabalho3sem.indicacao_de_jogos.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import br.com.fatecmaua.trabalho3sem.indicacao_de_jogos.model.Avaliacao;
import br.com.fatecmaua.trabalho3sem.indicacao_de_jogos.model.Jogo;
import br.com.fatecmaua.trabalho3sem.indicacao_de_jogos.model.Usuario;


public interface AvaliacaoRepository extends JpaRepository<Avaliacao, Long> {
    List<Avaliacao> findByJogo(Jogo jogo);
    Optional<Avaliacao> findByUsuarioAndJogo(Usuario usuario, Jogo jogo);
    List<Avaliacao> findByUsuario(Usuario usuario);
}