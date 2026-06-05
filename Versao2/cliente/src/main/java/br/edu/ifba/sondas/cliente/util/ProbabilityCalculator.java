package br.edu.ifba.sondas.cliente.util;

import br.edu.ifba.sondas.cliente.impl.Leitura;
import java.util.List;

// O(M^2)
public class ProbabilityCalculator {
    public static double calcularProbabilidade(List<Leitura> leituras) {
        int m = leituras.size();
        double total = 0; int comps = 0;
        for (int i = 0; i < m; i++) {
            for (int j = i+1; j < m; j++) {
                Leitura a = leituras.get(i), b = leituras.get(j);
                double score = 0;
                if (a.getNivelOxigenio() > 15 && b.getNivelOxigenio() > 15) score += 30;
                if (a.getNivelRadiacao() < 100 && b.getNivelRadiacao() < 100) score += 25;
                if ((a.getTemperatura()>=0 && a.getTemperatura()<=50) && (b.getTemperatura()>=0 && b.getTemperatura()<=50)) score +=20;
                if (a.isAguaSubterranea() || b.isAguaSubterranea()) score += 25;
                total += score; comps++;
            }
        }
        return comps > 0 ? (total / comps) : 0.0;
    }
}
