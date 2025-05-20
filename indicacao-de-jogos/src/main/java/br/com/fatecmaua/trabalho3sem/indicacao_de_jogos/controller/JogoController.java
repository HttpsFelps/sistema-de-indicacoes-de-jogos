package br.com.fatecmaua.trabalho3sem.indicacao_de_jogos.controller;



import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import br.com.fatecmaua.trabalho3sem.indicacao_de_jogos.model.Jogo;
import br.com.fatecmaua.trabalho3sem.indicacao_de_jogos.repository.JogoRepository;
import br.com.fatecmaua.trabalho3sem.indicacao_de_jogos.service.CachingService;

@RestController
@RequestMapping(value = "/jogos")
public class JogoController {
	@Autowired
	private JogoRepository repJ;
	@Autowired
	private CachingService cacheJ;
	
	@GetMapping(value= "/todos")
	public List<Jogo> retornaTodosJogos(){
		return cacheJ.findAll();
	}
	@PostMapping(value = "/novo")
	public ResponseEntity<Jogo> inserirJogo(@RequestBody Jogo jogo) {
	    Jogo jogoSalvo = repJ.save(jogo);
	    return ResponseEntity.status(HttpStatus.CREATED).body(jogoSalvo);
	}
	@DeleteMapping("/deletar/{id}")
	public Jogo removerJogo(@PathVariable Long id) {
		Optional<Jogo> op = repJ.findById(id);
		
		if(op.isPresent()) {
			Jogo jogo = op.get();
			repJ.deleteById(id);
			cacheJ.removerCache();
			return jogo;
		} else {
			throw new 
			ResponseStatusException
			(HttpStatus.NOT_FOUND);
		}
	}
}
