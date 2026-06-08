package br.edu.ifba.sondas.cliente.impl;

public class Sonda implements Comparable<Sonda> {

    private String identificacao = "";
    private String planetaAlvo = "";

    // O(1)
    public Sonda(String identificacao, String planetaAlvo) {
        this.identificacao = identificacao;
        this.planetaAlvo = planetaAlvo;
    }

    // O(1)
    public String getIdentificacao() {
        return identificacao;
    }

    // O(1)
    public void setIdentificacao(String identificacao) {
        this.identificacao = identificacao;
    }

    // O(1)
    public String getPlanetaAlvo() {
        return planetaAlvo;
    }

    // O(1)
    public void setPlanetaAlvo(String planetaAlvo) {
        this.planetaAlvo = planetaAlvo;
    }

    // O(1)
    @Override
    public String toString() {
        return "sonda: " + identificacao + " | planeta: " + planetaAlvo;
    }

    // O(1)
    @Override
    public int compareTo(Sonda outra) {
        return identificacao.compareTo(outra.getIdentificacao());
    }

}
