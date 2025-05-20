package br.com.fatecmaua.trabalho3sem.indicacao_de_jogos.service;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import br.com.fatecmaua.trabalho3sem.indicacao_de_jogos.model.Jogo;
import br.com.fatecmaua.trabalho3sem.indicacao_de_jogos.repository.JogoRepository;

@Service
public class CachingService {
	@Autowired
	private JogoRepository repJ;
	
	@Cacheable(value = "TodosJogosCacheable")
	public List<Jogo> findAll(){
		return repJ.findAll();
	}
	@CacheEvict(value = {"TodasMusicasCacheable"}, allEntries = true)
	public void removerCache() {
		System.out.println("Removendo cache");
	}
}
	