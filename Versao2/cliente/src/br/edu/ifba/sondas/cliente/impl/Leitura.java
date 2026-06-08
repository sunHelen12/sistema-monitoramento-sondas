package br.edu.ifba.sondas.cliente.impl;

public class Leitura {

    private double nivelOxigenio = 0;
    private double nivelRadiacao = 0;
    private double temperatura = 0;
    private boolean aguaSubterranea = false;

    public Leitura(double nivelOxigenio, double nivelRadiacao, double temperatura, boolean aguaSubterranea) {
        this.nivelOxigenio = nivelOxigenio;
        this.nivelRadiacao = nivelRadiacao;
        this.temperatura = temperatura;
        this.aguaSubterranea = aguaSubterranea;
    }

    public double getNivelOxigenio() {
        return nivelOxigenio;
    }

    public void setNivelOxigenio(double nivelOxigenio) {
        this.nivelOxigenio = nivelOxigenio;
    }

    public double getNivelRadiacao() {
        return nivelRadiacao;
    }

    public void setNivelRadiacao(double nivelRadiacao) {
        this.nivelRadiacao = nivelRadiacao;
    }

    public double getTemperatura() {
        return temperatura;
    }

    public void setTemperatura(double temperatura) {
        this.temperatura = temperatura;
    }

    public boolean isAguaSubterranea() {
        return aguaSubterranea;
    }

    public void setAguaSubterranea(boolean aguaSubterranea) {
        this.aguaSubterranea = aguaSubterranea;
    }

    @Override
    public String toString() {
        return String.format("O2: %.1f%% | Radiação: %.1f MeV | Temp: %.1f°C | Água: %b",
                nivelOxigenio, nivelRadiacao, temperatura, aguaSubterranea);
    }

}
