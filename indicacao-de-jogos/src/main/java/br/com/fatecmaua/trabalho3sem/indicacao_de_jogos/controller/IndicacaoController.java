package br.com.fatecmaua.trabalho3sem.indicacao_de_jogos.controller;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import br.com.fatecmaua.trabalho3sem.indicacao_de_jogos.model.Jogo;
import br.com.fatecmaua.trabalho3sem.indicacao_de_jogos.model.Usuario;
import br.com.fatecmaua.trabalho3sem.indicacao_de_jogos.repository.UsuarioRepository;
import br.com.fatecmaua.trabalho3sem.indicacao_de_jogos.service.JogosRecomendadosService;

@RestController
@RequestMapping("/indicacao")
public class IndicacaoController {
	@Autowired
	JogosRecomendadosService servJR;
	@Autowired
	UsuarioRepository repU;
	
	@GetMapping("/{id}")
	public ResponseEntity<List<Jogo>> gerarIndicacao(@PathVariable Long id) {
		List<Jogo> indicados =  servJR.recomendarJogosBaseadoEmCurtidas(id);
		return ResponseEntity.ok(indicados);
	}
	
	@GetMapping("/usuario")
	public ResponseEntity<List<Jogo>> tgerarIndicacaoPorLogado(@AuthenticationPrincipal UserDetails userDetails){
        Usuario usuario = (Usuario) repU.findByEmail(userDetails.getUsername());
        Long id = usuario.getId();
        List<Jogo> indicados =  servJR.recomendarJogosBaseadoEmCurtidas(id);
		return ResponseEntity.ok(indicados);
	}
}
