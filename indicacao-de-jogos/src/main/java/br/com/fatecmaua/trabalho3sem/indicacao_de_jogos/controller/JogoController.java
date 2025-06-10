package br.com.fatecmaua.trabalho3sem.indicacao_de_jogos.controller;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import com.fasterxml.jackson.databind.ObjectMapper;

import br.com.fatecmaua.trabalho3sem.indicacao_de_jogos.Projection.JogoSubstringProjection;
import br.com.fatecmaua.trabalho3sem.indicacao_de_jogos.model.Jogo;
import br.com.fatecmaua.trabalho3sem.indicacao_de_jogos.repository.JogoRepository;
import br.com.fatecmaua.trabalho3sem.indicacao_de_jogos.service.JogoCachingService;

@RestController
@RequestMapping(value = "/jogos")
public class JogoController {

    private final UsuarioController usuarioController;
	private static Path caminho = Paths.get("imagens").toAbsolutePath();

	@Autowired
	private JogoRepository repJ;
	@Autowired
	private JogoCachingService cacheJ;

    JogoController(UsuarioController usuarioController) {
        this.usuarioController = usuarioController;
    }
	
    @GetMapping(value = "/mostrar-imagem/{imagem}")
    @ResponseBody
    public ResponseEntity<byte[]> retornarImagem(@PathVariable("imagem") String imagem) throws IOException {
        if (imagem != null && !imagem.trim().isEmpty()) {
            Path caminhoImagem = caminho.resolve(imagem);

            System.out.println("Buscando imagem: " + caminhoImagem); // debug

            if (Files.exists(caminhoImagem)) {
                byte[] imagemBytes = Files.readAllBytes(caminhoImagem);
                String contentType = Files.probeContentType(caminhoImagem);
                MediaType mediaType = MediaType.parseMediaType(contentType != null ? contentType : "application/octet-stream");

                return ResponseEntity.ok()
                    .contentType(mediaType)
                    .body(imagemBytes);
            } else {
                System.out.println("Arquivo não encontrado: " + caminhoImagem); // debug
                return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
            }
        }

        return ResponseEntity.badRequest().build();
    }



	@GetMapping(value = "/todos")
	public List<Jogo> retornaTodosJogos() {
		return cacheJ.findAll();
	}

	@GetMapping(value = "/{id}")
	public Jogo retornaJogo(@PathVariable Long id) {
		Optional<Jogo> op = cacheJ.findById(id);
		if (op.isPresent()) {
			return op.get();
		} else {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND);
		}
	}

	@PostMapping(value = "/novo", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public ResponseEntity<?> inserirJogo(
	    @RequestParam("jogo") String jogoJson,
	    @RequestParam(value = "file", required = false) MultipartFile arquivo
	) {
	    ObjectMapper mapper = new ObjectMapper();
	    mapper.registerModule(new com.fasterxml.jackson.datatype.jsr310.JavaTimeModule());

	    Jogo jogo = null;

	    try {
	        jogo = mapper.readValue(jogoJson, Jogo.class);
	    } catch (IOException e) {
	        e.printStackTrace();
	        return ResponseEntity.badRequest().body("Erro ao desserializar JSON: " + e.getMessage());
	    }
	    if (repJ.findByNome(jogo.getNome()).isPresent()) {
	        return ResponseEntity
		            .status(HttpStatus.CONFLICT)
		            .body("Já existe um jogo com esse nome.");
		    }

	    Jogo jogoSalvo = repJ.save(jogo);

	    try {
	        if (arquivo != null && !arquivo.isEmpty()) {
	            byte[] bytes = arquivo.getBytes();
	            String id = Long.toString(jogoSalvo.getId());
	            
	            Path pastaImagens = Paths.get("imagens").toAbsolutePath();
	            
	            
	            Path caminhoImagem = pastaImagens.resolve(jogo.getNome()+id + arquivo.getOriginalFilename());
	            Files.write(caminhoImagem, bytes);

	            jogoSalvo.setNomeImagem(jogo.getNome()+id + arquivo.getOriginalFilename());
	            jogoSalvo = repJ.save(jogoSalvo);
	        }
	    } catch (IOException e) {
	        e.printStackTrace();
	    }


	    cacheJ.removerCache();
	    return ResponseEntity.status(HttpStatus.CREATED).body(jogoSalvo);
	}



	@DeleteMapping("/deletar/{id}")
	public Jogo removerJogo(@PathVariable Long id) {
		Optional<Jogo> op = repJ.findById(id);

		if (op.isPresent()) {
			Jogo jogo = op.get();
			repJ.deleteById(id);
			cacheJ.removerCache();
			return jogo;
		} else {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND);
		}
	}

	@PutMapping(value = "/atualizar/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public ResponseEntity<Jogo> atualizaJogo(
	    @PathVariable Long id,
	    @RequestParam("jogo") String jogoJson,
	    @RequestParam(value = "file", required = false) MultipartFile arquivo
	) {
	    Optional<Jogo> op = repJ.findById(id);
	    if (op.isEmpty()) {
	        return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
	    }

	    // Desserializa o JSON do jogo atualizado
	    ObjectMapper mapper = new ObjectMapper();
	    mapper.registerModule(new com.fasterxml.jackson.datatype.jsr310.JavaTimeModule());

	    Jogo jogoAtualizado;
	    try {
	        jogoAtualizado = mapper.readValue(jogoJson, Jogo.class);
	    } catch (IOException e) {
	        e.printStackTrace();
	        return ResponseEntity.badRequest().build();
	    }

	    Jogo jogoExistente = op.get();

	    // Atualiza campos
	    jogoExistente.setNome(jogoAtualizado.getNome());
	    jogoExistente.setDescricao(jogoAtualizado.getDescricao());
	    jogoExistente.setData_lancamento(jogoAtualizado.getData_lancamento());
	    jogoExistente.setGenero(jogoAtualizado.getGenero());
	    jogoExistente.setDesenvolvedora(jogoAtualizado.getDesenvolvedora());
	    jogoExistente.setPublicadora(jogoAtualizado.getPublicadora());

	    Jogo jogoSalvo = repJ.save(jogoExistente);
	    // Salva arquivo se existir
	    if (arquivo != null && !arquivo.isEmpty()) {
	        try {
	            byte[] bytes = arquivo.getBytes();
	            String idString = Long.toString(id);

	            Path pastaImagens = Paths.get("imagens").toAbsolutePath();

	            Path caminhoImagem = pastaImagens.resolve(jogoExistente.getNome()+idString + arquivo.getOriginalFilename());
	            Files.write(caminhoImagem, bytes);

	            jogoExistente.setNomeImagem(jogoExistente.getNome()+idString + arquivo.getOriginalFilename());
	            jogoSalvo = repJ.save(jogoExistente);
	        } catch (IOException e) {
	            e.printStackTrace();
	            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
	        }
	    }


	    cacheJ.removerCache();
	    return ResponseEntity.ok(jogoSalvo);
	}


	@GetMapping("/substring")
	public List<JogoSubstringProjection> buscaPorSubstring(@RequestParam(value = "substring") String substring) {

		return cacheJ.buscaPorSubstring(substring);

	}
}
