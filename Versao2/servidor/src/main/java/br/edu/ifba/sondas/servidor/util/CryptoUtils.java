package br.edu.ifba.sondas.servidor.util;

import javax.crypto.Cipher;
import javax.crypto.spec.GCMParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.nio.file.*;
import java.security.MessageDigest;
import java.security.SecureRandom;
import java.util.Arrays;

public class CryptoUtils {
    private static final String SEED_FILE = "entropy.bin";
    private static final int AES_KEY_BYTES = 16;

    public static byte[] getOrCreateSharedSeed() {
        try {
            Path p = Paths.get(SEED_FILE);
            if (Files.exists(p)) return Files.readAllBytes(p);
            SecureRandom sr = SecureRandom.getInstanceStrong();
            byte[] seed = new byte[32];
            sr.nextBytes(seed);
            Files.write(p, seed);
            System.out.println("Seed gerada no servidor (salve e copie para cliente se necessário).");
            return seed;
        } catch (Exception e) { throw new RuntimeException(e); }
    }

    private static SecretKeySpec deriveKey(byte[] seed) throws Exception {
        MessageDigest sha = MessageDigest.getInstance("SHA-256");
        byte[] keyBytes = sha.digest(seed);
        return new SecretKeySpec(Arrays.copyOf(keyBytes, AES_KEY_BYTES), "AES");
    }

    public static byte[] decrypt(byte[] iv, byte[] cipherText, byte[] seed) throws Exception {
        SecretKeySpec key = deriveKey(seed);
        Cipher c = Cipher.getInstance("AES/GCM/NoPadding");
        c.init(Cipher.DECRYPT_MODE, key, new GCMParameterSpec(128, iv));
        return c.doFinal(cipherText);
    }
}