package br.edu.ifba.sondas.impl;

import java.util.List;
import br.edu.ifba.sondas.ordenacao.Ordenador;
import br.edu.ifba.sondas.ordenacao.TipoOrdenacao;

public class OrdenadorImpl extends Ordenador<Leitura> {

    public OrdenadorImpl(List<Leitura> leituras, TipoOrdenacao tipoOrdenacao) {
        super(leituras, tipoOrdenacao);
    }

    // O(M^2) - Bubble Sort 
    // M - quantidade de leituras
    // justificativa: o método utiliza dois laços de repetição aninhados 
    // consequências: se a entrada de dados for muito grande, o tempo de processamento 
    // escalará de forma quadrática, tornando a ordenação lenta e ineficiente para grandes volumes de dados
    @Override
    public void ordenar() {
        int n = leituras.size();
        for (int i = 0; i < n - 1; i++) {
            for (int j = 0; j < n - i - 1; j++) {
                
                boolean deveTrocar = false;
                Leitura atual = leituras.get(j);
                Leitura proxima = leituras.get(j + 1);

                switch (tipoOrdenacao) {
                    case POR_TEMPERATURA:
                        deveTrocar = atual.getTemperatura() > proxima.getTemperatura();
                        break;
                    case POR_NIVEL_RADIACAO:
                        deveTrocar = atual.getNivelRadiacao() > proxima.getNivelRadiacao();
                        break;
                    case POR_NIVEL_O2:
                        deveTrocar = atual.getNivelOxigenio() > proxima.getNivelOxigenio();
                        break;
                    case POR_PRESENCA_AGUA:
                        deveTrocar = atual.isAguaSubterranea() && !proxima.isAguaSubterranea();
                        break;
                }

                if (deveTrocar) {
                    leituras.set(j, proxima);
                    leituras.set(j + 1, atual);
                }
            }
        }
    }
}