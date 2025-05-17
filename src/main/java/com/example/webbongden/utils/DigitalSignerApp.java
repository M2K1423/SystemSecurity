package com.example.webbongden.utils;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.security.*;
import java.security.cert.X509Certificate;
import java.util.Base64;

public class DigitalSignerApp extends JFrame {
    private JTextField fileField, keystoreField;
    private JTextArea outputArea;
    private File inputFile, keystoreFile;

    public DigitalSignerApp() {
        setTitle("Ứng dụng ký file đơn hàng (.txt)");
        setSize(600, 400);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        initUI();
    }

    private void initUI() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        JPanel top = new JPanel(new GridLayout(3, 1, 5, 5));

        // Chọn file đơn hàng
        JButton chooseFile = new JButton("📄 Chọn file hóa đơn (.txt)");
        fileField = new JTextField(); fileField.setEditable(false);
        chooseFile.addActionListener(e -> {
            JFileChooser chooser = new JFileChooser();
            int result = chooser.showOpenDialog(this);
            if (result == JFileChooser.APPROVE_OPTION) {
                inputFile = chooser.getSelectedFile();
                fileField.setText(inputFile.getAbsolutePath());
            }
        });

        // Chọn keystore
        JButton chooseKeystore = new JButton("🔐 Chọn keystore (.p12)");
        keystoreField = new JTextField(); keystoreField.setEditable(false);
        chooseKeystore.addActionListener(e -> {
            JFileChooser chooser = new JFileChooser();
            int result = chooser.showOpenDialog(this);
            if (result == JFileChooser.APPROVE_OPTION) {
                keystoreFile = chooser.getSelectedFile();
                keystoreField.setText(keystoreFile.getAbsolutePath());
            }
        });

        top.add(wrapRow(chooseFile, fileField));
        top.add(wrapRow(chooseKeystore, keystoreField));

        // Ký file
        JButton signButton = new JButton("✅ Ký và xuất file .sig");
        signButton.addActionListener(e -> signFile());

        outputArea = new JTextArea(10, 50);
        outputArea.setEditable(false);

        panel.add(top, BorderLayout.NORTH);
        panel.add(signButton, BorderLayout.CENTER);
        panel.add(new JScrollPane(outputArea), BorderLayout.SOUTH);

        add(panel);
    }

    private JPanel wrapRow(JButton button, JTextField field) {
        JPanel row = new JPanel(new BorderLayout(5, 5));
        row.add(button, BorderLayout.WEST);
        row.add(field, BorderLayout.CENTER);
        return row;
    }

    private void signFile() {
        try {
            if (inputFile == null || keystoreFile == null) {
                JOptionPane.showMessageDialog(this, "Vui lòng chọn file và keystore.");
                return;
            }

            String password = JOptionPane.showInputDialog(this, "Nhập mật khẩu keystore:");
            if (password == null || password.isEmpty()) return;

            // ✅ Đọc nội dung file rawData đơn hàng (ví dụ: "46phat1558000.002025-05-15")
            String rawData = Files.readString(inputFile.toPath(), StandardCharsets.UTF_8).trim();
            byte[] data = rawData.getBytes(StandardCharsets.UTF_8); // 👈 Ký rawData, không phải toàn bộ file

            // ✅ Load keystore
            KeyStore ks = KeyStore.getInstance("PKCS12");
            try (InputStream is = new FileInputStream(keystoreFile)) {
                ks.load(is, password.toCharArray());
            }

            String alias = ks.aliases().nextElement();
            PrivateKey privateKey = (PrivateKey) ks.getKey(alias, password.toCharArray());

            // ✅ Ký rawData
            Signature signature = Signature.getInstance("SHA256withRSA");
            signature.initSign(privateKey);
            signature.update(data);
            byte[] signed = signature.sign();
            String signatureBase64 = Base64.getEncoder().encodeToString(signed);

            // ✅ Ghi chữ ký vào file .sig
            File sigFile = new File(inputFile.getParent(), inputFile.getName() + ".sig");
            try (FileWriter fw = new FileWriter(sigFile)) {
                fw.write(signatureBase64);
            }

            // ✅ Mở thư mục chứa file .sig
            Desktop.getDesktop().open(sigFile.getParentFile());
            outputArea.setText("✅ Đã ký và lưu file:\n" + sigFile.getAbsolutePath());

        } catch (Exception ex) {
            ex.printStackTrace();
            outputArea.setText("❌ Lỗi khi ký file: " + ex.getMessage());
        }
    }



    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new DigitalSignerApp().setVisible(true));
    }
}
