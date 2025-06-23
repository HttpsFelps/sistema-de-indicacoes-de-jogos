# 🎮 Sistema de Indicação de Jogos

Este projeto é um sistema de indicação de jogos desenvolvido com **Spring Boot** no back-end e **React** no front-end para trabalho semestral do curso de Desenvolvimento de Software Multiplataforma na FATEC Mauá. Usuários podem cadastrar, visualizar e recomendar jogos, além de curtir as indicações de outros usuários.

---

## 🚀 Tecnologias Utilizadas

### Back-end (Java + Spring Boot)
- Spring Boot
- Spring Web
- Spring Data JPA
- Banco de Dados: H2
---

## 🛠️ Funcionalidades

- ✅ Cadastro de jogos com título, gênero, descrição e imagem
- ✅ Listagem dos jogos recomendados
- ✅ Sistema de likes nas recomendações
- ✅ Interface amigável com React
- ✅ Integração entre front-end e back-end via API REST

---

## Modelagem UML de classe

```mermaid
classDiagram
    class Usuario {
        +avaliarJogo(nomeJogo: String, nota: int): void
        +comentarJogo(idJogo: int, comentario: String): void
        +deletarComentario(idComentario: int): void
        +editarComentario(idComentario: int, novoTexto: String): void
        +criarConta(nomeUsuario: String, fotoPerfil: String, senha: String): void
        +atualizarFotoPerfil(novaFoto: String): void
        +indicarJogo(nomeJogo: String): void
    }

    class Moderador {
        +ativarOuDesativarUsuario(nomeUsuario: String): void
        +adicionarJogoAoCatalogo(nomeJogo: String, urlCapa: String, descricao: String, dataPublicacao: String): void
        +removerJogoDoCatalogo(nomeJogo: String): void
        +removerComentario(idComentario: int): void
    }

    class Jogo {
        -nome: String
        -descricao: String
        -urlCapa: String
        -dataPublicacao: String
        +getDetalhes(): String
    }

    class Avaliacao {
        -nota: int
        -comentario: String
        -autor: Usuario
        -jogo: Jogo
        +getResumo(): String
    }

    Usuario "1" --> "0..*" Avaliacao : escreve
    Jogo "1" --> "0..*" Avaliacao : recebe
    Moderador "1" --> "0..*" Usuario : gerencia
    Moderador "1" --> "0..*" Jogo : modera

```

---
