package br.edu.ifba.sondas.servidor;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.util.Base64;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import br.edu.ifba.sondas.servidor.impl.Leitura;
import br.edu.ifba.sondas.servidor.impl.OperacoesImpl;
import br.edu.ifba.sondas.servidor.impl.Sonda;
import br.edu.ifba.sondas.servidor.operacoes.Operacoes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Path("sondas")
public class Rotas {

    private static Operacoes<Sonda, Leitura> operacoes = null;

    // O(1)
    // justificativa: o algoritmo executa um número fixo de operações
    // consequências: o tempo de execução permanece constante
    private static Operacoes<Sonda, Leitura> getOperacoes() {
        if (operacoes == null) {
            operacoes = new OperacoesImpl();
        }

        return operacoes;
    }

    private static final String INFORMACOES = "serviço de atendimento a sondas espaciais, v2.0";
    private static final String ALGORITMO_DE_ENCRIPTACAO = "RSA";
    private static final String CAMINHO_CHAVE_PRIVADA = "chave/ch_privada.chv";

    private PrivateKey chave = null;

    // O(1)
    // justificativa: o algoritmo executa um número fixo de operações
    // consequências: o tempo de execução permanece constante
    private PrivateKey getChavePrivada() throws IOException, NoSuchAlgorithmException, InvalidKeySpecException {
        if (chave == null) {
            File arquivo = new File(CAMINHO_CHAVE_PRIVADA);
            FileInputStream stream = new FileInputStream(arquivo);
            byte[] bytes = stream.readAllBytes();
            stream.close();

            PKCS8EncodedKeySpec spec = new PKCS8EncodedKeySpec(bytes);
            KeyFactory kf = KeyFactory.getInstance(ALGORITMO_DE_ENCRIPTACAO);
            chave = kf.generatePrivate(spec);
        }

        return chave;
    }

    // O(1)
    // justificativa: o algoritmo executa um número fixo de operações
    // consequências: o tempo de execução permanece constante
    private String desencriptar(byte[] encriptado) throws NoSuchAlgorithmException, NoSuchPaddingException,
            InvalidKeyException, InvalidKeySpecException, IOException, IllegalBlockSizeException, BadPaddingException {
        Cipher cipher = Cipher.getInstance(ALGORITMO_DE_ENCRIPTACAO);
        cipher.init(Cipher.DECRYPT_MODE, getChavePrivada());

        byte[] desencriptado = cipher.doFinal(encriptado);

        return new String(desencriptado);
    }

    @GET
    @Path("/")
    public Response getInformacoes() {
        return Response.ok(INFORMACOES, MediaType.TEXT_PLAIN).build();
    }

    @POST
    @Path("/leituras/{dados}")
    public Response gravarLeitura(@PathParam("dados") String dados) {
        Response resposta = Response.serverError().build();

        System.out.println("dados encriptados: " + dados);

        try {
            String json = desencriptar(Base64.getUrlDecoder().decode(dados));

            ObjectMapper mapeador = new ObjectMapper();
            JsonNode dic = mapeador.readTree(json);

            Sonda sonda = new Sonda(dic.get("id").asText(), dic.get("planeta").asText());
            Leitura leitura = new Leitura(
                    dic.get("oxigenio").asDouble(),
                    dic.get("radiacao").asDouble(),
                    dic.get("temperatura").asDouble(),
                    Boolean.parseBoolean(dic.get("agua").asText()));

            getOperacoes().gravar(sonda, leitura);

            resposta = Response.ok().build();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return resposta;
    }

    @POST
    @Path("/probabilidades/{dados}")
    public Response gravarProbabilidade(@PathParam("dados") String dados) {
        Response resposta = Response.serverError().build();

        System.out.println("dados encriptados: " + dados);

        try {
            String json = desencriptar(Base64.getUrlDecoder().decode(dados));

            ObjectMapper mapeador = new ObjectMapper();
            JsonNode dic = mapeador.readTree(json);

            Sonda sonda = new Sonda(dic.get("id").asText(), "desconhecido");
            double probabilidade = dic.get("probabilidade").asDouble();

            getOperacoes().gravar(sonda, probabilidade);

            resposta = Response.ok().build();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return resposta;
    }

    @GET
    @Path("probabilidades")
    public Response obterProbabilidades() {
        double total = getOperacoes().obterTotalProbabilidades();

        return Response.ok(String.format("%.4f", total), MediaType.TEXT_PLAIN).build();
    }

}
