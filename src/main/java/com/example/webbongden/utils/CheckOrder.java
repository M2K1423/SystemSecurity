package com.example.webbongden.utils;

import com.example.webbongden.dao.model.Order;
import com.example.webbongden.services.PublicKeyServices;

import javax.crypto.Cipher;
import java.nio.charset.StandardCharsets;
import java.security.KeyFactory;
import java.security.MessageDigest;
import java.security.PublicKey;
import java.security.Signature;
import java.security.spec.X509EncodedKeySpec;
import java.text.SimpleDateFormat;
import java.util.Base64;
import java.util.Date;
import java.util.TimeZone;

public class CheckOrder {

    public static String getPublickey(Order order) {
        PublicKeyServices publicKeyServices = new PublicKeyServices();
        return publicKeyServices.getPublicKey(order.getAccountId()).getPublicKey();
    }

    public static PublicKey decodePublicKey(String base64PublicKey) throws Exception {
        byte[] decodedBytes = Base64.getDecoder().decode(base64PublicKey);
        X509EncodedKeySpec spec = new X509EncodedKeySpec(decodedBytes);
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        return keyFactory.generatePublic(spec);
    }

    private static String generateRawData(int orderId, String customerName, double total, Date createdAt) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        sdf.setTimeZone(TimeZone.getTimeZone("UTC"));
        String totalStr = String.format("%.2f", total);
        customerName = customerName.trim();
        return orderId + customerName + totalStr + sdf.format(createdAt);
    }
    public static byte[] generateDataByBytes(Order order) {
        return generateRawData(order.getId(), order.getCustomerName(), order.getTotalPrice(), order.getCreatedAt()).getBytes(StandardCharsets.UTF_8);
    }
    public static String decodeSignature(Order order) throws Exception {
        byte[] signatureBytes = Base64.getDecoder().decode(order.getDigitalSignature());
        Cipher cipher = Cipher.getInstance("RSA");
        cipher.init(Cipher.DECRYPT_MODE, decodePublicKey(getPublickey(order)));
        byte[] decryptedHash = cipher.doFinal(signatureBytes); // đây chính là SHA-256 hash (32 bytes)

        return Base64.getEncoder().encodeToString(decryptedHash); // Trả về dạng String

    }
    public static byte[] getSignedBytes(Order order) throws Exception {
        String signature = order.getDigitalSignature();
        return Base64.getDecoder().decode(signature);
    }
    public static boolean checkOrder(Order order) throws Exception{
//        String rawData = generateRawData(order.getId(), order.getCustomerName(), order.getTotalPrice(), order.getCreatedAt());
//        MessageDigest digest = MessageDigest.getInstance("SHA-256");
//        byte[] hashOrder = digest.digest(rawData.getBytes(StandardCharsets.UTF_8));
//        String hashOrderBase64 = Base64.getEncoder().encodeToString(hashOrder);
//        String decodedSignature = decodeSignature(order);
//        return decodedSignature.equals(hashOrderBase64);
        Signature verifier = Signature.getInstance("SHA256withRSA");
        verifier.initVerify(decodePublicKey(getPublickey(order)));
        verifier.update(generateDataByBytes(order));
        return verifier.verify(getSignedBytes(order));
    }
}
