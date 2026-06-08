package br.edu.ifba.sondas.servidor.impl;

public class Sonda implements Comparable<Sonda> {

    private String identificacao = "";
    private String planetaAlvo = "";

    public Sonda(String identificacao, String planetaAlvo) {
        this.identificacao = identificacao;
        this.planetaAlvo = planetaAlvo;
    }

    public String getIdentificacao() {
        return identificacao;
    }

    public String getPlanetaAlvo() {
        return planetaAlvo;
    }

    @Override
    public String toString() {
        return "sonda: " + identificacao + " | planeta: " + planetaAlvo;
    }

    @Override
    public int compareTo(Sonda outra) {
        return identificacao.compareTo(outra.getIdentificacao());
    }

}
