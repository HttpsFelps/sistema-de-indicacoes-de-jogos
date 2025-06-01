package br.com.fatecmaua.trabalho3sem.indicacao_de_jogos.model;

public enum CargoUsuario {

    ADMIN("admin"),
    USER("user");

    private String cargo;

    CargoUsuario(String cargo){
        this.cargo = cargo;
    }

    public String getCargo(){
        return cargo;
    }
}
