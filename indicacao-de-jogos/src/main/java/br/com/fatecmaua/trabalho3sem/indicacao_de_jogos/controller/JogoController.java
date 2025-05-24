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

import br.com.fatecmaua.trabalho3sem.indicacao_de_jogos.Projection.JogoSubstringProjection;
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
	
	@GetMapping(value="/{id}")
	public Jogo retornaJogo(@PathVariable Long id) {
		Optional<Jogo> op = cacheJ.findById(id);
		if(op.isPresent()) {
			return op.get();
		}else {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND);
		}
	}
	
	@PostMapping(value = "/novo")
	public ResponseEntity<Jogo> inserirJogo(@RequestBody Jogo jogo) {
	    Jogo jogoSalvo = repJ.save(jogo);
	    cacheJ.removerCache();
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
			throw new ResponseStatusException(HttpStatus.NOT_FOUND);
		}
	}
	
	public Jogo atualizaJogo(@PathVariable Long id, @RequestBody Jogo jogo_atualizado) {
		Optional<Jogo> op = repJ.findById(id);
		
		if(op.isPresent()) {
			Jogo jogo_existente = op.get();
			jogo_existente.setNome(jogo_atualizado.getNome());
			jogo_existente.setDescricao(jogo_atualizado.getDescricao());
			jogo_existente.setData_lancamento(jogo_atualizado.getData_lancamento());
			jogo_existente.setGenero(jogo_atualizado.getGenero());
			jogo_existente.setImagem(jogo_atualizado.getImagem());
			jogo_existente.setDesenvolvedora(jogo_atualizado.getDesenvolvedora());
			jogo_existente.setPublicadora(jogo_atualizado.getPublicadora());
			
			repJ.save(jogo_existente);
			
			return jogo_existente;
		}else {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND);
			}
	}
	
	@GetMapping("/substring")
	public List<JogoSubstringProjection> 
	buscaPorSubstring(@RequestParam(value = "substring")
	String substring){
		
		return cacheJ.buscaPorSubstring(substring);
		
	}
}
