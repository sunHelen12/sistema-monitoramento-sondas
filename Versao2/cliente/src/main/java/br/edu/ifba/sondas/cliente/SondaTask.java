package br.edu.ifba.sondas.cliente;

import jakarta.ws.rs.client.Client;
import jakarta.ws.rs.client.ClientBuilder;
import jakarta.ws.rs.client.Entity;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import br.edu.ifba.sondas.cliente.impl.Leitura;
import br.edu.ifba.sondas.cliente.impl.SensoriamentoImpl;
import br.edu.ifba.sondas.cliente.impl.Sonda;
import br.edu.ifba.sondas.cliente.util.CryptoUtils;
import br.edu.ifba.sondas.cliente.util.ProbabilityCalculator;
import br.edu.ifba.sondas.cliente.util.ThresholdFilter;

import java.util.Base64;
import java.util.List;
import java.time.Instant;

public class SondaTask implements Runnable {
    private final Sonda sonda;
    private final SensoriamentoImpl sensoriamento = new SensoriamentoImpl();
    private final ThresholdFilter threshold = new ThresholdFilter(5.0); // só envia se prob dif >= 5%

    public SondaTask(Sonda sonda) {
        this.sonda = sonda;
    }

    // Complexidade O(N), cálculo de probabilidade O(M^2) (intencionalmente pesado)
    @Override
    public void run() {
        Client client = ClientBuilder.newClient();
        String urlServidor = "http://localhost:8080/api/receber/" + sonda.getIdentificacao();

        try {
            byte[] seed = CryptoUtils.getOrCreateSharedSeed();
            while (true) {
                List<Leitura> leituras = sensoriamento.gerar(20); // M = 20 (exemplo)

                double prob = ProbabilityCalculator.calcularProbabilidade(leituras); // pesado: O(M^2)
                System.out.printf("[%s] Probabilidade local: %.2f%%\n", sonda.getIdentificacao(), prob);

                if (!threshold.shouldSend(sonda.getIdentificacao(), prob)) {
                    Thread.sleep(1000);
                    continue;
                }

                String payloadPlain = buildPlainPayload(leituras, prob);

                // Encriptar
                byte[][] enc = CryptoUtils.encrypt(payloadPlain.getBytes("UTF-8"), seed);
                String ivB64 = Base64.getEncoder().encodeToString(enc[0]);
                String cipherB64 = Base64.getEncoder().encodeToString(enc[1]);

                System.out.println("---- DADOS ANTES DA ENCRIPTAÇÃO ----");
                System.out.println(payloadPlain);

                String jsonWrapper = String.format("{\"meta\":{\"sonda\":\"%s\",\"ts\":\"%s\"},\"iv\":\"%s\",\"cipher\":\"%s\"}",
                        sonda.getIdentificacao(), Instant.now().toString(), ivB64, cipherB64);

                try {
                    Response r = client.target(urlServidor)
                            .request(MediaType.APPLICATION_JSON)
                            .post(Entity.entity(jsonWrapper, MediaType.APPLICATION_JSON));

                    if (r.getStatus() == 200) System.out.println("Enviado com sucesso.");
                    else System.err.println("Falha HTTP: " + r.getStatus());
                } catch (Exception e) {
                    System.err.println("Servidor fora do ar.");
                }

                Thread.sleep(2000);
            }
        } catch (InterruptedException ex) {
            Thread.currentThread().interrupt();
        } finally {
            client.close();
        }
    }

    private String buildPlainPayload(List<Leitura> leituras, double prob) {
        StringBuilder sb = new StringBuilder();
        sb.append("{");
        sb.append("\"sonda\":\"").append(sonda.getIdentificacao()).append("\",");
        sb.append("\"probabilidade\":").append(String.format("%.4f", prob)).append(",");
        sb.append("\"amostra\":[");
        for (int i = 0; i < Math.min(5, leituras.size()); i++) {
            sb.append(leituras.get(i).toJson());
            if (i < Math.min(5, leituras.size()) - 1) sb.append(",");
        }
        sb.append("],");
        sb.append("\"total_leituras\":").append(leituras.size());
        sb.append("}");
        return sb.toString();
    }
}