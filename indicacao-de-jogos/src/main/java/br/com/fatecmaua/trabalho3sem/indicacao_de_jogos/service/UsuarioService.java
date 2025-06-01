package br.com.fatecmaua.trabalho3sem.indicacao_de_jogos.service;

import java.util.Collections;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import br.com.fatecmaua.trabalho3sem.indicacao_de_jogos.model.Usuario;
import br.com.fatecmaua.trabalho3sem.indicacao_de_jogos.repository.UsuarioRepository;

@Service
public class UsuarioService implements UserDetailsService {
	@Autowired
	private UsuarioRepository repU;

	
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        return repU.findByEmail(email);
    }
}
