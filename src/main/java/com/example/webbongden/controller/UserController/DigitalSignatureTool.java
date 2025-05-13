package com.example.webbongden.controller.UserController;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.*;
import java.nio.file.Files;
import java.security.*;
import java.security.cert.X509Certificate;
import java.util.Base64;

public class DigitalSignatureTool extends JFrame {
    private File invoiceFile;
    private File p12File;
    private JTextField passwordField;

    public DigitalSignatureTool() {
        setTitle("Công cụ ký hóa đơn bằng .p12");
        setSize(500, 250);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new GridLayout(5, 1));

        JButton chooseInvoiceBtn = new JButton("1. Chọn file hóa đơn");
        JButton chooseP12Btn = new JButton("2. Chọn file .p12");
        passwordField = new JTextField();
        JButton signBtn = new JButton("3. Ký và lưu chữ ký");
        JLabel statusLabel = new JLabel("Trạng thái: Chưa làm gì", SwingConstants.CENTER);

        add(chooseInvoiceBtn);
        add(chooseP12Btn);
        add(new JLabel("3. Nhập mật khẩu .p12:", SwingConstants.CENTER));
        add(passwordField);
        add(signBtn);
        add(statusLabel);

        chooseInvoiceBtn.addActionListener((ActionEvent e) -> {
            invoiceFile = chooseFile("Chọn hóa đơn (.txt)");
            statusLabel.setText("✔ File hóa đơn: " + invoiceFile.getName());
        });

        chooseP12Btn.addActionListener((ActionEvent e) -> {
            p12File = chooseFile("Chọn file .p12");
            statusLabel.setText("✔ File .p12: " + p12File.getName());
        });

        signBtn.addActionListener((ActionEvent e) -> {
            if (invoiceFile == null || p12File == null || passwordField.getText().isEmpty()) {
                JOptionPane.showMessageDialog(this, "Vui lòng chọn đủ file và nhập mật khẩu!", "Lỗi", JOptionPane.ERROR_MESSAGE);
                return;
            }
            try {
                String signature = signFile(invoiceFile, p12File, passwordField.getText());
                File sigFile = new File(invoiceFile.getParent(), "signature.txt");
                Files.write(sigFile.toPath(), signature.getBytes());
                statusLabel.setText("✅ Đã ký. File: " + sigFile.getAbsolutePath());
            } catch (Exception ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(this, "Lỗi khi ký: " + ex.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
            }
        });

        setVisible(true);
    }

    private File chooseFile(String title) {
        JFileChooser chooser = new JFileChooser();
        chooser.setDialogTitle(title);
        int result = chooser.showOpenDialog(this);
        return (result == JFileChooser.APPROVE_OPTION) ? chooser.getSelectedFile() : null;
    }

    private String signFile(File inputFile, File p12File, String password) throws Exception {
        byte[] data = Files.readAllBytes(inputFile.toPath());

        KeyStore ks = KeyStore.getInstance("PKCS12");
        ks.load(new FileInputStream(p12File), password.toCharArray());

        String alias = ks.aliases().nextElement();
        PrivateKey privateKey = (PrivateKey) ks.getKey(alias, password.toCharArray());
        X509Certificate cert = (X509Certificate) ks.getCertificate(alias);

        Signature signature = Signature.getInstance("SHA256withRSA");
        signature.initSign(privateKey);
        signature.update(data);

        byte[] signedBytes = signature.sign();
        return Base64.getEncoder().encodeToString(signedBytes);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(DigitalSignatureTool::new);
    }
}

