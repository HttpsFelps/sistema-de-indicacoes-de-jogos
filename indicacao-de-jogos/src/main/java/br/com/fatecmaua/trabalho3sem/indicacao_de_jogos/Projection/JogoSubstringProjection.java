package br.com.fatecmaua.trabalho3sem.indicacao_de_jogos.Projection;

import java.util.Date;

public interface JogoSubstringProjection {
    String getNome_jogo();
    String getNome_desenvolvedora();
    Date getData_fundacao_dev();
    String getPais_origem_dev();
    String getNome_publicadora();
    Date getData_fundacao_pub();
    String getPais_origem_pub();
    String getGenero_jogo();
    String getDescricao_jogo();
    Date getData_lancamento_jogo();
    String getImg_jogo();
}
