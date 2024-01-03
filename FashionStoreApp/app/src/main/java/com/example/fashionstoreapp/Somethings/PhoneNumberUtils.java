package com.example.fashionstoreapp.Somethings;

public class PhoneNumberUtils {
    public static String maskPhoneNumber(String phoneNumber) {
        // Che dấu 4 số cuối của số điện thoại
        int length = phoneNumber.length();
        int endIndex = length - 4;
        StringBuilder maskedNumber = new StringBuilder();
        for (int i = 0; i < length; i++) {
            if (i < endIndex) {
                // Hiển thị ký tự che dấu (ví dụ: "*")
                maskedNumber.append("*");
            } else {
                // Hiển thị số điện thoại thực tế
                maskedNumber.append(phoneNumber.charAt(i));
            }
        }
        return maskedNumber.toString();
    }
    public static String maskEmail(String email) {
        int length = email.length();
        int endIndex = length - 12;
        StringBuilder maskedNumber = new StringBuilder();
        for (int i = 0; i < length; i++) {
            if (i < endIndex) {
                maskedNumber.append("*");
            } else {
                maskedNumber.append(email.charAt(i));
            }
        }
        return maskedNumber.toString();
    }
}
