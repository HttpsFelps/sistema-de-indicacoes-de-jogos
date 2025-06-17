package br.com.fatecmaua.trabalho3sem.indicacao_de_jogos.service;

import java.util.Comparator;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import br.com.fatecmaua.trabalho3sem.indicacao_de_jogos.Projection.GeneroContagemProjection;
import br.com.fatecmaua.trabalho3sem.indicacao_de_jogos.model.Jogo;
import br.com.fatecmaua.trabalho3sem.indicacao_de_jogos.repository.AvaliacaoRepository;
import br.com.fatecmaua.trabalho3sem.indicacao_de_jogos.repository.JogoRepository;

@Service
public class JogosRecomendadosService {
	@Autowired
	AvaliacaoRepository repA;
	@Autowired
	JogoRepository repJ;
	
	public List<Jogo> recomendarJogosBaseadoEmCurtidas(Long usuarioId) {
	    var generosCurtidos = repA.findGenerosBemAvaliados(usuarioId);
	    var generosOrdenados = generosCurtidos.stream()
	        .map(GeneroContagemProjection::getGenero)
	        .toList();

	    var idsJogosAvaliados = repA.findIdsJogosAvaliadosPorUsuario(usuarioId);

	    List<Jogo> jogos = repJ.findByGeneroInAndIdNotIn(generosOrdenados, idsJogosAvaliados);

	    jogos.sort(Comparator.comparingInt(j -> generosOrdenados.indexOf(j.getGenero())));
	    
	    return jogos;
	}


}
