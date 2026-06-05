package br.edu.ifba.sondas.cliente.sensoriamento;

import java.util.List;

public interface Sensoriamento<T> {
    public List<T> gerar(int totalLeituras);
}
