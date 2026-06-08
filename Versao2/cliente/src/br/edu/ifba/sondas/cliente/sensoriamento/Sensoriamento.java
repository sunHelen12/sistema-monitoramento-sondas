package br.edu.ifba.sondas.cliente.sensoriamento;

import java.util.List;

public interface Sensoriamento<Leitura> {

    public List<Leitura> gerar(int totalLeituras);

}
