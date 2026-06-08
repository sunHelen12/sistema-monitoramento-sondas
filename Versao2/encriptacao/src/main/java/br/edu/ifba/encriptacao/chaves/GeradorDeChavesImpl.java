package br.edu.ifba.encriptacao.chaves;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;

import br.edu.ifba.encriptacao.aleatoriedade.GeradorDeAleatoriedadeReal;
import br.edu.ifba.encriptacao.excecoes.FalhaGeracaoDeChaves;

public class GeradorDeChavesImpl implements GeradorDeChaves<GeradorDeAleatoriedadeReal> {

    private static int TAMANHO_CHAVES_ENCRIPTACAO = 2048;

    private GeradorDeAleatoriedadeReal geradorDeAleatoriedade = null;
    private String algoritmoDeEncriptacao = null;

    // O(1)
    // justificativa: o algoritmo executa um número fixo de operações
    // consequências: o tempo de execução permanece constante
    @Override
    public void inicializar(GeradorDeAleatoriedadeReal geradorDeAleatoriedade, String algoritmoDeEncriptacao) {
        this.geradorDeAleatoriedade = geradorDeAleatoriedade;
        this.algoritmoDeEncriptacao = algoritmoDeEncriptacao;
    }

    // O(1)
    // justificativa: o algoritmo executa um número fixo de operações
    // consequências: o tempo de execução permanece constante
    @Override
    public KeyPair gerarChaves() throws FalhaGeracaoDeChaves {
        KeyPair chaves = null;

        try {
            KeyPairGenerator geradorDePares = KeyPairGenerator.getInstance(algoritmoDeEncriptacao);
            geradorDePares.initialize(TAMANHO_CHAVES_ENCRIPTACAO, geradorDeAleatoriedade);

            chaves = geradorDePares.generateKeyPair();
        } catch (NoSuchAlgorithmException e) {
            throw new FalhaGeracaoDeChaves("falha gerando chave: " + e.getMessage());
        }

        return chaves;
    }

    // O(1)
    // justificativa: o algoritmo executa um número fixo de operações
    // consequências: o tempo de execução permanece constante
    @Override
    public KeyPair gerarChaves(String caminhoChavePrivada, String caminhoChavePublica) throws FalhaGeracaoDeChaves {
        KeyPair chaves = gerarChaves();

        byte[] bytes = chaves.getPublic().getEncoded();
        gravar(caminhoChavePublica, bytes);

        bytes = chaves.getPrivate().getEncoded();
        gravar(caminhoChavePrivada, bytes);

        return chaves;
    }

    // O(1)
    // justificativa: o algoritmo executa um número fixo de operações
    // consequências: o tempo de execução permanece constante
    private void gravar(String caminho, byte[] bytes) throws FalhaGeracaoDeChaves {
        File f = new File(caminho);
        f.getParentFile().mkdirs();

        if (f.exists()) {
            f.delete();
        }

        try (FileOutputStream stream = new FileOutputStream(f)) {
            stream.write(bytes);
        } catch (IOException e) {
            throw new FalhaGeracaoDeChaves("erro gravando chaves no arquivo");
        }
    }

    // O(1)
    // justificativa: o algoritmo executa um número fixo de operações
    // consequências: o tempo de execução permanece constante
    @Override
    public void finalizar() throws FalhaGeracaoDeChaves {
        geradorDeAleatoriedade.finalizar();
    }

}
