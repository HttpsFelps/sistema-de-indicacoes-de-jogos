package br.com.fatecmaua.trabalho3sem.indicacao_de_jogos.controller;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.fatecmaua.trabalho3sem.indicacao_de_jogos.dto.AvaliacaoDto;
import br.com.fatecmaua.trabalho3sem.indicacao_de_jogos.dto.AvaliacaoResponseDto;
import br.com.fatecmaua.trabalho3sem.indicacao_de_jogos.model.Avaliacao;
import br.com.fatecmaua.trabalho3sem.indicacao_de_jogos.model.Jogo;
import br.com.fatecmaua.trabalho3sem.indicacao_de_jogos.model.Usuario;
import br.com.fatecmaua.trabalho3sem.indicacao_de_jogos.repository.AvaliacaoRepository;
import br.com.fatecmaua.trabalho3sem.indicacao_de_jogos.repository.JogoRepository;
import br.com.fatecmaua.trabalho3sem.indicacao_de_jogos.repository.UsuarioRepository;

@RestController
@RequestMapping("/avaliacoes")
public class AvaliacaoController {

    @Autowired
    private AvaliacaoRepository avaliacaoRepository;

    @Autowired
    private JogoRepository jogoRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @GetMapping
    public List<AvaliacaoResponseDto> listarTodas() {
        DateTimeFormatter formatterData = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        DateTimeFormatter formatterHora = DateTimeFormatter.ofPattern("HH:mm");

        return avaliacaoRepository.findAll().stream().map(av -> {
            String nomeUsuario = av.getUsuario().getNomeCompleto();
            String nomeJogo = av.getJogo().getNome();
            String comentario = av.getComentario();
            Boolean recomenda = av.getRecomenda();
            String data = av.getDataHora().format(formatterData);
            String hora = av.getDataHora().format(formatterHora);
            return new AvaliacaoResponseDto(nomeUsuario, nomeJogo, comentario, recomenda, data, hora);
        }).toList();
    }

    @GetMapping("/jogo/{idJogo}")
    public List<Avaliacao> listarPorJogo(@PathVariable Long idJogo) {
        Optional<Jogo> jogo = jogoRepository.findById(idJogo);
        return jogo.map(avaliacaoRepository::findByJogo).orElse(null);
    }

    @PostMapping
    public ResponseEntity<?> criarAvaliacao(@RequestBody AvaliacaoDto dto) {
        Optional<Usuario> usuarioOpt = usuarioRepository.findById(dto.getIdUsuario());
        Optional<Jogo> jogoOpt = jogoRepository.findById(dto.getIdJogo());
        
        if (usuarioOpt.isEmpty() || jogoOpt.isEmpty()) {
            return ResponseEntity.badRequest().body("Usuário ou Jogo não encontrado");
        }

        Avaliacao avaliacao = avaliacaoRepository
            .findByUsuarioAndJogo(usuarioOpt.get(), jogoOpt.get())
            .orElse(new Avaliacao());

        avaliacao.setUsuario(usuarioOpt.get());
        avaliacao.setJogo(jogoOpt.get());
        avaliacao.setComentario(dto.getComentario());
        avaliacao.setRecomenda(dto.getRecomenda());
        avaliacao.setDataHora(LocalDateTime.now());

        return ResponseEntity.ok(avaliacaoRepository.save(avaliacao));
    }

    @GetMapping("/jogo/{idJogo}/estatisticas")
    public Map<String, Object> estatisticas(@PathVariable Long idJogo) {
        Optional<Jogo> jogo = jogoRepository.findById(idJogo);
        if (jogo.isPresent()) {
            List<Avaliacao> avaliacoes = avaliacaoRepository.findByJogo(jogo.get());
            long total = avaliacoes.size();
            long recomendam = avaliacoes.stream().filter(Avaliacao::getRecomenda).count();
            long naoRecomendam = total - recomendam;
            return Map.of(
                "total", total,
                "recomendam", recomendam,
                "naoRecomendam", naoRecomendam,
                "percentualRecomendam", total > 0 ? (100.0 * recomendam / total) : 0,
                "percentualNaoRecomendam", total > 0 ? (100.0 * naoRecomendam / total) : 0
            );
        }
        return Map.of("erro", "Jogo não encontrado");
    }

    @GetMapping("/usuario/{idUsuario}")
    public ResponseEntity<?> listarPorUsuario(@PathVariable Long idUsuario) {
        Optional<Usuario> usuarioOpt = usuarioRepository.findById(idUsuario);
        if (usuarioOpt.isEmpty()) {
            return ResponseEntity.badRequest().body("Usuário não encontrado");
        }

        List<Avaliacao> avaliacoes = avaliacaoRepository.findByUsuario(usuarioOpt.get());
        return ResponseEntity.ok(avaliacoes);
    }
    
    @DeleteMapping("/deletar/{id}")
	public ResponseEntity<?> removerAvaliacao(@PathVariable Long id) {
		Optional<Avaliacao> op = avaliacaoRepository.findById(id);
		
		if(op.isPresent()) {
			Avaliacao avaliacao = op.get();
			avaliacaoRepository.deleteById(id);
			return ResponseEntity.ok(avaliacao);
		} else {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("Erro", "Nenhuma avaliação encontrada"));
		}
	}
}
