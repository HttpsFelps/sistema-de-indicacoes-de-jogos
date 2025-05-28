package br.com.fatecmaua.trabalho3sem.indicacao_de_jogos.service;

import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import br.com.fatecmaua.trabalho3sem.indicacao_de_jogos.model.Usuario;
import br.com.fatecmaua.trabalho3sem.indicacao_de_jogos.repository.UsuarioRepository;

@Service
public class UsuarioService {
	@Autowired
	private UsuarioRepository repU;
	
	private BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
	
	public Usuario salvarUsuario(Usuario usuario) {
		usuario.setSenha(passwordEncoder.encode(usuario.getSenha()));
		return repU.save(usuario);
	}
	
	public boolean autenticar(String email, String senha) {
		Optional<Usuario> op = repU.findByEmail(email);
		if(op.isPresent()) {
			return passwordEncoder.matches(senha, op.get().getSenha());
		}
		return false;
	}
}
