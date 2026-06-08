package br.edu.ifba.sondas.servidor;

import java.io.IOException;
import java.net.URI;

import org.glassfish.grizzly.http.server.HttpServer;
import org.glassfish.jersey.grizzly2.httpserver.GrizzlyHttpServerFactory;
import org.glassfish.jersey.server.ResourceConfig;

public class App {

    private static final String BASE_URL = "http://0.0.0.0:8080/";

    // O(1)
    // justificativa: o algoritmo executa um número fixo de operações
    // consequências: o tempo de execução permanece constante
    private static HttpServer iniciarServidor() {
        ResourceConfig configuracao = new ResourceConfig().packages("br.edu.ifba.sondas.servidor");

        return GrizzlyHttpServerFactory.createHttpServer(URI.create(BASE_URL), configuracao);
    }

    public static void main(String[] args) throws IOException {
        HttpServer servidor = iniciarServidor();
        System.out.println("atendendo sondas espaciais...");
        System.in.read();
        servidor.shutdown();
    }
}
