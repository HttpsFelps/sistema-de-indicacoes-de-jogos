package br.com.fatecmaua.trabalho3sem.indicacao_de_jogos.dto;

public class AvaliacaoDto {
    private Long idUsuario;
    private Long idJogo;
    private String comentario;
    private Boolean recomenda;

    public Long getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(Long idUsuario) {
        this.idUsuario = idUsuario;
    }

    public Long getIdJogo() {
        return idJogo;
    }

    public void setIdJogo(Long idJogo) {
        this.idJogo = idJogo;
    }

    public String getComentario() {
        return comentario;
    }

    public void setComentario(String comentario) {
        this.comentario = comentario;
    }

    public Boolean getRecomenda() {
        return recomenda;
    }

    public void setRecomenda(Boolean recomenda) {
        this.recomenda = recomenda;
    }
    
}
