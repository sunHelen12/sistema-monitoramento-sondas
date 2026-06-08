package br.edu.ifba.sondas.cliente.impl;

public class Leitura {

    private double nivelOxigenio = 0;
    private double nivelRadiacao = 0;
    private double temperatura = 0;
    private boolean aguaSubterranea = false;

    // O(1)
    public Leitura(double nivelOxigenio, double nivelRadiacao, double temperatura, boolean aguaSubterranea) {
        this.nivelOxigenio = nivelOxigenio;
        this.nivelRadiacao = nivelRadiacao;
        this.temperatura = temperatura;
        this.aguaSubterranea = aguaSubterranea;
    }

    // O(1)
    public double getNivelOxigenio() {
        return nivelOxigenio;
    }

    // O(1)
    public void setNivelOxigenio(double nivelOxigenio) {
        this.nivelOxigenio = nivelOxigenio;
    }

    // O(1)
    public double getNivelRadiacao() {
        return nivelRadiacao;
    }

    // O(1)
    public void setNivelRadiacao(double nivelRadiacao) {
        this.nivelRadiacao = nivelRadiacao;
    }

    // O(1)
    public double getTemperatura() {
        return temperatura;
    }

    // O(1)
    public void setTemperatura(double temperatura) {
        this.temperatura = temperatura;
    }

    // O(1)
    public boolean isAguaSubterranea() {
        return aguaSubterranea;
    }

    // O(1)
    public void setAguaSubterranea(boolean aguaSubterranea) {
        this.aguaSubterranea = aguaSubterranea;
    }

    // O(1)
    @Override
    public String toString() {
        return String.format("O2: %.1f%% | Radiação: %.1f MeV | Temp: %.1f°C | Água: %b",
                nivelOxigenio, nivelRadiacao, temperatura, aguaSubterranea);
    }

}
