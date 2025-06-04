package com.example.webbongden.utils;

import java.io.FileOutputStream;
import java.io.IOException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.Base64;

public class KeyUtils {

    public static void savePublicKey(PublicKey publicKey, String filePath) throws IOException {
        byte[] encoded = publicKey.getEncoded();
        writePemFile(encoded, "PUBLIC KEY", filePath);
    }

    public static void savePrivateKey(PrivateKey privateKey, String filePath) throws IOException {
        byte[] encoded = privateKey.getEncoded();
        writePemFile(encoded, "PRIVATE KEY", filePath);
    }

    private static void writePemFile(byte[] encoded, String type, String filePath) throws IOException {
        String base64Encoded = Base64.getMimeEncoder(64, new byte[]{'\n'}).encodeToString(encoded);
        String pem = "-----BEGIN " + type + "-----\n" +
                base64Encoded +
                "\n-----END " + type + "-----\n";
        try (FileOutputStream fos = new FileOutputStream(filePath)) {
            fos.write(pem.getBytes());
        }
    }

    public static String encodePublicKey(PublicKey publicKey) {
        byte[] encoded = publicKey.getEncoded();
        return Base64.getEncoder().encodeToString(encoded);
    }

    /** Encode private key: chỉ Base64, không PEM */
    public static String encodePrivateKey(PrivateKey privateKey) {
        byte[] encoded = privateKey.getEncoded();
        return Base64.getEncoder().encodeToString(encoded);
    }
}
