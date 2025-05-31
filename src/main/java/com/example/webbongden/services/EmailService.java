package com.example.webbongden.services;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import java.util.Properties;

public class EmailService {
    private static final String SMTP_HOST = "smtp.gmail.com"; // Thay bằng SMTP host của bạn
    private static final String SMTP_PORT = "587"; // Thường là 587 hoặc 465 cho TLS/SSL
    private static final String SMTP_USERNAME = "ryanz1292004@gmail.com"; // Email gửi đi
    private static final String SMTP_PASSWORD = "xfpg rywy kemf yfde"; // Mật khẩu email

    // Phương thức gửi email
    public static void sendEmail(String to, String subject, String body) throws MessagingException {
        // Cấu hình thuộc tính email
        Properties properties = new Properties();
        properties.put("mail.smtp.auth", "true");
        properties.put("mail.smtp.starttls.enable", "true");
        properties.put("mail.smtp.host", SMTP_HOST); // Đảm bảo SMTP_HOST được định nghĩa chính xác
        properties.put("mail.smtp.port", SMTP_PORT); // Đảm bảo SMTP_PORT được định nghĩa chính xác

        // Tạo session với thông tin xác thực
        Session session = Session.getInstance(properties, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(SMTP_USERNAME, SMTP_PASSWORD);
            }
        });

        try {
            // Tạo nội dung email
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(SMTP_USERNAME)); // Email người gửi
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(to)); // Email người nhận
            message.setSubject(subject); // Chủ đề email
            message.setText(body); // Nội dung email

            // Gửi email
            Transport.send(message);
            System.out.println("Email đã được gửi thành công đến: " + to);
        } catch (MessagingException e) {
            System.err.println("Có lỗi xảy ra khi gửi email: " + e.getMessage());
            throw e;
        }
    }

    //gửi email theo định dạng html
    public static void sendEmailHtml(String to, String subject, String htmlBody) throws MessagingException {
        // Cấu hình thuộc tính email
        Properties properties = new Properties();
        properties.put("mail.smtp.auth", "true");
        properties.put("mail.smtp.starttls.enable", "true");
        properties.put("mail.smtp.host", SMTP_HOST);
        properties.put("mail.smtp.port", SMTP_PORT);

        // Tạo session
        Session session = Session.getInstance(properties, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(SMTP_USERNAME, SMTP_PASSWORD);
            }
        });

        try {
            // Tạo message
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(SMTP_USERNAME));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(to));
            message.setSubject(subject);

            // Gửi nội dung HTML
            MimeBodyPart mimeBodyPart = new MimeBodyPart();
            mimeBodyPart.setContent(htmlBody, "text/html; charset=utf-8");

            Multipart multipart = new MimeMultipart();
            multipart.addBodyPart(mimeBodyPart);

            message.setContent(multipart); // Gắn nội dung HTML vào email

            Transport.send(message);
            System.out.println("Email đã được gửi thành công đến: " + to);
        } catch (MessagingException e) {
            System.err.println("Có lỗi xảy ra khi gửi email: " + e.getMessage());
            throw e;
        }
    }

}
