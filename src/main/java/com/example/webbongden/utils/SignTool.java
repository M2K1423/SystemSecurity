package com.example.webbongden.utils;

import java.io.File;
import java.io.FileInputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.KeyStore;
import java.security.PrivateKey;
import java.security.Signature;
import java.security.cert.X509Certificate;
import java.util.Base64;

public class SignTool {
    public static void main(String[] args) throws Exception {
        String keystorePath = "src/main/webapp/WEB-INF/keystore.p12"; // hoặc nơi bạn để file
        String keystorePassword = "keystorePassword";
        String alias = "myalias";
        String filePath = "C:/Users/PHAN PHAT/Downloads/invoice_14.txt"; // file cần ký

        // Load keystore
        KeyStore keystore = KeyStore.getInstance("PKCS12");
        keystore.load(new FileInputStream(keystorePath), keystorePassword.toCharArray());

        // Lấy private key
        PrivateKey privateKey = (PrivateKey) keystore.getKey(alias, keystorePassword.toCharArray());

        // Đọc file
        byte[] data = Files.readAllBytes(Paths.get(filePath));

        // Ký bằng SHA256withRSA
        Signature signature = Signature.getInstance("SHA256withRSA");
        signature.initSign(privateKey);
        signature.update(data);
        byte[] signedBytes = signature.sign();

        // Xuất Base64
        String base64Signature = Base64.getEncoder().encodeToString(signedBytes);
        System.out.println("✅ Đây là chữ ký số của bạn:");
        System.out.println(base64Signature);
    }
}
