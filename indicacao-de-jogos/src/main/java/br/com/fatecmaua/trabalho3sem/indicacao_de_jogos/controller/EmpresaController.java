package br.com.fatecmaua.trabalho3sem.indicacao_de_jogos.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import br.com.fatecmaua.trabalho3sem.indicacao_de_jogos.Projection.EmpresaSubstringProjection;
import br.com.fatecmaua.trabalho3sem.indicacao_de_jogos.model.Empresa;
import br.com.fatecmaua.trabalho3sem.indicacao_de_jogos.model.EmpresasFavoritas;
import br.com.fatecmaua.trabalho3sem.indicacao_de_jogos.model.Usuario;
import br.com.fatecmaua.trabalho3sem.indicacao_de_jogos.repository.EmpresaRepository;
import br.com.fatecmaua.trabalho3sem.indicacao_de_jogos.repository.EmpresasFavoritasRepository;
import br.com.fatecmaua.trabalho3sem.indicacao_de_jogos.repository.UsuarioRepository;
import br.com.fatecmaua.trabalho3sem.indicacao_de_jogos.service.EmpresaCachingService;
import jakarta.transaction.Transactional;

@RestController
@RequestMapping("/empresa")
public class EmpresaController {
	
	@Autowired
	EmpresaCachingService cacheE;
	
	@Autowired
	EmpresaRepository repE;
	
	@Autowired
	UsuarioRepository repU;

	@Autowired
	EmpresasFavoritasRepository repEF;
	
	@GetMapping(value= "/todos")
	public List<Empresa> retornaTodasEmpresas(){
		return cacheE.findAll();
	}
	
	@GetMapping(value="/{id}")
	public Empresa retornaEmpresa(@PathVariable Long id) {
		Optional<Empresa> op = cacheE.findById(id);
		if(op.isPresent()) {
			return op.get();
		}else {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND);
		}
	}
	
	@PostMapping(value = "/novo")
	public ResponseEntity<Empresa> inserirJogo(@RequestBody Empresa empresa) {
	    Empresa jogoSalvo = repE.save(empresa);
	    cacheE.removerCache();
	    return ResponseEntity.status(HttpStatus.CREATED).body(jogoSalvo);
	}
	
	@DeleteMapping("/deletar/{id}")
	public Empresa removerJogo(@PathVariable Long id) {
		Optional<Empresa> op = repE.findById(id);
		
		if(op.isPresent()) {
			Empresa empresa = op.get();
			repE.deleteById(id);
			cacheE.removerCache();
			return empresa;
		} else {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND);
		}
	}
	
	@PutMapping(value = "/atualiza/{id}")
	public Empresa atualizaJogo(@PathVariable Long id, @RequestBody Empresa empresa_atualizada) {
		Optional<Empresa> op = repE.findById(id);
		
		if(op.isPresent()) {
			Empresa empresa_existente = op.get();
			empresa_existente.setNome(empresa_atualizada.getNome());
			empresa_existente.setPais_origem(empresa_existente.getPais_origem());
			empresa_existente.setData_fundacao(empresa_existente.getData_fundacao());
			
			
			repE.save(empresa_existente);
			cacheE.removerCache();
			return empresa_existente;
		}else {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND);
			}
	}
	
	@GetMapping("/substring")
	public List<EmpresaSubstringProjection> 
	buscaPorSubstring(@RequestParam(value = "substring")
	String substring){
		
		return cacheE.buscaPorSubstring(substring);
		
	}
	
	@Transactional
	@PostMapping("/seguir/{id}")
    public ResponseEntity<String> seguirEmpresa(@PathVariable Long id, 
                                @AuthenticationPrincipal UserDetails userDetails) {
		String email = userDetails.getUsername(); // Isso pega o email, que foi o que você colocou no token
        Usuario usuario = (Usuario) repU.findByEmail(email);
        Empresa empresa = repE.findById(id).get();
        boolean seguindo = repEF.existsByUsuarioAndEmpresa(usuario, empresa);
        if(seguindo) {
        	repEF.deleteByUsuarioAndEmpresa(usuario, empresa);
        	cacheE.removerCache();
        	return ResponseEntity.ok("Seguir");
        }else {
        	EmpresasFavoritas seguiu = new EmpresasFavoritas(null, usuario, empresa);
        	repEF.save(seguiu);
        	cacheE.removerCache();
        	return ResponseEntity.ok("Seguindo");
        }
    }
	
	@GetMapping("/todas/seguidas")
	public List<EmpresasFavoritas> todasSeguidas(){
		return cacheE.findAllFavoritas();
	}
	
	@GetMapping("/todas/seguidas/{id}")
	public List<EmpresasFavoritas> todasSeguidasPorUsuario(@PathVariable Long id){
		return cacheE.findAllFavoritasByUser(id);
	}
	
	@GetMapping("/todas/seguidas/pormim")
	public List<EmpresasFavoritas> todasSeguidasPorLogado(@AuthenticationPrincipal UserDetails userDetails){
        Usuario usuario = (Usuario) repU.findByEmail(userDetails.getUsername());
        Long id = usuario.getId();
		return cacheE.findAllFavoritasByUser(id);
	}
}
