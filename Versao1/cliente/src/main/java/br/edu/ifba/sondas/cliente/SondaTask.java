package br.edu.ifba.sondas.cliente;

import jakarta.ws.rs.client.Client;
import jakarta.ws.rs.client.ClientBuilder;
import jakarta.ws.rs.client.Entity;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import br.edu.ifba.sondas.cliente.impl.Leitura;
import br.edu.ifba.sondas.cliente.impl.SensoriamentoImpl;
import br.edu.ifba.sondas.cliente.impl.Sonda;
import br.edu.ifba.sondas.cliente.sensoriamento.Sensoriamento;

import java.util.List;

public class SondaTask implements Runnable {

    private Sonda sonda;
    private Sensoriamento<Leitura> sensoriamento;

    public SondaTask(Sonda sonda){
        this.sonda = sonda;
        this.sensoriamento = new SensoriamentoImpl();
    }

    // O(N)
    // N - total de leituras geradas por ciclo
    // justificativa: o método gerar() possui complexidade linear O(N).
    // consequências: como não há otimização nesta Versão 1, a Thread vai gerar
    // e tentar enviar todos os N dados indiscriminadamente, consumindo banda e CPU.
    @Override
    public void run(){
        Client client = ClientBuilder.newClient();

        String  urlServidor = "http://localhost:8080/api/receber/" + sonda.getIdentificacao();

        try {
            // Loop infiniro para simular a sonda ligada 24/7 no espaço
            while (true){
                System.out.println("[" + sonda.getIdentificacao() + "] Coletando dados em " + sonda.getPlanetaAlvo() + "...");

                List<Leitura> pacoteDeDados = sensoriamento.gerar(10);

                System.out.println("[" + sonda.getIdentificacao() + "] Enviando dados para o servidor...");

                try {
                    Response resposta = client.target(urlServidor)
                            .request(MediaType.APPLICATION_JSON)
                            .post(Entity.entity(pacoteDeDados, MediaType.APPLICATION_JSON));

                    if (resposta.getStatus() == 200) {
                        System.out.println("[" + sonda.getIdentificacao() + "] Dados recebidos pela base Terrestre!");
                    }else {
                        System.out.println("[" + sonda.getIdentificacao() + "] Falha na comunicação: HTTP " + resposta.getStatus());
                    }

                } catch (Exception e) {
                    System.err.println("[" + sonda.getIdentificacao() + "] Servidor fora do ar! A base não responde.");
                }

                Thread.sleep(1000);
            }
        } catch (InterruptedException e) {
            System.err.println("Sonda " + sonda.getIdentificacao() + " foi interrompida.");
        }finally {
            client.close();
        }
    }

}
