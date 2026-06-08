package br.edu.ifba.sondas.cliente.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import br.edu.ifba.sondas.cliente.sensoriamento.Sensoriamento;

public class SensoriamentoImpl implements Sensoriamento<Leitura> {

    // Complexidade Linear, O(N)
    @Override
    public List<Leitura> gerar(int totalLeituras) {
        List<Leitura> leituras = new ArrayList<>();

        Random randomizador = new Random();
        for (int i = 0; i < totalLeituras; i++) {
            double oxigenio = randomizador.nextDouble() * 30.0;
            double radiacao = randomizador.nextDouble() * 200.0;
            double temperatura = -100.0 + (randomizador.nextDouble() * 200.0);
            boolean agua = randomizador.nextBoolean();

            leituras.add(new Leitura(oxigenio, radiacao, temperatura, agua));
        }

        return leituras;
    }

}
