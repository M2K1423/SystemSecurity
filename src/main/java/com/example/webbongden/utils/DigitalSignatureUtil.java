package com.example.webbongden.utils;

import java.io.File;

public class DigitalSignatureUtil {

    // Đường dẫn gốc lưu trữ hóa đơn và file .sig
    private static final String INVOICE_BASE_PATH = "D:\\Order";

    /**
     * Kiểm tra xem hóa đơn đã được ký số chưa.
     *
     * @param orderId ID của đơn hàng
     * @return true nếu hóa đơn đã được ký, false nếu chưa
     */
    public static boolean isInvoiceSigned(String orderId) {
        // Tạo đường dẫn file hóa đơn
        String invoicePath = INVOICE_BASE_PATH + "invoice_" + orderId + ".txt";
        String sigPath = invoicePath + ".sig";

        // Kiểm tra sự tồn tại của cả file hóa đơn và file .sig
        File invoiceFile = new File(invoicePath);
        File sigFile = new File(sigPath);

        return invoiceFile.exists() && sigFile.exists();
    }

    /**
     * Kiểm tra tính hợp lệ của chữ ký (có thể mở rộng sau này).
     *
     * @param orderId ID của đơn hàng
     * @return true nếu chữ ký hợp lệ, false nếu không
     */
    public static boolean verifySignature(String orderId) {
        // TODO: Viết logic verify chữ ký thật/sai ở đây
        return false; // Mặc định trả về false
    }
    public static void main(String[] args) {
        // Test với đơn hàng 1001 (có .sig)
        System.out.println("Đơn hàng 40:");
        System.out.println("isInvoiceSigned: " + DigitalSignatureUtil.isInvoiceSigned("40"));
        System.out.println("verifySignature: " + DigitalSignatureUtil.verifySignature("40"));

        // Test với đơn hàng 1002 (không có .sig)
        System.out.println("\nĐơn hàng 41:");
        System.out.println("41: " + DigitalSignatureUtil.isInvoiceSigned("41"));
        System.out.println("verifySignature: " + DigitalSignatureUtil.verifySignature("1002"));

        // Test với đơn hàng không tồn tại
        System.out.println("\nĐơn hàng 42:");
        System.out.println("isInvoiceSigned: " + DigitalSignatureUtil.isInvoiceSigned("42"));
        System.out.println("verifySignature: " + DigitalSignatureUtil.verifySignature("42"));
    }
}