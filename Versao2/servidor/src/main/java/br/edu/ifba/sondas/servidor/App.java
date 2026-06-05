package br.edu.ifba.sondas.servidor;

import org.glassfish.grizzly.http.server.HttpServer;
import org.glassfish.jersey.grizzly2.httpserver.GrizzlyHttpServerFactory;
import org.glassfish.jersey.server.ResourceConfig;
import java.net.URI;

public class App {
    public static void main(String[] args) throws Exception {
        System.out.println("Base terrestre Versão 2 (otimizada) iniciando...");
        String baseUri = "http://localhost:8080/api/";
        ResourceConfig config = new ResourceConfig().packages("br.edu.ifba.sondas.servidor");
        HttpServer server = GrizzlyHttpServerFactory.createHttpServer(URI.create(baseUri), config);
        System.out.println("Servidor iniciado em " + baseUri);
        System.in.read();
        server.shutdownNow();
    }
}