package br.edu.ifba.sondas.cliente;

import java.util.ArrayList;
import java.util.List;

import br.edu.ifba.sondas.cliente.impl.ClienteImpl;
import br.edu.ifba.sondas.cliente.impl.SensoriamentoImpl;
import br.edu.ifba.sondas.cliente.impl.Sonda;

public class App {

    private static final int TOTAL_SONDAS = 10;

    public static void main(String[] args) throws Exception {
        System.out.println("Iniciando Versão 2 (com otimizações e encriptação)...");

        List<Thread> processos = new ArrayList<>();

        for (int i = 0; i < TOTAL_SONDAS; i++) {
            String id = String.format("SND-%03d", i + 1);

            ClienteImpl cliente = new ClienteImpl();
            cliente.configurar(new Sonda(id, "Kepler-" + (100 + i + 1)), new SensoriamentoImpl());

            Thread processo = new Thread(cliente);
            processos.add(processo);
            processo.start();
        }

        for (Thread processo : processos) {
            processo.join();
        }

        System.out.println("leituras enviadas");
    }
}
