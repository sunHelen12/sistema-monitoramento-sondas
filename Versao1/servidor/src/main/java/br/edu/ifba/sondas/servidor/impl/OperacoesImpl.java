package br.edu.ifba.sondas.servidor.impl;

import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import br.edu.ifba.sondas.servidor.operacoes.Operacoes;
import br.edu.ifba.sondas.servidor.ordenacao.TipoOrdenacao;


public class OperacoesImpl implements Operacoes<Sonda, Leitura> {

    // d.1 - O(N)
    // N - número de sondas.
    // justificativa: apenas um laço de repetição simples 
    // consequências: operação muito rápida, mesmo com milhares de sondas, a listagem será 
    // concluída em tempo linear
    @Override
    public void imprimir(List<Sonda> monitorados) {
        for (Sonda sonda : monitorados) {
            System.out.println("Sonda em operação: " + sonda);
        }
    }

    // d.2 - O(N * M)
    // N - número de sondas e M - número de leituras 
    // justificativa: existe um laço externo e um laço 
    // consequências: o crescimento é proporcional ao volume total de dados gerados, em casos de 
    // entradas extremas pode demorar um pouco para finalizar a impressão no console.
    @Override
    public void imprimir(Map<Sonda, List<Leitura>> leituras) {
        for (Sonda sonda : leituras.keySet()) {
            System.out.println("\nLeituras da " + sonda.getIdentificacao() + " no planeta " + sonda.getPlanetaAlvo());
            for (Leitura leitura : leituras.get(sonda)) {
                System.out.println(leitura);
            }
        }
    }

    // d.3 - O(N * M^2)
    // N - número de sondas e M - número de leituras 
    // justificativa: o laço principal percorre as N sondas, dentro dele, é chamado o método ordenar() 
    // do OrdenadorImpl que executa o algoritmo bubble sort de complexidade quadrática
    // consequências: alto risco de gargalo de processamento, muitas entradas podem quebrar o funcionamento da execução
    @Override
    public Map<Sonda, List<Leitura>> ordenar(Map<Sonda, List<Leitura>> leituras, TipoOrdenacao tipoOrdenacao) {
        Map<Sonda, List<Leitura>> leiturasOrdenadas = new TreeMap<>();

        for (Sonda sonda : leituras.keySet()) {
            List<Leitura> listaLeituras = leituras.get(sonda);
            OrdenadorImpl ordenador = new OrdenadorImpl(listaLeituras, tipoOrdenacao);
            ordenador.ordenar();
            leiturasOrdenadas.put(sonda, listaLeituras);
        }
        return leiturasOrdenadas;
    }

    // d.4 - O(N * M^2)
    // N - número de sondas e M - número de leituras.
    // justificativa: o algoritmo utiliza dois laços aninhados (i e j) cruzando todas as M leituras entre si
    // consequências: se a massa de dados coletados por uma sonda for muito grande, o cálculo de combinação 
    // explodirá exponencialmente o uso da CPU, resultando em grande lentidão no cálculo da probabilidade
    @Override
    public void calcularProbabilidadeDeVida(Map<Sonda, List<Leitura>> dados) {
        System.out.println("\nProbabilidade de vida em % -----------------------------------------");
        
        for (Sonda sonda : dados.keySet()) {
            List<Leitura> leituras = dados.get(sonda);
            int m = leituras.size();
            
            double scorePlanetaTotal = 0;
            int comparacoes = 0;

            for (int i = 0; i < m; i++) {
                for (int j = i + 1; j < m; j++) {
                    Leitura le1 = leituras.get(i);
                    Leitura le2 = leituras.get(j);
                    
                    double probabilidadeCombinada = 0;

                    if (le1.getNivelOxigenio() > 15.0 && le2.getNivelOxigenio() > 15.0) probabilidadeCombinada += 30;
                    if (le1.getNivelRadiacao() < 100.0 && le2.getNivelRadiacao() < 100.0) probabilidadeCombinada += 25;
                    if ((le1.getTemperatura() >= 0 && le1.getTemperatura() <= 50) && 
                        (le2.getTemperatura() >= 0 && le2.getTemperatura() <= 50)) probabilidadeCombinada += 20;
                    if (le1.isAguaSubterranea() || le2.isAguaSubterranea()) probabilidadeCombinada += 25;

                    scorePlanetaTotal += probabilidadeCombinada;
                    comparacoes++;
                }
            }

            double probabilidadeFinal = comparacoes > 0 ? (scorePlanetaTotal / comparacoes) : 0;
            System.out.printf("Planeta %s | Probabilidade de sustentar vida: %.2f%%\n", 
                    sonda.getPlanetaAlvo(), probabilidadeFinal);
        }
    }

}