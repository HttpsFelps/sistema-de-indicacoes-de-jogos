package br.com.fatecmaua.trabalho3sem.indicacao_de_jogos.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import br.com.fatecmaua.trabalho3sem.indicacao_de_jogos.model.Usuario;
import br.com.fatecmaua.trabalho3sem.indicacao_de_jogos.service.UsuarioService;

@RestController
@RequestMapping("/usuario")
public class UsuarioController {
	@Autowired
	private UsuarioService servU;
	
	@PostMapping("/registrar")
	public Usuario registrar(@RequestBody Usuario usuario) {
		return servU.salvarUsuario(usuario);
	}
	@PostMapping("/login")
	public String login(@RequestParam String email, @RequestParam String senha) {
		boolean autenticado = servU.autenticar(email, senha);
		return autenticado ? "Login bem sucedido!" : "Credenciais invalidadas, por favor tente novamente!";
	}
}
