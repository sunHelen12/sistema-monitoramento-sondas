package br.edu.ifba.sondas.cliente.impl;

import java.io.File;
import java.io.FileInputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.KeyFactory;
import java.security.PublicKey;
import java.security.spec.X509EncodedKeySpec;
import java.util.ArrayList;
import java.util.Base64;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.crypto.Cipher;

import br.edu.ifba.sondas.cliente.comunicacao.Cliente;
import br.edu.ifba.sondas.cliente.comunicacao.Resultado;
import br.edu.ifba.sondas.cliente.sensoriamento.Sensoriamento;

public class ClienteImpl implements Cliente<Sonda, Leitura>, Runnable {

    private static final int TOTAL_DE_LEITURAS = 1000;
    private static final int TAMANHO_JANELA_PROBABILIDADE = 10;

    private static final String URL_SERVIDOR = "http://localhost:8080";
    private static final String URL_SONDAS = URL_SERVIDOR + "/sondas/";

    private static final String ALGORITMO_ENCRIPTACAO = "RSA";
    private static final String CAMINHO_CHAVE_PUBLICA = "chave/ch_publica.chv";

    private static final double LIMIAR_ENVIO_TEMPERATURA = 5.0;
    private static final double LIMIAR_ENVIO_OXIGENIO = 5.0;

    private Sonda sonda = null;
    private Sensoriamento<Leitura> sensoriamento = null;

    private Leitura ultimaLeitura = new Leitura(0, 0, 0, false);

    private PublicKey chave = null;

    // O(1)
    @Override
    public void configurar(Sonda sonda, Sensoriamento<Leitura> sensoriamento) throws Exception {
        this.sonda = sonda;
        this.sensoriamento = sensoriamento;
        this.chave = getChave();
    }

    // O(1)
    private PublicKey getChave() throws Exception {
        File arquivo = new File(CAMINHO_CHAVE_PUBLICA);
        FileInputStream stream = new FileInputStream(arquivo);

        byte[] bytes = stream.readAllBytes();
        stream.close();

        X509EncodedKeySpec spec = new X509EncodedKeySpec(bytes);
        KeyFactory kf = KeyFactory.getInstance(ALGORITMO_ENCRIPTACAO);

        return kf.generatePublic(spec);
    }

    // O(1)
    private byte[] encriptar(String dados) throws Exception {
        Cipher cifrador = Cipher.getInstance(ALGORITMO_ENCRIPTACAO);
        cifrador.init(Cipher.ENCRYPT_MODE, chave);

        return cifrador.doFinal(dados.getBytes());
    }

    @SuppressWarnings("deprecation")
    // O(1)
    @Override
    public Resultado enviar(Leitura leitura) throws Exception {
        Resultado resultado = Resultado.SUCESSO;

        Map<String, String> json = new HashMap<>();
        json.put("id", sonda.getIdentificacao());
        json.put("planeta", sonda.getPlanetaAlvo());
        json.put("oxigenio", leitura.getNivelOxigenio() + "");
        json.put("radiacao", leitura.getNivelRadiacao() + "");
        json.put("temperatura", leitura.getTemperatura() + "");
        json.put("agua", leitura.isAguaSubterranea() + "");

        String jsonPlain = montarJson(json);

        System.out.println("---- DADOS ANTES DA ENCRIPTAÇÃO [" + sonda.getIdentificacao() + "] ----");
        System.out.println(jsonPlain);

        URL urlEnvio = new URL(URL_SONDAS + "leituras/"
                + new String(Base64.getUrlEncoder().encode(encriptar(jsonPlain))));

        HttpURLConnection conexao = (HttpURLConnection) urlEnvio.openConnection();
        conexao.setRequestMethod("POST");
        if (conexao.getResponseCode() != 200) {
            resultado = Resultado.ERRO;
            throw new Exception("erro de conexão com o servidor");
        }
        conexao.disconnect();

        return resultado;
    }

    @SuppressWarnings("deprecation")
    // O(1)
    @Override
    public Resultado enviar(double probabilidade) throws Exception {
        Resultado resultado = Resultado.SUCESSO;

        Map<String, String> json = new HashMap<>();
        json.put("id", sonda.getIdentificacao());
        json.put("probabilidade", String.format("%.4f", probabilidade));

        String jsonPlain = montarJson(json);

        System.out.println("---- PROBABILIDADE ANTES DA ENCRIPTAÇÃO [" + sonda.getIdentificacao() + "] ----");
        System.out.println(jsonPlain);

        URL urlEnvio = new URL(URL_SONDAS + "probabilidades/"
                + new String(Base64.getUrlEncoder().encode(encriptar(jsonPlain))));

        HttpURLConnection conexao = (HttpURLConnection) urlEnvio.openConnection();
        conexao.setRequestMethod("POST");
        if (conexao.getResponseCode() != 200) {
            resultado = Resultado.ERRO;
            throw new Exception("erro de conexão com o servidor");
        }
        conexao.disconnect();

        return resultado;
    }

    // O(1)
    @Override
    public boolean ocorreuDiferencaSignificativa(Leitura leituraAtual, Leitura ultimaLeitura,
            double limiarTemperatura, double limiarOxigenio) {
        double diferencaTemperatura = Math.abs(leituraAtual.getTemperatura() - ultimaLeitura.getTemperatura());
        double diferencaOxigenio = Math.abs(leituraAtual.getNivelOxigenio() - ultimaLeitura.getNivelOxigenio());

        return diferencaOxigenio > limiarOxigenio || diferencaTemperatura > limiarTemperatura;
    }

    // O(F)
    private String montarJson(Map<String, String> campos) {
        StringBuilder json = new StringBuilder("{");
        int i = 0;
        for (Map.Entry<String, String> campo : campos.entrySet()) {
            if (i > 0) {
                json.append(",");
            }
            json.append("\"").append(campo.getKey()).append("\":\"").append(campo.getValue()).append("\"");
            i++;
        }
        json.append("}");
        return json.toString();
    }

    // O(M^2)
    private double calcularProbabilidadeDeVida(List<Leitura> leituras) {
        int m = leituras.size();
        double scorePlanetaTotal = 0;
        int comparacoes = 0;

        for (int i = 0; i < m; i++) {
            for (int j = i + 1; j < m; j++) {
                Leitura le1 = leituras.get(i);
                Leitura le2 = leituras.get(j);

                double probabilidadeCombinada = 0;

                if (le1.getNivelOxigenio() > 15.0 && le2.getNivelOxigenio() > 15.0) {
                    probabilidadeCombinada += 30;
                }
                if (le1.getNivelRadiacao() < 100.0 && le2.getNivelRadiacao() < 100.0) {
                    probabilidadeCombinada += 25;
                }
                if ((le1.getTemperatura() >= 0 && le1.getTemperatura() <= 50)
                        && (le2.getTemperatura() >= 0 && le2.getTemperatura() <= 50)) {
                    probabilidadeCombinada += 20;
                }
                if (le1.isAguaSubterranea() || le2.isAguaSubterranea()) {
                    probabilidadeCombinada += 25;
                }

                scorePlanetaTotal += probabilidadeCombinada;
                comparacoes++;
            }
        }

        return comparacoes > 0 ? (scorePlanetaTotal / comparacoes) : 0;
    }

    // O(N * M^2)
    @Override
    public void run() {
        List<Leitura> leituras = sensoriamento.gerar(TOTAL_DE_LEITURAS);
        List<Leitura> janela = new ArrayList<>();

        for (Leitura leitura : leituras) {
            janela.add(leitura);
            if (janela.size() > TAMANHO_JANELA_PROBABILIDADE) {
                janela.remove(0);
            }

            if (ocorreuDiferencaSignificativa(leitura, ultimaLeitura,
                    LIMIAR_ENVIO_TEMPERATURA, LIMIAR_ENVIO_OXIGENIO)) {
                System.out.println("[" + sonda.getIdentificacao() + "] leitura e probabilidade sendo enviadas...");

                try {
                    double probabilidade = calcularProbabilidadeDeVida(janela);
                    System.out.printf("[%s] Probabilidade de vida calculada localmente: %.2f%%\n",
                            sonda.getIdentificacao(), probabilidade);

                    enviar(leitura);
                    enviar(probabilidade);

                    Thread.sleep(50);
                } catch (Exception e) {
                    e.printStackTrace();
                }

                ultimaLeitura = leitura;
            } else {
                System.out.println("[" + sonda.getIdentificacao()
                        + "] não ocorreram diferenças significativas desde a última leitura");
            }
        }
    }

}
