package br.edu.ifba.sondas.cliente.impl;

public class Sonda implements Comparable<Sonda> {
    private String identificacao;
    private String planetaAlvo;

    public Sonda(String identificacao, String planetaAlvo) {
        this.identificacao = identificacao;
        this.planetaAlvo = planetaAlvo;
    }

    public String getIdentificacao() {
        return identificacao;
    }

    public void setIdentificacao(String identificacao) {
        this.identificacao = identificacao;
    }

    public String getPlanetaAlvo() {
        return planetaAlvo;
    }

    public void setPlanetaAlvo(String planetaAlvo) {
        this.planetaAlvo = planetaAlvo;
    }

    @Override
    public String toString() {
        return "Sonda ID: " + identificacao + " | Planeta: " + planetaAlvo;
    }

    @Override
    public int compareTo(Sonda outra) {
        return this.identificacao.compareTo(outra.getIdentificacao());
    }
}
