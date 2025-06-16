package br.com.fatecmaua.trabalho3sem.indicacao_de_jogos.controller;

import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
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
	public ResponseEntity<?> login(@RequestBody @Valid AuthenticationDTO data) {
		if (repU.findByEmail(data.email()) != null) {
			var usernamePassword = new UsernamePasswordAuthenticationToken(data.email(), data.senha());
			var auth = this.authenticationManager.authenticate(usernamePassword);
			
			var token = tokenService.gerarToken((Usuario) auth.getPrincipal());
			
			return ResponseEntity.ok(new LoginResponseDTO(token));
		} else {
			return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("Erro", "Usuário não encontrado"));
		}
		
	}
	
	@PostMapping("/registro")
	public ResponseEntity<?> registro(@RequestBody @Valid RegisterDTO data) {
		if(this.repU.findByEmail(data.email()) != null) return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("Erro", "Usuário já registrado"));
		
		String encryptPassword = new BCryptPasswordEncoder().encode(data.senha());
		Usuario newUsuario = new Usuario(data.cargo(),data.nome_completo(), data.usuario(),data.email(), encryptPassword, data.dataNasc(), data.imagemUsuario());
		
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


}


