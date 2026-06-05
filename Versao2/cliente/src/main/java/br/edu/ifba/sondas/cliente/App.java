package br.edu.ifba.sondas.cliente;

import br.edu.ifba.sondas.cliente.impl.Sonda;

public class App {
    private static final int TOTAL_SONDAS = 5;
    public static void main(String[] args) {
        System.out.println("Iniciando Versão 2 (com otimizações e encriptação)...");
        for (int i = 1; i <= TOTAL_SONDAS; i++) {
            String id = String.format("SND-%03d", i);
            Sonda sonda = new Sonda(id, "Kepler-" + (100 + i));
            SondaTask tarefa = new SondaTask(sonda);
            new Thread(tarefa).start();
        }
    }
}
