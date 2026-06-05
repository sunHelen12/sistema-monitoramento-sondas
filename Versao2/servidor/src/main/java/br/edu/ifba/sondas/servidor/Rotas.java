package br.edu.ifba.sondas.servidor;

import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import br.edu.ifba.sondas.servidor.store.ServerStore;
import br.edu.ifba.sondas.servidor.util.CryptoUtils;

import java.util.Base64;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.HashMap;
import java.nio.file.Paths;
import java.nio.file.Files;

@Path("receber")
public class Rotas {
    private static final ServerStore store = ServerStore.getInstance();

    @POST
    @Path("/{idSonda}")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response receberDadosDaSonda(@PathParam("idSonda") String idSonda, String body) {
        try {
            System.out.println("---- Payload criptografado recebido (string) ----");
            System.out.println(body);

            String iv = extractField(body, "\"iv\":\"", "\"");
            String cipher = extractField(body, "\"cipher\":\"", "\"");
            if (iv == null || cipher == null) return Response.status(400).entity("Formato inválido").build();

            byte[] ivb = Base64.getDecoder().decode(iv);
            byte[] ct = Base64.getDecoder().decode(cipher);

            byte[] seed = CryptoUtils.getOrCreateSharedSeed();

            byte[] plain = CryptoUtils.decrypt(ivb, ct, seed);
            String json = new String(plain, StandardCharsets.UTF_8);

            System.out.println("---- Dados DEPOIS da decriptação ----");
            System.out.println(json);

            store.add(idSonda, json);

            return Response.ok().build();
        } catch (Exception e) {
            e.printStackTrace();
            return Response.serverError().entity(e.getMessage()).build();
        }
    }

    private String extractField(String s, String prefix, String end) {
        int i = s.indexOf(prefix);
        if (i < 0) return null;
        int j = s.indexOf(end, i + prefix.length());
        if (j < 0) return null;
        return s.substring(i + prefix.length(), j);
    }
}