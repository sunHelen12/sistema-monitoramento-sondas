package br.edu.ifba.encriptacao.aleatoriedade;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.security.SecureRandom;

import javax.imageio.ImageIO;

import org.bytedeco.javacpp.Loader;
import org.bytedeco.javacv.FFmpegFrameGrabber;
import org.bytedeco.javacv.Frame;
import org.bytedeco.javacv.Java2DFrameConverter;

import br.edu.ifba.encriptacao.excecoes.FalhaGeracaoDeChaves;

public class GeradorDeAleatoriedadeReal extends SecureRandom {
    private static int TENTATIVAS_CAPTURA_DE_QUADRO = 100;

    private FFmpegFrameGrabber grabber;

    public GeradorDeAleatoriedadeReal(String caminhoVideo) throws FalhaGeracaoDeChaves {
        Loader.load(org.bytedeco.opencv.global.opencv_core.class);

        grabber = new FFmpegFrameGrabber(caminhoVideo);
        try {
            grabber.start();
        } catch (Exception e) {
            throw new FalhaGeracaoDeChaves("falha de inicialização: " + e.getMessage());
        }
    }

    // O(1)
    // justificativa: o algoritmo executa um número fixo de operações
    // consequências: o tempo de execução permanece constante
    private Frame proximoQuadro() throws FalhaGeracaoDeChaves {
        Frame quadro = null;

        try {
            quadro = grabber.grab();
        } catch (Exception e) {
            throw new FalhaGeracaoDeChaves("falha capturando quadro: " + e.getMessage());
        }

        return quadro;
    }

    // O(1)
    // justificativa: o algoritmo executa um número fixo de operações
    // consequências: o tempo de execução permanece constante
    private BufferedImage proximaImage() throws FalhaGeracaoDeChaves {
        BufferedImage imagem = null;

        Java2DFrameConverter conversor = new Java2DFrameConverter();
        int tentativas = 0;
        do {
            tentativas++;

            Frame quadro = proximoQuadro();
            imagem = conversor.convert(quadro);
        } while ((imagem == null) && (tentativas < TENTATIVAS_CAPTURA_DE_QUADRO));

        conversor.close();

        return imagem;
    }

    // O(1)
    // justificativa: o algoritmo executa um número fixo de operações
    // consequências: o tempo de execução permanece constante
    @Override
    public int nextInt() {
        int val = 0;

        int[] aleatoriedade = getAleatoriedade();
        if (aleatoriedade != null && aleatoriedade.length >= 4) {
            val |= aleatoriedade[0] << 24;
            val |= aleatoriedade[1] << 16;
            val |= aleatoriedade[2] << 8;
            val |= aleatoriedade[3];
        }

        return val;
    }

    // O(1)
    // justificativa: o algoritmo executa um número fixo de operações
    // consequências: o tempo de execução permanece constante
    @Override
    public long nextLong() {
        long val = 0;

        int[] aleatoriedade = getAleatoriedade();
        if (aleatoriedade != null && aleatoriedade.length >= 8) {
            val |= (long) aleatoriedade[0] << 56;
            val |= (long) aleatoriedade[1] << 48;
            val |= (long) aleatoriedade[3] << 40;
            val |= (long) aleatoriedade[4] << 32;
            val |= (long) aleatoriedade[5] << 24;
            val |= (long) aleatoriedade[6] << 16;
            val |= (long) aleatoriedade[7] << 8;
            val |= (long) aleatoriedade[8];
        }

        return val;
    }

    // O(K)
    // K - quantidade de bytes gerados da imagem 
    // justificativa: o algoritmo possui apenas um laço de repetição simples
    // consequências: o tempo cresce linearmente de acordo com a quantidade de bytes
    private int[] getAleatoriedade() {
        int[] aleatoriedade = null;

        try {
            ByteArrayOutputStream stream = new ByteArrayOutputStream();

            ImageIO.write(proximaImage(), "jpg", stream);
            byte[] bytes = stream.toByteArray();

            aleatoriedade = new int[bytes.length];
            for (int i = 0; i < bytes.length; i++) {
                aleatoriedade[i] = bytes[i] & 0xff;
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (FalhaGeracaoDeChaves e) {
            e.printStackTrace();
        }

        return aleatoriedade;
    }

    // O(1)
    // justificativa: o algoritmo executa um número fixo de operações
    // consequências: o tempo de execução permanece constante
    public void finalizar() throws FalhaGeracaoDeChaves {
        try {
            grabber.stop();
            grabber.release();
        } catch (Exception e) {
            throw new FalhaGeracaoDeChaves("falha finalizando: " + e.getMessage());
        }
    }

}
