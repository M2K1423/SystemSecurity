package com.example.webbongden.utils;

import javax.swing.*;
import java.awt.*;
import java.awt.datatransfer.*;
import java.awt.dnd.DnDConstants;
import java.awt.dnd.DropTarget;
import java.awt.dnd.DropTargetDropEvent;
import java.awt.event.*;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.security.*;
import java.security.cert.Certificate;
import java.security.cert.X509Certificate;
import java.text.SimpleDateFormat;
import java.util.Base64;
import java.util.Enumeration;

public class DigitalSignerApp extends JFrame {
    private JTextField fileField, keystoreField, sigPathField;
    private JTextArea outputArea;
    private File inputFile, keystoreFile;
    private JLabel certInfoLabel;

    public DigitalSignerApp() {
        // Bật antialiasing cho chữ rõ nét
        System.setProperty("awt.useSystemAAFontSettings", "on");
        System.setProperty("swing.aatext", "true");

        setTitle("Ứng dụng Ký Số Hóa Đơn (.txt)");
        setSize(800, 600);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setIconImage(new ImageIcon("icon.png").getImage());
        initUI();
    }

    private void initUI() {
        try {
            // Dùng FlatLaf theme hiện đại (nếu có)
//            UIManager.setLookAndFeel(new com.formdev.flatlaf.FlatIntelliJLaf());
        } catch (Exception ex) {
            // Không bắt buộc
        }

        Font mainFont = new Font("Segoe UI", Font.PLAIN, 14);
        Color bgColor = new Color(245, 247, 250);
        Color accentColor = new Color(30, 144, 255);
        Color successColor = new Color(46, 204, 113);

        JPanel panel = new JPanel(new BorderLayout(20, 20));
        panel.setBackground(bgColor);
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Panel chọn file
        JPanel topPanel = new JPanel();
        topPanel.setLayout(new BoxLayout(topPanel, BoxLayout.Y_AXIS));
        topPanel.setBackground(bgColor);

        // Chọn file hóa đơn
        JButton chooseFileBtn = createStyledButton("📄 Chọn file hóa đơn (.txt)", accentColor);
        fileField = new JTextField(); fileField.setEditable(true); fileField.setFont(mainFont); fileField.setBackground(Color.WHITE);
        chooseFileBtn.addActionListener(e -> chooseFile());

        // Drag & Drop cho file hóa đơn
        addDragAndDrop(fileField, false);

        // Chọn keystore
        JButton chooseKeystoreBtn = createStyledButton("🔐 Chọn keystore (.p12)", accentColor);
        keystoreField = new JTextField(); keystoreField.setEditable(true); keystoreField.setFont(mainFont); keystoreField.setBackground(Color.WHITE);
        chooseKeystoreBtn.addActionListener(e -> chooseKeystore());

        // Drag & Drop cho keystore
        addDragAndDrop(keystoreField, true);

        // Hiển thị đường dẫn .sig
        sigPathField = new JTextField(); sigPathField.setEditable(true); sigPathField.setFont(mainFont.deriveFont(Font.ITALIC)); sigPathField.setBackground(bgColor);
        sigPathField.setBorder(BorderFactory.createTitledBorder("Đường dẫn file .sig"));

        // Thông tin chứng thư số
        certInfoLabel = new JLabel("🔸 Chưa có keystore được chọn");
        certInfoLabel.setFont(mainFont.deriveFont(Font.ITALIC));
        certInfoLabel.setForeground(new Color(80, 80, 80));

        // Nút hành động
        JButton signBtn = createStyledButton("✅ Ký và xuất file .sig", successColor);
        JButton clearBtn = createStyledButton("🗑 Xóa tất cả", Color.RED);
        signBtn.addActionListener(e -> signFile());
        clearBtn.addActionListener(e -> clearAll());

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        buttonPanel.setBackground(bgColor);
        buttonPanel.add(signBtn);
        buttonPanel.add(clearBtn);

        // Kết quả đầu ra
        outputArea = new JTextArea(6, 40);
        outputArea.setEditable(false);
        outputArea.setFont(mainFont);
        outputArea.setForeground(new Color(50, 50, 50));
        outputArea.setLineWrap(true);
        outputArea.setWrapStyleWord(true);
        JScrollPane scrollPane = new JScrollPane(outputArea);
        scrollPane.setBorder(BorderFactory.createTitledBorder("Kết quả"));

        // Thêm thành phần vào giao diện
        topPanel.add(wrapRowWithLabel(chooseFileBtn, fileField));
        topPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        topPanel.add(wrapRowWithLabel(chooseKeystoreBtn, keystoreField));
        topPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        topPanel.add(sigPathField);
        topPanel.add(certInfoLabel);

        panel.add(topPanel, BorderLayout.NORTH);
        panel.add(buttonPanel, BorderLayout.CENTER);
        panel.add(scrollPane, BorderLayout.SOUTH);

        add(panel);
        setVisible(true);
    }

    private JButton createStyledButton(String text, Color color) {
        JButton button = new JButton(text);
        button.setFont(new Font("Segoe UI", Font.BOLD, 14));
        button.setBackground(color);
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createEmptyBorder(10, 15, 10, 15));
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        return button;
    }

    private JPanel wrapRowWithLabel(JButton button, JComponent field) {
        JPanel row = new JPanel(new BorderLayout(10, 10));
        row.setBackground(getBackground());
        button.setPreferredSize(new Dimension(220, 40));
        row.add(button, BorderLayout.WEST);
        row.add(field, BorderLayout.CENTER);
        return row;
    }

    private void chooseFile() {
        JFileChooser fc = new JFileChooser();
        fc.setFileFilter(new javax.swing.filechooser.FileFilter() {
            public boolean accept(File f) {
                return f.isDirectory() || f.getName().toLowerCase().endsWith(".txt");
            }
            public String getDescription() {
                return "Text Files (*.txt)";
            }
        });

        if (fc.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
            inputFile = fc.getSelectedFile();
            fileField.setText(inputFile.getAbsolutePath());
            updateSigPath();
        }
    }

    private void chooseKeystore() {
        JFileChooser fc = new JFileChooser();
        fc.setFileFilter(new javax.swing.filechooser.FileFilter() {
            public boolean accept(File f) {
                return f.isDirectory() || f.getName().toLowerCase().endsWith(".p12");
            }
            public String getDescription() {
                return "PKCS#12 Files (*.p12)";
            }
        });

        if (fc.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
            keystoreFile = fc.getSelectedFile();
            keystoreField.setText(keystoreFile.getAbsolutePath());
            showCertInfo();
        }
    }

    private void updateSigPath() {
        if (inputFile != null) {
            File sigFile = new File(inputFile.getParent(), inputFile.getName() + ".sig");
            sigPathField.setText(sigFile.getAbsolutePath());
        } else {
            sigPathField.setText("");
        }
    }

    private void showCertInfo() {
        try {


            KeyStore ks = KeyStore.getInstance("PKCS12");
            try (InputStream is = new FileInputStream(keystoreFile)) {



            Enumeration<String> aliases = ks.aliases();
            while (aliases.hasMoreElements()) {
                String alias = aliases.nextElement();
                Certificate cert = ks.getCertificate(alias);
                if (cert instanceof X509Certificate) {
                    X509Certificate x509 = (X509Certificate) cert;
                    SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
                    certInfoLabel.setText(String.format("🔹 CN: %s | Từ: %s đến %s",
                            x509.getSubjectDN().getName(),
                            sdf.format(x509.getNotBefore()),
                            sdf.format(x509.getNotAfter())));
                    break;  }
                }
            }
        } catch (Exception ex) {
            certInfoLabel.setText("🔸 Không đọc được thông tin chứng thư");
        }
    }

    private void signFile() {
        try {
            if (inputFile == null || keystoreFile == null) {
                JOptionPane.showMessageDialog(this, "Vui lòng chọn file và keystore.");
                return;
            }

            if (!inputFile.getName().endsWith(".txt")) {
                JOptionPane.showMessageDialog(this, "Chỉ hỗ trợ ký file .txt!");
                return;
            }

            String password = JOptionPane.showInputDialog(this, "Nhập mật khẩu keystore:");
            if (password == null || password.isEmpty()) return;

            String rawData = Files.readString(inputFile.toPath(), StandardCharsets.UTF_8).trim();
            byte[] data = rawData.getBytes(StandardCharsets.UTF_8);

            KeyStore ks = KeyStore.getInstance("PKCS12");
            try (InputStream is = new FileInputStream(keystoreFile)) {
                ks.load(is, password.toCharArray());
            }

            String alias = ks.aliases().nextElement();
            PrivateKey privateKey = (PrivateKey) ks.getKey(alias, password.toCharArray());

            Signature signature = Signature.getInstance("SHA256withRSA");
            signature.initSign(privateKey);
            signature.update(data);
            byte[] signed = signature.sign();
            String signatureBase64 = Base64.getEncoder().encodeToString(signed);

            File sigFile = new File(inputFile.getParent(), inputFile.getName() + ".sig");
            try (FileWriter fw = new FileWriter(sigFile)) {
                fw.write(signatureBase64);
            }

            Desktop.getDesktop().open(sigFile.getParentFile());
            outputArea.setText("✅ Đã ký và lưu file:\n" + sigFile.getAbsolutePath());

        } catch (Exception ex) {
            ex.printStackTrace();
            outputArea.setText("❌ Lỗi khi ký file: " + ex.getMessage());
        }
    }

    private void clearAll() {
        inputFile = null;
        keystoreFile = null;
        fileField.setText("");
        keystoreField.setText("");
        sigPathField.setText("");
        certInfoLabel.setText("🔸 Chưa có keystore được chọn");
        outputArea.setText("");
    }

    private void addDragAndDrop(JTextField field, boolean isKeystore) {
        field.setDropTarget(new DropTarget() {
            public synchronized void drop(DropTargetDropEvent evt) {
                try {
                    evt.acceptDrop(DnDConstants.ACTION_COPY);
                    java.util.List<File> files = (java.util.List<File>) evt.getTransferable()
                            .getTransferData(DataFlavor.javaFileListFlavor);
                    if (!files.isEmpty()) {
                        File selectedFile = files.get(0);
                        if (isKeystore && selectedFile.getName().toLowerCase().endsWith(".p12")) {
                            keystoreFile = selectedFile;
                            keystoreField.setText(selectedFile.getAbsolutePath());
                            showCertInfo();
                        } else if (!isKeystore && selectedFile.getName().toLowerCase().endsWith(".txt")) {
                            inputFile = selectedFile;
                            fileField.setText(selectedFile.getAbsolutePath());
                            updateSigPath();
                        } else {
                            JOptionPane.showMessageDialog(DigitalSignerApp.this,
                                    "Định dạng file không hợp lệ.");
                        }
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        });
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new DigitalSignerApp());
    }
}