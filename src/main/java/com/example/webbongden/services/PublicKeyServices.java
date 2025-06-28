package com.example.webbongden.services;

import com.example.webbongden.dao.OrderDao;
import com.example.webbongden.dao.UserDao;

import java.security.GeneralSecurityException;
import java.security.KeyFactory;
import java.security.PublicKey;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

public class PublicKeyServices {
    public UserDao userDao;


    public PublicKeyServices() {
        this.userDao = new UserDao();
    }

    public boolean addPublicKey(int id, String encodedPublicKey) throws GeneralSecurityException, IllegalArgumentException {
        byte[] decodedKey = Base64.getDecoder().decode(encodedPublicKey);
        X509EncodedKeySpec keySpec = new X509EncodedKeySpec(decodedKey);
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        PublicKey publicKey = keyFactory.generatePublic(keySpec); // Sẽ ném lỗi nếu sai định dạng
        return userDao.addPublicKey(id, encodedPublicKey);
    }

    public boolean updatePublicKey(int id){
        return userDao.updatePublicKey(id);
    }

    public com.example.webbongden.dao.model.PublicKey getPublicKey(int customerId){
        return userDao.getPublicKey(customerId);
    }

    public String getPublicKeyString(int orderId){
        OrderDao orderDao = new OrderDao();
        return orderDao.getPublicKeyByOrderId(orderId);
    }

}
