package com.example.webbongden.utils;

import java.io.*;
import java.nio.file.*;
import java.security.*;
import java.security.cert.Certificate;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.util.Base64;

public class VerifyTool {
    public static void main(String[] args) {
        try {
            String keystorePath = "src/main/webapp/WEB-INF/keystore.p12";
            String keystorePassword = "keystorePassword";
            String alias = "myalias";

            String filePath = "src/main/webapp/sample.txt";
            String signaturePath = "src/main/webapp/sample.txt.sig";

            boolean isValid = verifySignature(filePath, signaturePath, keystorePath, keystorePassword, alias);
            System.out.println("✅ Chữ ký hợp lệ: " + isValid);

        } catch (Exception e) {
            System.err.println("❌ Lỗi xác minh chữ ký: " + e.getMessage());
        }
    }

    public static boolean verifySignature(String filePath, String signaturePath,
                                          String keystorePath, String keystorePassword, String alias) throws Exception {

        KeyStore keyStore = KeyStore.getInstance("PKCS12");
        try (InputStream keystoreStream = new FileInputStream(keystorePath)) {
            keyStore.load(keystoreStream, keystorePassword.toCharArray());
        }

        Certificate cert = keyStore.getCertificate(alias);
        if (cert == null) throw new RuntimeException("Không tìm thấy certificate với alias: " + alias);

        Signature signature = Signature.getInstance("SHA256withRSA");
        signature.initVerify(cert.getPublicKey());

        byte[] fileData = Files.readAllBytes(Paths.get(filePath));
        signature.update(fileData);

        byte[] sigBytes = Files.readAllBytes(Paths.get(signaturePath));
        return signature.verify(sigBytes);
    }
}
