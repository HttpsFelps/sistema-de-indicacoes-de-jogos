package br.com.fatecmaua.trabalho3sem.indicacao_de_jogos.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.fatecmaua.trabalho3sem.indicacao_de_jogos.model.Empresa;
import br.com.fatecmaua.trabalho3sem.indicacao_de_jogos.model.EmpresasFavoritas;
import br.com.fatecmaua.trabalho3sem.indicacao_de_jogos.model.Usuario;

public interface EmpresasFavoritasRepository extends JpaRepository<EmpresasFavoritas, Long> {
	List<EmpresasFavoritas> findByUsuario_Id(Long id);
	
	boolean existsByUsuarioAndEmpresa(Usuario usuario, Empresa empresa);

	Optional<EmpresasFavoritas> findByUsuarioAndEmpresa(Usuario usuario, Empresa empresa);

	void deleteByUsuarioAndEmpresa(Usuario usuario, Empresa empresa);
	
}
