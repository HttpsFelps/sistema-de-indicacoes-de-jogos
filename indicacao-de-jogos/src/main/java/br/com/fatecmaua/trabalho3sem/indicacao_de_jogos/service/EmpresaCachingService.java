package br.com.fatecmaua.trabalho3sem.indicacao_de_jogos.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import br.com.fatecmaua.trabalho3sem.indicacao_de_jogos.Projection.EmpresaSubstringProjection;
import br.com.fatecmaua.trabalho3sem.indicacao_de_jogos.model.Empresa;
import br.com.fatecmaua.trabalho3sem.indicacao_de_jogos.model.EmpresasFavoritas;
import br.com.fatecmaua.trabalho3sem.indicacao_de_jogos.repository.EmpresaRepository;
import br.com.fatecmaua.trabalho3sem.indicacao_de_jogos.repository.EmpresasFavoritasRepository;

@Service
public class EmpresaCachingService {

	@Autowired
	EmpresaRepository repE;
	
	@Autowired
	EmpresasFavoritasRepository repEF;
	
	@Cacheable(value = "TodasEmpresasCacheable")
	public List<Empresa> findAll(){
		return repE.findAll();
	}
	
	@Cacheable(value = "TodasEmpresasSeguidasCacheable")
	public List<EmpresasFavoritas> findAllFavoritas(){
		return repEF.findAll();
	}
	
	@Cacheable(value = "TodasEmpresasSeguidasPorIdCacheable")
	public List<EmpresasFavoritas> findAllFavoritasByUser(Long id){
		return repEF.findByUsuario_Id(id);
	}
	
    @Cacheable(value = "EmpresaPorId", key = "#id")
    public Optional<Empresa> findById(Long id) {
        return repE.findById(id);
    }
    
    @Cacheable(value = "EmpresaPorSubstring", key = "#substring")
	public List<EmpresaSubstringProjection> buscaPorSubstring(String substring){
		return repE.buscaPorSubstring(substring);
	}
    

	@CacheEvict(value = {"TodasEmpresasCacheable","EmpresaPorId","EmpresaPorSubstring","TodasEmpresasSeguidasCacheable","TodasEmpresasSeguidasPorUserCacheable"}, allEntries = true)
	public void removerCache() {
		System.out.println("Removendo cache");
	}
}
