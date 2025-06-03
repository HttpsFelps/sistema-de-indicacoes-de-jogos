package br.com.fatecmaua.trabalho3sem.indicacao_de_jogos.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

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
	public ResponseEntity<LoginResponseDTO> login(@RequestBody @Valid AuthenticationDTO data) {
		var usernamePassword = new UsernamePasswordAuthenticationToken(data.email(), data.senha());
		var auth = this.authenticationManager.authenticate(usernamePassword);
		
		var token = tokenService.gerarToken((Usuario) auth.getPrincipal());
		
		return ResponseEntity.ok(new LoginResponseDTO(token));
	}
	
	@PostMapping("/registro")
	public ResponseEntity<?> registro(@RequestBody @Valid RegisterDTO data) {
		if(this.repU.findByEmail(data.email()) != null) return ResponseEntity.badRequest().build();
		
		String encryptPassword = new BCryptPasswordEncoder().encode(data.senha());
		Usuario newUsuario = new Usuario(data.cargo(),data.nome_completo(), data.usuario(),data.email(), encryptPassword, data.dataNasc(), data.imagemUsuario());
		
		this.repU.save(newUsuario);
		
		return ResponseEntity.ok().build();
	}
	
	
	
	@GetMapping("/todos")
	public List<Usuario> retornaTodosusuarios(){
	    return cacheU.findAll();  
	}
	
	@GetMapping("/substring")
	public List<UsuarioSubstringProjection> 
	buscaPorSubstring(@RequestParam(value = "substring")
	String substring){
		
		return cacheU.buscaPorSubstring(substring);
		
	}


}


