package br.com.fatecmaua.trabalho3sem.indicacao_de_jogos.dto;

public class AvaliacaoResponseDto {
    private String nomeUsuario;
    private String nomeJogo;
    private String comentario;
    private Boolean recomenda;
    private String data;
    private String hora;

    public AvaliacaoResponseDto(String nomeUsuario, String nomeJogo, String comentario, Boolean recomenda, String data, String hora) {
        this.nomeUsuario = nomeUsuario;
        this.nomeJogo = nomeJogo;
        this.comentario = comentario;
        this.recomenda = recomenda;
        this.data = data;
        this.hora = hora;
    }

    public String getNomeUsuario() { return nomeUsuario; }
    public String getNomeJogo() { return nomeJogo; }
    public String getComentario() { return comentario; }
    public Boolean getRecomenda() { return recomenda; }
    public String getData() { return data; }
    public String getHora() { return hora; }
}
