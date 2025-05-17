package com.example.webbongden.dao.model;

import java.util.Date;

public class PublicKeyWrapper {
    private int id;
    private int customerId;
    private String publicKey;
    private Date dateCreate;
    private Date dateUpdate;
    private boolean revoked;

    public PublicKeyWrapper() {
    }

    public PublicKeyWrapper(int id, int customerId, String publicKey, Date dateCreate, boolean revoked, Date dateUpdate) {
        this.id = id;
        this.customerId = customerId;
        this.publicKey = publicKey;
        this.dateCreate = dateCreate;
        this.revoked = revoked;
        this.dateUpdate = dateUpdate;
    }

    @Override
    public String toString() {
        return "PublicKeyWrapper{" +
                "id=" + id +
                ", customerId=" + customerId +
                ", publicKey='" + publicKey + '\'' +
                ", dateCreate=" + dateCreate +
                ", dateUpdate=" + dateUpdate +
                ", revoked=" + revoked +
                '}';
    }
//    private PublicKey publicKey;
//
//    // Lấy public key dưới dạng chuỗi Base64 UTF-8
//    public String getPublicKey() {
//        if (publicKey == null) return null;
//        byte[] keyBytes = publicKey.getEncoded();
//        return Base64.getEncoder().encodeToString(keyBytes);
//    }
//
//    // Đặt public key từ chuỗi Base64 UTF-8, trả về true nếu thành công
//    public boolean setPublicKey(String base64Key) {
//        try {
//            byte[] decodedKey = Base64.getDecoder().decode(base64Key);
//            X509EncodedKeySpec keySpec = new X509EncodedKeySpec(decodedKey);
//            KeyFactory keyFactory = KeyFactory.getInstance("RSA"); // hoặc "EC", "DSA" tuỳ loại
//            this.publicKey = keyFactory.generatePublic(keySpec);
//            return true;
//        } catch (Exception e) {
//            return false;
//        }
//    }
//
//    // Truy cập đối tượng PublicKey trực tiếp (nếu cần)
//    public PublicKey getPublicKeyObject() {
//        return this.publicKey;
//    }
}

