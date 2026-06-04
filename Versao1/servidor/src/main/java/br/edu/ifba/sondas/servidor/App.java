package br.edu.ifba.sondas.servidor;

import org.glassfish.grizzly.Grizzly;
import org.glassfish.grizzly.http.server.HttpServer;
import org.glassfish.jersey.grizzly2.httpserver.GrizzlyHttpServerFactory;
import org.glassfish.jersey.server.ResourceConfig;

import java.net.URI;

public class App {
    public static void main(String[] args) {
        // O(1)
        // justificativa: método de inicialização do servidor, não depende de volume de dados.
        // consequências: o servidor sobe rapidamente e trava a thread principal aguardando conexões.


        System.out.println("""
                ========================================================================================================
                                       BASE TERRESTRE ATIVADA (Versão 1 - Sem Otimização)
                ========================================================================================================
                """);

        String baseUri = "http://localhost:8080/api/";

        ResourceConfig config = new ResourceConfig().packages("br.edu.ifba.sondas.servidor");

        try {
            HttpServer server = GrizzlyHttpServerFactory.createHttpServer(URI.create(baseUri), config);
            System.out.println("Servidor iniciado! Acesse: " + baseUri);

            System.in.read();
            server.shutdownNow();

        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}