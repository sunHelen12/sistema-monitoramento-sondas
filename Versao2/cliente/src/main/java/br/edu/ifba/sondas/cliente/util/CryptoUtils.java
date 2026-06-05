package br.edu.ifba.sondas.cliente.util;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.GCMParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.nio.file.*;
import java.security.MessageDigest;
import java.security.SecureRandom;
import java.util.Arrays;

public class CryptoUtils {
    private static final String SEED_FILE = "entropy.bin";
    private static final int AES_KEY_BYTES = 16;
    private static final int IV_BYTES = 12;

    public static byte[] getOrCreateSharedSeed() {
        try {
            Path p = Paths.get(SEED_FILE);
            if (Files.exists(p)) return Files.readAllBytes(p);
            SecureRandom sr = SecureRandom.getInstanceStrong();
            byte[] seed = new byte[32];
            sr.nextBytes(seed);
            Files.write(p, seed);
            System.out.println("Seed gerada (salve e copie para o outro lado se necessário).");
            return seed;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private static SecretKey deriveKey(byte[] seed) throws Exception {
        MessageDigest sha = MessageDigest.getInstance("SHA-256");
        byte[] keyBytes = sha.digest(seed);
        return new SecretKeySpec(Arrays.copyOf(keyBytes, AES_KEY_BYTES), "AES");
    }

    public static byte[][] encrypt(byte[] plain, byte[] seed) throws Exception {
        SecretKey key = deriveKey(seed);
        byte[] iv = new byte[IV_BYTES];
        SecureRandom.getInstanceStrong().nextBytes(iv);
        Cipher c = Cipher.getInstance("AES/GCM/NoPadding");
        c.init(Cipher.ENCRYPT_MODE, key, new GCMParameterSpec(128, iv));
        byte[] cipher = c.doFinal(plain);
        return new byte[][]{iv, cipher};
    }

    public static byte[] decrypt(byte[] iv, byte[] cipherText, byte[] seed) throws Exception {
        SecretKey key = deriveKey(seed);
        Cipher c = Cipher.getInstance("AES/GCM/NoPadding");
        c.init(Cipher.DECRYPT_MODE, key, new GCMParameterSpec(128, iv));
        return c.doFinal(cipherText);
    }
}