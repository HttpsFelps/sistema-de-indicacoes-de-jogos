package br.com.fatecmaua.trabalho3sem.indicacao_de_jogos.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
public class Avaliacao {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private Usuario usuario;

    @ManyToOne
    private Jogo jogo;
    private String comentario;
    private Boolean recomenda;
    private LocalDateTime dataHora;

    public Avaliacao() {}

    public Avaliacao(Usuario usuario, Jogo jogo, String comentario, Boolean recomenda, LocalDateTime dataHora) {
        this.usuario = usuario;
        this.jogo = jogo;
        this.comentario = comentario;
        this.recomenda = recomenda;
        this.dataHora = dataHora;
    }

    public Long getId() { return id; }
    public Usuario getUsuario() { return usuario; }
    public void setUsuario(Usuario usuario) { this.usuario = usuario; }
    public Jogo getJogo() { return jogo; }
    public void setJogo(Jogo jogo) { this.jogo = jogo; }
    public String getComentario() { return comentario; }
    public void setComentario(String comentario) { this.comentario = comentario; }
    public Boolean getRecomenda() { return recomenda; }
    public void setRecomenda(Boolean recomenda) { this.recomenda = recomenda; }
    public LocalDateTime getDataHora() { return dataHora; }
    public void setDataHora(LocalDateTime dataHora) { this.dataHora = dataHora; }
}
