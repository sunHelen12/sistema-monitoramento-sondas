package br.edu.ifba.sondas.impl;

public class Leitura {

    private String id;
    private double nivelOxigenio; // espectrômetro -> em %
    private double nivelRadiacao; // detector de partículas de energia -> em MeV
    private double temperatura;   // sensor térmico -> em °C
    private boolean aguaSubterranea; // radar de penetração do solo -> água detectada

    public Leitura() {
    }

    public Leitura(String id, double nivelOxigenio, double nivelRadiacao, double temperatura, boolean aguaSubterranea) {
        this.id = id;
        this.nivelOxigenio = nivelOxigenio;
        this.nivelRadiacao = nivelRadiacao;
        this.temperatura = temperatura;
        this.aguaSubterranea = aguaSubterranea;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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
        return String.format("ID: %s | O2: %.1f%% | Radiação: %.1f MeV | Temp: %.1f°C | Água: %b",
                id, nivelOxigenio, nivelRadiacao, temperatura, aguaSubterranea);
    }
}