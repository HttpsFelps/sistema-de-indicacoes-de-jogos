package br.com.fatecmaua.trabalho3sem.indicacao_de_jogos.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import br.com.fatecmaua.trabalho3sem.indicacao_de_jogos.Projection.UsuarioSubstringProjection;
import br.com.fatecmaua.trabalho3sem.indicacao_de_jogos.model.Usuario;
import br.com.fatecmaua.trabalho3sem.indicacao_de_jogos.repository.UsuarioRepository;

@Service
public class UsuarioCachingService {
	@Autowired
	private UsuarioRepository repU;
	
	@Cacheable(value = "Todosusuarios")
	public List<Usuario> findAll(){
		return repU.findAll();
	}
	
    @Cacheable(value = "UsuarioPorSubstring", key = "#substring")
	public List<UsuarioSubstringProjection> buscaPorSubstring(String substring){
		return repU.buscaPorSubstring(substring);
	}

	@Cacheable("usuarioPorId") 
	public Optional<Usuario> findById(Long id) {
		return repU.findById(id); 
	}
	
	@CacheEvict(value = {"TodosUsuarios", "UsuarioPorSubstring", "usuarioPorId"}, allEntries = true)
	public void removerCache() {
		System.out.println("Removendo cache");
	}
	
}
