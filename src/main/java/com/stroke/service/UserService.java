package com.stroke.service;

import com.stroke.model.User;
import com.stroke.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.util.Base64;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @Value("${aes.key}")
    private String aesKey;

    public User findByUsername(String username) {
        return userRepository.findByUsername(username).orElse(null);
    }

    public boolean validateLogin(String username, String encryptedPassword) {
        try {
            String decryptedPassword = decrypt(encryptedPassword);
            User user = findByUsername(username);
            if (user == null) {
                System.out.println("User not found: " + username);
                return false;
            }
            boolean isValid = passwordEncoder.matches(decryptedPassword, user.getPassword());
            System.out.println("Password validation result: " + isValid);
            return isValid;
        } catch (Exception e) {
            System.out.println("Password decryption failed: " + e.getMessage());
            return false;
        }
    }

    public void registerUser(String username, String encryptedPassword) throws Exception {
        if (findByUsername(username) != null) {
            throw new IllegalArgumentException("用户名已存在");
        }
        String decryptedPassword = decrypt(encryptedPassword);
        User user = new User();
        user.setUsername(username);
        user.setPassword(passwordEncoder.encode(decryptedPassword));
        userRepository.save(user);
    }

    private String decrypt(String encryptedPassword) throws Exception {
        byte[] ivAndCiphertext = Base64.getDecoder().decode(encryptedPassword);
        // 前 16 字节为 IV
        byte[] iv = new byte[16];
        byte[] ciphertext = new byte[ivAndCiphertext.length - 16];
        System.arraycopy(ivAndCiphertext, 0, iv, 0, 16);
        System.arraycopy(ivAndCiphertext, 16, ciphertext, 0, ivAndCiphertext.length - 16);

        SecretKeySpec key = new SecretKeySpec(aesKey.getBytes("UTF-8"), "AES");
        IvParameterSpec ivSpec = new IvParameterSpec(iv);
        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        cipher.init(Cipher.DECRYPT_MODE, key, ivSpec);
        byte[] decrypted = cipher.doFinal(ciphertext);
        return new String(decrypted, "UTF-8");
    }
}