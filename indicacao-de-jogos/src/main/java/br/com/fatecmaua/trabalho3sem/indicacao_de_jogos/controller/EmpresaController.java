package br.com.fatecmaua.trabalho3sem.indicacao_de_jogos.controller;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;
import com.fasterxml.jackson.databind.ObjectMapper;
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
	
	@GetMapping(value= "/todas")
	public ResponseEntity<?> retornaTodasEmpresas(){
		List<Empresa> empresa = cacheE.findAll();

        if (empresa.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("Erro", "Nenhuma empresa encontrada"));
        }

        return ResponseEntity.ok(empresa);
    }
	
	
	@GetMapping(value="/{id}")
	public ResponseEntity<?> retornaEmpresa(@PathVariable Long id) {
		Optional<Empresa> op = cacheE.findById(id);
		if(op.isPresent()) {
			return ResponseEntity.ok(op.get());
		}else {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("Erro", "Nenhuma empresa encontrada"));
		}
	}
	
	@PostMapping(value = "/novo", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public ResponseEntity<?> inserirEmpresa(@RequestParam("empresa") String empresaJson, @RequestParam(value = "file", required = false) MultipartFile arquivo) {
		ObjectMapper mapper = new ObjectMapper();
	    mapper.registerModule(new com.fasterxml.jackson.datatype.jsr310.JavaTimeModule());

	    Empresa empresa = null;
	    
	    try {
	        empresa = mapper.readValue(empresaJson, Empresa.class);
	    } catch (IOException e) {
	        e.printStackTrace();
	        return ResponseEntity.badRequest().body("Erro ao desserializar JSON: " + e.getMessage());
	    }
	    if (repE.findByNome(empresa.getNome()).isPresent()) {
	        return ResponseEntity
	            .status(HttpStatus.CONFLICT)
	            .body("Já existe uma empresa com esse nome.");
	    }

	    Empresa empresaSalva = repE.save(empresa);
	    
	    try {
	        if (arquivo != null && !arquivo.isEmpty()) {
	            byte[] bytes = arquivo.getBytes();
	            String id = Long.toString(empresa.getId());
	            
	            Path pastaImagens = Paths.get("imagens").toAbsolutePath();
	            
	            
	            Path caminhoImagem = pastaImagens.resolve(empresa.getNome()+id+arquivo.getOriginalFilename());
	            Files.write(caminhoImagem, bytes);

	            empresaSalva.setNomeImagem(empresa.getNome()+id + arquivo.getOriginalFilename());
	            empresaSalva = repE.save(empresaSalva);
	        }
	    } catch (IOException e) {
	        e.printStackTrace();
	    }


	    cacheE.removerCache();
	    return ResponseEntity.status(HttpStatus.CREATED).body(empresaSalva);
	}

	
	@DeleteMapping("/deletar/{id}")
	public ResponseEntity<?> removerEmpresa(@PathVariable Long id) {
		Optional<Empresa> op = repE.findById(id);
		
		if(op.isPresent()) {
			Empresa empresa = op.get();
			repE.deleteById(id);
			repEF.deleteByEmpresa(empresa);
			cacheE.removerCache();
			return ResponseEntity.ok(empresa);
		} else {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("Erro", "Nenhuma empresa encontrada"));
		}
	}
	
	@PutMapping(value = "/atualizar/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public ResponseEntity<Empresa> atualizaEmpresa(@PathVariable Long id, @RequestParam("empresa") String empresaJson, @RequestParam(value = "file", required = false) MultipartFile arquivo) {
		Optional<Empresa> op = repE.findById(id);
		
	    if (op.isEmpty()) {
	        return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
	    }

	    ObjectMapper mapper = new ObjectMapper();
	    mapper.registerModule(new com.fasterxml.jackson.datatype.jsr310.JavaTimeModule());

	    Empresa empresaAtualizada;
	    try {
	    	empresaAtualizada = mapper.readValue(empresaJson, Empresa.class);
	    } catch (IOException e) {
	        e.printStackTrace();
	        return ResponseEntity.badRequest().build();
	    }
	    

	    Empresa empresaExistente = op.get();

	    // Atualiza campos
	    empresaExistente.setNome(empresaAtualizada.getNome());
	    empresaExistente.setPais_origem(empresaAtualizada.getPais_origem());
	    empresaExistente.setData_fundacao(empresaAtualizada.getData_fundacao());
		
	    Empresa empresaSalva = repE.save(empresaExistente);
	    
	 // Salva arquivo se existir
	    if (arquivo != null && !arquivo.isEmpty()) {
	        try {
	            byte[] bytes = arquivo.getBytes();
	            String idString = Long.toString(id);

	            Path pastaImagens = Paths.get("imagens").toAbsolutePath();

	            Path caminhoImagem = pastaImagens.resolve(empresaExistente.getNome()+id+arquivo.getOriginalFilename());
	            Files.write(caminhoImagem, bytes);
	            
	            empresaExistente.setNomeImagem(empresaExistente.getNome()+idString + arquivo.getOriginalFilename());
	            empresaSalva = repE.save(empresaExistente);
	        } catch (IOException e) {
	            e.printStackTrace();
	            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
	        }
	    }

	    

	    cacheE.removerCache();
	    return ResponseEntity.ok(empresaSalva);
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
		String email = userDetails.getUsername();
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
	
	@GetMapping("/todas/seguidas-por-mim")
	public List<EmpresasFavoritas> todasSeguidasPorLogado(@AuthenticationPrincipal UserDetails userDetails){
        Usuario usuario = (Usuario) repU.findByEmail(userDetails.getUsername());
        Long id = usuario.getId();
		return cacheE.findAllFavoritasByUser(id);
	}
}
