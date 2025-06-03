package br.com.fatecmaua.trabalho3sem.indicacao_de_jogos.service;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import br.com.fatecmaua.trabalho3sem.indicacao_de_jogos.Projection.JogoSubstringProjection;
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
	
	
	
    @Cacheable(value = "jogoPorId", key = "#id")
    public Optional<Jogo> findById(Long id) {
        return repJ.findById(id);
    }
    
    @Cacheable(value = "JogoPorSubstring", key = "#substring")
	public List<JogoSubstringProjection> buscaPorSubstring(String substring){
		return repJ.buscaPorSubstring(substring);
	}
	
	@CacheEvict(value = {"TodosJogosCacheable","JogoPorIdCacheable","JogoPorSubstring"}, allEntries = true)
	public void removerCache() {
		System.out.println("Removendo cache");
	}
}
	