package com.example.webbongden.utils;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.*;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.util.Base64;

public class SignTool {
    public static void signFile(String keystorePath, String keystorePassword, String alias, String keyPassword, String filePath) throws Exception {
        KeyStore ks = KeyStore.getInstance("PKCS12");
        ks.load(new FileInputStream(keystorePath), keystorePassword.toCharArray());

        PrivateKey privateKey = (PrivateKey) ks.getKey(alias, keyPassword.toCharArray());

        Signature signature = Signature.getInstance("SHA256withRSA");
        signature.initSign(privateKey);

        byte[] fileData = Files.readAllBytes(Paths.get(filePath));
        signature.update(fileData);
        byte[] signedData = signature.sign();

        String base64Signature = Base64.getEncoder().encodeToString(signedData);
        Files.write(Paths.get(filePath + ".sig"), base64Signature.getBytes());

        System.out.println("✅ File đã được ký: " + filePath + ".sig");
    }

    public static void main(String[] args) throws Exception {
        String keystore = "src/main/webapp/WEB-INF/keystore.p12";
        String fileToSign = "src/main/resources/db.properties";
        signFile(keystore, "keystorePassword", "myalias", "keystorePassword", fileToSign);
    }

    public static boolean verify(String filePath, String sigPath, String certPath) throws Exception {
        // Đọc nội dung file cần kiểm tra
        byte[] fileData = Files.readAllBytes(Paths.get(filePath));

        // Đọc nội dung chữ ký
        byte[] signatureBytes = Base64.getDecoder().decode(Files.readAllBytes(Paths.get(sigPath)));

        // Đọc chứng chỉ
        CertificateFactory cf = CertificateFactory.getInstance("X.509");
        X509Certificate cert = (X509Certificate) cf.generateCertificate(new FileInputStream(certPath));

        Signature signature = Signature.getInstance("SHA256withRSA");
        signature.initVerify(cert.getPublicKey());
        signature.update(fileData);

        return signature.verify(signatureBytes);
    }
}
