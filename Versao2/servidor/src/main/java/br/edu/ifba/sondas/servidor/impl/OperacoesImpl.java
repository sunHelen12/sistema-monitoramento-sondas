package br.edu.ifba.sondas.servidor.impl;

import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;
import java.util.TreeMap;

import br.edu.ifba.sondas.servidor.operacoes.Operacoes;

public class OperacoesImpl implements Operacoes<Sonda, Leitura> {

    private static final int LIMIAR_ROTACIONAMENTO_LEITURAS = 40;

    private Map<Sonda, Queue<Leitura>> bancoDeDados = new TreeMap<>();
    private Map<Sonda, Double> probabilidades = new TreeMap<>();

    // O(1)
    // justificativa: o algoritmo executa um número fixo de operações
    // consequências: o tempo de execução permanece constante
    @Override
    public void gravar(Sonda sonda, Leitura leitura) {
        Queue<Leitura> leituras = new LinkedList<>();
        if (bancoDeDados.containsKey(sonda)) {
            leituras = bancoDeDados.get(sonda);
        } else {
            bancoDeDados.put(sonda, leituras);
        }

        try {
            Thread.sleep(5);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        if (leituras.size() > LIMIAR_ROTACIONAMENTO_LEITURAS) {
            leituras.poll();
            System.out.println("limite de rotacionamento atingido, última leitura descartada");
        }
        leituras.add(leitura);

        System.out.println("gravada nova leitura para a sonda: " + sonda);
        System.out.println("---- DADOS DEPOIS DA DESENCRIPTAÇÃO ----");
        System.out.println(leitura);
    }

    // O(1)
    // justificativa: o algoritmo executa um número fixo de operações
    // consequências: o tempo de execução permanece constante
    @Override
    public void gravar(Sonda sonda, double probabilidade) {
        System.out.printf("probabilidade informada pela sonda %s: %.2f%%\n",
                sonda.getIdentificacao(), probabilidade);

        double acumulado = probabilidade;
        if (probabilidades.containsKey(sonda)) {
            acumulado += probabilidades.get(sonda);
        }
        probabilidades.put(sonda, acumulado);
    }

    // O(N)
    // N - quantidade de probabilidades armazenadas
    // justificativa: o algoritmo possui apenas um laço de repetição simples
    // consequências: o tempo cresce linearmente de acordo com a quantidade de probabilidades 
    @Override
    public double obterTotalProbabilidades() {
        double total = 0;

        for (Double probabilidade : probabilidades.values()) {
            total += probabilidade;
        }

        return total;
    }

}
