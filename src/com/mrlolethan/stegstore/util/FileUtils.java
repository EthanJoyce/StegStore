package com.mrlolethan.stegstore.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.InputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

public enum FileUtils {;
    
	public static String readFile(File file) {
	    try(BufferedReader in = new BufferedReader(new FileReader(file));) {
	        StringBuffer buf = new StringBuffer();
	        
	        String line;
	        while((line = in.readLine()) != null) {
	            buf.append(line + "\r\n");
	        }
	        
	        return buf.toString();
	    } catch(Exception ex) { ex.printStackTrace(); }
	    return null;
	}
	
	
	public static byte[] readFileBytes(File file) {
	    try(FileInputStream in = new FileInputStream(file);) {
	        byte[] bytes = new byte[(int) file.length()];
	        
	        in.read(bytes);
	        
	        return bytes;
	    } catch(Exception ex) { ex.printStackTrace(); }
	    return null;
	}
	
	
	public static void zipFile(File inFile, File outFile) {
	    try(FileInputStream in = new FileInputStream(inFile); ZipOutputStream out = new ZipOutputStream(new FileOutputStream(outFile));) {
	        out.putNextEntry(new ZipEntry(inFile.getName()));
	        
	        byte[] b = new byte[1024];
	        int count;
	        
	        while((count = in.read(b)) > 0) {
	            out.write(b, 0, count);
	        }
	    } catch(Exception ex) { ex.printStackTrace(); }
	}
	
	
	public static void unzipFileInputStream(InputStream in, File outFolder) {
	    byte[] buffer = new byte[1024];
	    try(ZipInputStream zis = new ZipInputStream(in);) {
	        if(!outFolder.exists()) outFolder.mkdirs();
	        
	        ZipEntry ze = zis.getNextEntry();
	        while(ze != null) {
	            File newFile = new File(outFolder.getAbsolutePath() + File.separator + ze.getName());
	            
	            new File(newFile.getParent()).mkdirs();
	            
	            try(FileOutputStream fos = new FileOutputStream(newFile)) {
	                int len;
	                while((len = zis.read(buffer)) > 0) {
	                    fos.write(buffer, 0, len);
	                }
	            }
	            
	            ze = zis.getNextEntry();
	        }
	    } catch(Exception ex) { ex.printStackTrace(); }
	}
	
	public static void unzipFile(File inFile, File outFolder) {
	    try {
	    	unzipFileInputStream(new FileInputStream(inFile), outFolder);
	    } catch(FileNotFoundException ex) {
	    	ex.printStackTrace();
	    }
	}
	
	
}
