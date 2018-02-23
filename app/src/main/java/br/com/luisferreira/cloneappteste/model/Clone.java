package br.com.luisferreira.cloneappteste.model;

/**
 * Created by Luis Ferreira on 23/02/2018.
 */

public class Clone {
    private String nome;
    private int idade;
    private String dataCriacao;

    public Clone() {
    }

    public Clone(String nome, int idade, String dataCriacao) {
        this.nome = nome;
        this.idade = idade;
        this.dataCriacao = dataCriacao;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public int getIdade() {
        return idade;
    }

    public void setIdade(int idade) {
        this.idade = idade;
    }

    public String getDataCriacao() {
        return dataCriacao;
    }

    public void setDataCriacao(String dataCriacao) {
        this.dataCriacao = dataCriacao;
    }
}
