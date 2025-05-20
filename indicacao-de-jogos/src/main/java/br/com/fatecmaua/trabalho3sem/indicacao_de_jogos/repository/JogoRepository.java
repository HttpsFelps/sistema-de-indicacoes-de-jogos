package br.com.fatecmaua.trabalho3sem.indicacao_de_jogos.repository;
import org.springframework.data.jpa.repository.JpaRepository;
import br.com.fatecmaua.trabalho3sem.indicacao_de_jogos.model.Jogo;

public interface JogoRepository extends JpaRepository<Jogo, Long> {

}
