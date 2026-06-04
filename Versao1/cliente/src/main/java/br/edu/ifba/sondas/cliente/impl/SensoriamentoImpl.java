package br.edu.ifba.sondas.cliente.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import br.edu.ifba.sondas.cliente.sensoriamento.Sensoriamento;

public class SensoriamentoImpl implements Sensoriamento<Leitura> {

    // O(N)  
    // N - total de leituras
    // justificativa: o algoritmo possui apenas um laço de repetição simples
    // consequências: o crescimento é linear, mesmo que a entrada de dados seja muito grande 
    // o tempo de execução será proporcional e controlado
    @Override
    public List<Leitura> gerar(int totalLeituras) {
        List<Leitura> leituras = new ArrayList<>();
        Random rand = new Random();

        for (int i = 0; i < totalLeituras; i++) {
            double oxigenio = rand.nextDouble() * 30.0; // 0 a 30%
            double radiacao = rand.nextDouble() * 200.0; // 0 a 200 
            double temperatura = -100.0 + (rand.nextDouble() * 200.0); // -100°C a 100°C
            boolean agua = rand.nextBoolean(); // true ou false

            leituras.add(new Leitura("SND-00" + i + "-L" + i, oxigenio, radiacao, temperatura, agua));
        }
        return leituras;
    }
}