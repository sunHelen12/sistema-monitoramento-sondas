package br.edu.ifba.sondas.cliente.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import br.edu.ifba.sondas.cliente.sensoriamento.Sensoriamento;

public class SensoriamentoImpl implements Sensoriamento<Leitura> {
    private final Random rand = new Random();
   
    // O(N)  
    // N - total de leituras
    // justificativa: o algoritmo possui apenas um laço de repetição simples
    // consequências: o crescimento é linear, mesmo que a entrada de dados seja muito grande 
    // o tempo de execução será proporcional e controlado
    @Override
    public List<Leitura> gerar(int totalLeituras) {
        List<Leitura> l = new ArrayList<>();
        for (int i = 0; i < totalLeituras; i++) {
            double o2 = rand.nextDouble() * 30.0;
            double rad = rand.nextDouble() * 200.0;
            double temp = -100.0 + (rand.nextDouble() * 200.0);
            boolean agua = rand.nextBoolean();
            l.add(new Leitura(String.format("L%03d", i+1), o2, rad, temp, agua));
        }
        return l;
    }
}