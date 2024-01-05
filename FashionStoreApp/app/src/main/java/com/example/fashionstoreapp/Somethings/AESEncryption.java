package com.example.fashionstoreapp.Somethings;

import android.util.Base64;

import org.bouncycastle.crypto.BufferedBlockCipher;
import org.bouncycastle.crypto.CipherParameters;
import org.bouncycastle.crypto.engines.AESEngine;
import org.bouncycastle.crypto.modes.CFBBlockCipher;
import org.bouncycastle.crypto.params.KeyParameter;
import org.bouncycastle.crypto.params.ParametersWithIV;

import java.security.MessageDigest;
import java.util.Arrays;

public class AESEncryption {
    public static String encrypt(String username, String keyword) {
        try {
            byte[] passwordHash = sha256(keyword);
            byte[] iv = Arrays.copyOfRange(passwordHash, 0, 16);
            BufferedBlockCipher cipher = new BufferedBlockCipher(new CFBBlockCipher(new AESEngine(), 128));
            CipherParameters ivAndKey = new ParametersWithIV(new KeyParameter(passwordHash), iv);

            cipher.init(true, ivAndKey);

            byte[] inputBytes = username.getBytes("UTF-8");
            byte[] outputBytes = new byte[cipher.getOutputSize(inputBytes.length)];

            int processedBytes = cipher.processBytes(inputBytes, 0, inputBytes.length, outputBytes, 0);
            processedBytes += cipher.doFinal(outputBytes, processedBytes);

            // Chuyển đổi dữ liệu mã hóa sang chuỗi kí tự sử dụng Base64
            return Base64.encodeToString(outputBytes, Base64.DEFAULT);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static String decrypt(String encryptedUsername, String keyword) {
        try {
            byte[] passwordHash = sha256(keyword);
            byte[] iv = Arrays.copyOfRange(passwordHash, 0, 16);

            BufferedBlockCipher cipher = new BufferedBlockCipher(new CFBBlockCipher(new AESEngine(), 128));
            CipherParameters ivAndKey = new ParametersWithIV(new KeyParameter(passwordHash), iv);

            cipher.init(false, ivAndKey);

            // Chuyển đổi chuỗi kí tự Base64 sang dữ liệu mã hóa
            byte[] inputBytes = Base64.decode(encryptedUsername, Base64.DEFAULT);
            byte[] outputBytes = new byte[cipher.getOutputSize(inputBytes.length)];

            int processedBytes = cipher.processBytes(inputBytes, 0, inputBytes.length, outputBytes, 0);
            processedBytes += cipher.doFinal(outputBytes, processedBytes);

            return new String(outputBytes, "UTF-8");
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private static byte[] sha256(String input) throws Exception {
        MessageDigest digest = MessageDigest.getInstance("SHA-256");
        return digest.digest(input.getBytes("UTF-8"));
    }

}
