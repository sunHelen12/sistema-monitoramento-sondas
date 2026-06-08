package br.edu.ifba.encriptacao;

import java.security.SecureRandom;

import br.edu.ifba.encriptacao.aleatoriedade.GeradorDeAleatoriedadeReal;
import br.edu.ifba.encriptacao.chaves.GeradorDeChaves;
import br.edu.ifba.encriptacao.chaves.GeradorDeChavesImpl;

public class App {

    private static final String CAMINHO_DO_VIDEO = "video/rajadas-solares.mp4";
    private static final String ALGORITMO_DE_ENCRIPTACAO = "RSA";

    private static final String CAMINHO_CHAVE_PUBLICA = "../cliente/chave/ch_publica.chv";
    private static final String CAMINHO_CHAVE_PRIVADA = "../servidor/chave/ch_privada.chv";

    private static final int DESLOCAMENTO_MAXIMO = 100;

    public static void main(String[] args) throws Exception {
        GeradorDeAleatoriedadeReal geradorDeAleatoriedadeReal = new GeradorDeAleatoriedadeReal(CAMINHO_DO_VIDEO);
        GeradorDeChaves<GeradorDeAleatoriedadeReal> geradorDeChaves = new GeradorDeChavesImpl();
        geradorDeChaves.inicializar(geradorDeAleatoriedadeReal, ALGORITMO_DE_ENCRIPTACAO);

        SecureRandom randomizador = new SecureRandom();
        int deslocamento = randomizador.nextInt(DESLOCAMENTO_MAXIMO);

        for (int i = 0; i <= deslocamento; i++) {
            System.out.println("deslocando " + (i + 1) + " frames");
            geradorDeAleatoriedadeReal.nextInt();
        }

        geradorDeChaves.gerarChaves(CAMINHO_CHAVE_PRIVADA, CAMINHO_CHAVE_PUBLICA);
        geradorDeChaves.finalizar();

        System.out.println("Chaves geradas com aleatoriedade real do vídeo:");
        System.out.println("Pública: " + CAMINHO_CHAVE_PUBLICA);
        System.out.println("Privada: " + CAMINHO_CHAVE_PRIVADA);
    }
}
