package com.xcally.ars.domain.common;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Base64;

@Component
public class EncryptionService {
	
	
	@Value("${secret.key}")
    private String secretKey;

	//암호화
    public String encrypt(String data) throws Exception {
        if (data == null || data.isEmpty()) {
            return ""; // 빈 문자열 반환
        }

        SecretKeySpec secretKeySpec = new SecretKeySpec(secretKey.getBytes(), "AES");
        Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
        cipher.init(Cipher.ENCRYPT_MODE, secretKeySpec);
        byte[] encryptedBytes = cipher.doFinal(data.getBytes());
        return Base64.getEncoder().encodeToString(encryptedBytes);
    }

    //복호화
    public String decrypt(String encryptedData) throws Exception {
        if (encryptedData == null || encryptedData.isEmpty()) {
            return ""; // 빈 문자열 반환
        }

        SecretKeySpec secretKeySpec = new SecretKeySpec(secretKey.getBytes(), "AES");
        Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
        cipher.init(Cipher.DECRYPT_MODE, secretKeySpec);

        byte[] decryptedBytes;
        try {
            decryptedBytes = cipher.doFinal(Base64.getDecoder().decode(encryptedData));
        } catch (Exception e) {
            return ""; // 복호화에 실패한 경우 빈 문자열 반환
        }
        
        return new String(decryptedBytes);
    }
}
