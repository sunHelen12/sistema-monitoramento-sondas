package br.edu.ifba.sondas.cliente;

import br.edu.ifba.sondas.impl.Sonda;

public class App {
    private static final int TOTAL_SONDAS = 10;
    public static void main(String[] args) {
        //O(N)
        // N - total de sondas
        // justificativa: um laço simples que itera 10 vezes para instanciar as Threads.
        // consequências: operação extremamente leve (O(N)), finalizada quase instantaneamente.
        System.out.println("""
                =====================================================================================
                        Iniciando Sistema de Lançamento de Sondas (Versào 1 - Sem Otimização)...
                =====================================================================================       
                """);
        for (int i = 1; i <= TOTAL_SONDAS ; i++) {
            Sonda sonda = new Sonda("SND-00" + i, "Kepler-" + (100 + i));

            SondaTask tarefa = new SondaTask(sonda);
            Thread threadSonda = new Thread(tarefa);

            threadSonda.start();
        }
        System.out.println("Todas as " + TOTAL_SONDAS + " sondas estão ativadas!");
    }
}
