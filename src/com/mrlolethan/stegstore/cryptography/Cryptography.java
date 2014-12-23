package com.mrlolethan.stegstore.cryptography;

import java.security.MessageDigest;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import javax.swing.JOptionPane;

import org.apache.commons.codec.binary.Base64;

import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;




public class Cryptography {
    
    public static String getMD5(String str) {
        String out = "";
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(str.getBytes(), 0, str.length());
            byte[] hash = md.digest();
            
            StringBuilder sb = new StringBuilder(hash.length * 2);
            for(byte b : hash) {
                sb.append(String.format("%02x", b&0xff));
            }
            
            out = sb.toString();
        } catch(Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
        return out;
    }
    
    public static String getSHA512(String str) {
        String out = "";
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-512");
            md.update(str.getBytes(), 0, str.length());
            byte[] hash = md.digest();
            
            StringBuilder sb = new StringBuilder(hash.length * 2);
            for(byte b : hash) {
                sb.append(String.format("%02x", b&0xff));
            }
            
            out = sb.toString();
        } catch(Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
        return out;
    }
    
    
    public static String encodeBASE64(String str) {
        try {
            BASE64Encoder enc = new BASE64Encoder();
            String encStr = new String(enc.encodeBuffer(str.getBytes()));
            
            return encStr;
        } catch(Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
        return null;
    }
    
    public static String decodeBASE64(String str) {
       try {
           BASE64Decoder dec = new BASE64Decoder();
           String decStr = new String(dec.decodeBuffer(str));
           
           return decStr;
       } catch(Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
       return null;
    }
    
    public static String encryptAES(String str, String key, byte[] IV) {
       try {
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            SecretKeySpec secretKey = new SecretKeySpec(getMD5(key).getBytes(), "AES");
            cipher.init(Cipher.ENCRYPT_MODE, secretKey, new IvParameterSpec(IV));
            String encryptedString = new String(Base64.encodeBase64(cipher.doFinal(str.getBytes())));
            return encryptedString;
       } catch(Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
       return null;
    }
    
    public static String decryptAES(String str, String key, byte[] IV) {
        try {
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            SecretKeySpec secretKey = new SecretKeySpec(getMD5(key).getBytes(), "AES");
            cipher.init(Cipher.DECRYPT_MODE, secretKey, new IvParameterSpec(IV));
            String decryptedString = new String(cipher.doFinal(Base64.decodeBase64(str.getBytes())));
            return decryptedString;
        } catch(Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
        return null;
    }
    
    public static String decryptAESwithExceptions(String str, String key, byte[] IV) throws Exception {
        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        SecretKeySpec secretKey = new SecretKeySpec(getMD5(key).getBytes(), "AES");
        cipher.init(Cipher.DECRYPT_MODE, secretKey, new IvParameterSpec(IV));
        String decryptedString = new String(cipher.doFinal(Base64.decodeBase64(str.getBytes())));
        return decryptedString;
    }
    
    
}
