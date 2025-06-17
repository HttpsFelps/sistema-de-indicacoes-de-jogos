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
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.databind.ObjectMapper;

import br.com.fatecmaua.trabalho3sem.indicacao_de_jogos.Projection.UsuarioSubstringProjection;
import br.com.fatecmaua.trabalho3sem.indicacao_de_jogos.infra.security.TokenService;
import br.com.fatecmaua.trabalho3sem.indicacao_de_jogos.model.AuthenticationDTO;
import br.com.fatecmaua.trabalho3sem.indicacao_de_jogos.model.LoginResponseDTO;
import br.com.fatecmaua.trabalho3sem.indicacao_de_jogos.model.RegisterDTO;
import br.com.fatecmaua.trabalho3sem.indicacao_de_jogos.model.Usuario;
import br.com.fatecmaua.trabalho3sem.indicacao_de_jogos.repository.UsuarioRepository;
import br.com.fatecmaua.trabalho3sem.indicacao_de_jogos.service.UsuarioCachingService;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/usuario")
public class UsuarioController {

	@Autowired
	private AuthenticationManager authenticationManager;
	
	@Autowired
	private UsuarioRepository repU;
	
	@Autowired
	private TokenService tokenService;

	
	@Autowired
	private UsuarioCachingService cacheU;
	
	
	@PostMapping("/login")
	public ResponseEntity<?> login(@RequestBody @Valid AuthenticationDTO data) {
		if (repU.findByEmail(data.email()) != null) {
			try {
		        var usernamePassword = new UsernamePasswordAuthenticationToken(data.email(), data.senha());
		        var auth = this.authenticationManager.authenticate(usernamePassword);
	
		        var token = tokenService.gerarToken((Usuario) auth.getPrincipal());
	
		        return ResponseEntity.ok(new LoginResponseDTO(token));
		    } catch (BadCredentialsException e) {
		        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
		                .body(Map.of("Erro", "Email ou senha incorretos"));
		        }
			} else{
	        return ResponseEntity.status(HttpStatus.NOT_FOUND)
	                .body(Map.of("Erro", "Usuário não encontrado"));
	    }
	}

	
	@PostMapping("/registro")
	public ResponseEntity<?> registro(@RequestBody @Valid RegisterDTO data) {
		if(this.repU.findByEmail(data.email()) != null) return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("Erro", "Usuário já registrado"));
		
		String encryptPassword = new BCryptPasswordEncoder().encode(data.senha());
		Usuario newUsuario = new Usuario(data.cargo(),data.nome_completo(), data.usuario(),data.email(), encryptPassword, data.dataNasc(), data.nomeImagem());
		
		this.repU.save(newUsuario);
		
		return ResponseEntity.ok().build();
	}
	
	
	
	@GetMapping("/todos")
    public ResponseEntity<?> retornaTodosusuarios() {
        List<Usuario> usuarios = cacheU.findAll();

        if (usuarios.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("Erro", "Nenhum usuário encontrado"));
        }

        return ResponseEntity.ok(usuarios);
    }
	
	@GetMapping("/{id}")
	public ResponseEntity<Usuario> buscaUsuarioPorId(@PathVariable Long id) {
		Optional<Usuario> usuario = cacheU.findById(id);
		if (usuario.isPresent()) {
			return ResponseEntity.ok(usuario.get());
		} else {
			return ResponseEntity.notFound().build();
		}
	}
	
	@GetMapping("/substring")
	public ResponseEntity<?> 
	buscaPorSubstring(@RequestParam(value = "substring")
	String substring){
		List<UsuarioSubstringProjection> usuarios = cacheU.buscaPorSubstring(substring);

        if (usuarios.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("Erro", "Nenhum usuário encontrado com esse trecho"));
        }

        return ResponseEntity.ok(usuarios);
		
	}
	
	@PutMapping("/{id}")
	public ResponseEntity<Usuario> atualizaUsuario(@PathVariable Long id, @RequestParam("usuario") String usuarioJson, @RequestParam(value = "file", required = false)MultipartFile arquivo) {
		Optional<Usuario> op = repU.findById(id);
		
		if (op.isEmpty()) {
	        return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
	    }
		
		ObjectMapper mapper = new ObjectMapper();
	    mapper.registerModule(new com.fasterxml.jackson.datatype.jsr310.JavaTimeModule());

	    Usuario usuarioAtualizado;
	    try {
	    	usuarioAtualizado = mapper.readValue(usuarioJson, Usuario.class);
		} catch (IOException e) {
			e.printStackTrace();
	        return ResponseEntity.badRequest().build();
		}
	    
	    Usuario usuarioExistente = op.get();
	    
	    usuarioExistente.setNomeCompleto(usuarioAtualizado.getNomeCompleto());
	    usuarioExistente.setDataNasc(usuarioAtualizado.getDataNasc());
	    usuarioExistente.setEmail(usuarioAtualizado.getEmail());
	    usuarioExistente.setUsuario(usuarioAtualizado.getEmail());
	    String encryptPassword = new BCryptPasswordEncoder().encode(usuarioAtualizado.getSenha());
	    usuarioExistente.setSenha(encryptPassword);
	    
	    Usuario usuarioSalvo = repU.save(usuarioExistente);
	    
	    if(arquivo != null && !arquivo.isEmpty()) {
	    	try {
				byte[] bytes = arquivo.getBytes();
				String idString = Long.toString(id);
				
				Path pastaImagens = Paths.get("imagens").toAbsolutePath();
				
				Path caminhoImagem = pastaImagens.resolve(usuarioExistente.getNomeCompleto()+id);
	            Files.write(caminhoImagem, bytes);
	            
	            usuarioExistente.setNomeImagem(usuarioExistente.getNomeCompleto()+idString);
	            usuarioSalvo = repU.save(usuarioExistente);
			}catch (Exception e) {
				e.printStackTrace();
	            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();			
	        }
	    }
	    cacheU.removerCache();
	    return ResponseEntity.ok(usuarioSalvo);
	}
	
	@PutMapping("/{id}/alternar-atividade")
	public ResponseEntity<?> alternarAtividade(@PathVariable Long id) {
	    Optional<Usuario> optUsuario = repU.findById(id);
	    if (optUsuario.isEmpty()) {
	        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("erro", "Usuário não encontrado"));
	    }

	    Usuario usuario = optUsuario.get();
	    usuario.setAtivo(!usuario.isAtivo()); // alterna o status
	    repU.save(usuario);

	    return ResponseEntity.ok(Map.of("mensagem", "Atividade do usuário atualizada", "ativo", usuario.isAtivo()));
	}

	
	
	
}


