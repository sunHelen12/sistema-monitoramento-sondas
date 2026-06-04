package br.edu.ifba.sondas.servidor;

import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import br.edu.ifba.sondas.servidor.impl.Leitura;
import br.edu.ifba.sondas.servidor.impl.OperacoesImpl;
import br.edu.ifba.sondas.servidor.impl.Sonda;
import br.edu.ifba.sondas.servidor.operacoes.Operacoes;
import br.edu.ifba.sondas.servidor.ordenacao.TipoOrdenacao;

import java.util.List;
import java.util.Map;
import java.util.TreeMap;

@Path("receber")
public class Rotas {

    private static Operacoes<Sonda, Leitura> operacoes = new OperacoesImpl();

    // O(N * M^2)
    // N - número de sondas (1 por requisição)
    // M - quantidade de leituras
    // justificativa: o método orquestra todas as operações sobre os dados recebidos.
    // A operação dominante é calcularProbabilidadeDeVida(), de complexidade O(M^2),
    // chamada para cada uma das N sondas. A ordenação também é O(M^2) via Bubble Sort,
    // mas como ambas são sequenciais, a complexidade total permanece O(N * M^2).
    // consequências: na Versão 1, sem otimizações, todo o processamento pesado ocorre
    // no servidor. Com 10 sondas enviando dados simultaneamente, o servidor acumula
    // requisições e pode sofrer gargalo severo de CPU.
    @POST
    @Path("/{idSonda}")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response receberDadosDaSonda(@PathParam("idSonda") String idSonda, List<Leitura> leituras) {
        System.out.println("""
                ======================================================================
                                          BASE TERRESTRE
                ======================================================================
                """);
        System.out.println("Dados Recebidos da Sonda " + idSonda + " | Total de leituras: " + leituras.size());

        Sonda sonda = new Sonda(idSonda, "Planeta Desconhecido");

        // O(1) - apenas inserção em um TreeMap já existente
        Map<Sonda, List<Leitura>> dados = new TreeMap<>();
        dados.put(sonda, leituras);

        // d.2 - O(N * M)
        // imprime todas as leituras recebidas de cada sonda
        operacoes.imprimir(dados);

        // d.3 - O(N * M^2)
        // ordena as leituras por temperatura usando Bubble Sort
        Map<Sonda, List<Leitura>> ordenados = operacoes.ordenar(dados, TipoOrdenacao.POR_TEMPERATURA);

        // d.4 - O(N * M^2)
        // calcula a probabilidade de vida combinando pares de leituras
        operacoes.calcularProbabilidadeDeVida(ordenados);

        return Response.ok().build();
    }
}