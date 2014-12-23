package com.mrlolethan.stegstore.steganography;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.nio.file.Files;

import javax.swing.JOptionPane;

import org.apache.commons.codec.binary.Base64;

import com.mrlolethan.stegstore.Boot;
import com.mrlolethan.stegstore.cryptography.Cryptography;


public class Steganography {
    
    public static void hideDataInFile(File FileFile, File outFile, String data, String key, byte[] iv) {
        byte[] bytes = readFileBytes(FileFile);
        
        data = Boot.IV_TAG + Cryptography.encryptAES(Base64.encodeBase64String(iv), key, Boot.IV_AES_IV) + Boot.IV_TAG + Boot.DATA_TAG + Cryptography.encryptAES(data, key, iv) + Boot.DATA_TAG;
        byte[] dataBytes = data.getBytes();
        
        byte[] endBytes = new byte[bytes.length + dataBytes.length];
        for(int i = 0; i < bytes.length; i++) endBytes[i] = bytes[i];
        for(int i = 0; i < dataBytes.length; i++) endBytes[i + bytes.length] = dataBytes[i];
        
        writeFileBytes(outFile, endBytes);
    }
    
    public static void hideDataInFile(byte[] bytes, File outFile, String data, String key, byte[] iv) {
        data = Boot.IV_TAG + Cryptography.encryptAES(Base64.encodeBase64String(iv), key, Boot.IV_AES_IV) + Boot.IV_TAG + Boot.DATA_TAG + Cryptography.encryptAES(data, key, iv) + Boot.DATA_TAG;
        byte[] dataBytes = data.getBytes();
        
        byte[] endBytes = new byte[bytes.length + dataBytes.length];
        for(int i = 0; i < bytes.length; i++) endBytes[i] = bytes[i];
        for(int i = 0; i < dataBytes.length; i++) endBytes[i + bytes.length] = dataBytes[i];
        
        writeFileBytes(outFile, endBytes);
    }
    
    
    public static String readFileAsString(File file) {
        try(FileInputStream in = new FileInputStream(file)) {
            byte[] FileData = new byte[(int) file.length()];
            in.read(FileData);
            
            String FileDataString = Base64.encodeBase64URLSafeString(FileData);
            return new String(Base64.decodeBase64(FileDataString));
        } catch(Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
        return null;
    }
    
    
    public static void writeFileBytes(File file, byte[] bytes) {
        try(BufferedOutputStream out = new BufferedOutputStream(new FileOutputStream(file))) {
            out.write(bytes);
            out.flush();
        } catch(Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    public static void writeFileBytes(File file, Byte[] Bytes) {
        byte[] bytes = new byte[Bytes.length];
        int i = 0;
        for(Byte b : Bytes)
            bytes[i++] = b;
        writeFileBytes(file, bytes);
    }
    
    public static byte[] readFileBytes(File file) {
        try {
            return Files.readAllBytes(file.toPath());
        } catch(Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
        return null;
    }
    
    public static String getFileExtension(String fileName) {
        return fileName.split("\\.")[fileName.split("\\.").length - 1];
    }
    
    
}
