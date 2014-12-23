package com.mrlolethan.stegstore;

import java.io.File;

import javax.crypto.Cipher;
import javax.swing.JOptionPane;

import com.mrlolethan.stegstore.gui.MainFrame;
import com.mrlolethan.stegstore.util.FileUtils;

public class Boot {
    
    public static MainFrame mainFrame;
    
    
    public static final String NAME = "StegStore";
    public static final String VERSION = "3.0";
    
    public static final String DATA_TAG = "[StegStore-Data]";
    public static final String IV_TAG = "[StegStore-IV]";
    
    
    public static final byte[] IV_AES_IV = new byte[] { (byte) 0x27, (byte) 0x01, (byte) 0x00, (byte) 0xBC, (byte) 0x00, (byte) 0xAA, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x0F, (byte) 0x07, (byte) 0x3C, (byte) 0x00, (byte) 0x0B, (byte) 0xA0, (byte) 0xFF,  };
    
    
    private static final String jreSecurityFilePath = System.getProperty("java.home") + File.separator + "lib" + File.separator + "security" + File.separator;
    
    
	public static void main(String[] args) {
		int jVersion = Integer.parseInt(System.getProperty("java.version").split("\\.")[1]);
		if(jVersion != 7) JOptionPane.showMessageDialog(null, "You are not using Java 7. You may encounter errors.", "Warning", JOptionPane.WARNING_MESSAGE);
		
		checkForJCE();
		
		mainFrame = new MainFrame();
	}
	
	
	private static void checkForJCE() {
	    if(!hasJCE()) {
	        int r = JOptionPane.showConfirmDialog(null, "You don't have the JCE Policy files. Should I install them for you now?", "No JCE Policy Files", JOptionPane.OK_CANCEL_OPTION, JOptionPane.WARNING_MESSAGE);
	        if(r == JOptionPane.OK_OPTION) {
	            try {
	            	File localPolFile = new File(jreSecurityFilePath + "local_policy.jar"),
	            	usExportPolFile = new File(jreSecurityFilePath + "US_export_policy.jar");
	                
	                if(!localPolFile.canWrite() || !usExportPolFile.canWrite()) {
	                	JOptionPane.showMessageDialog(null, "You don't have write access to the Java security policy files. Are you root/administrator?", "Error", JOptionPane.ERROR_MESSAGE);
	                	System.exit(1);
	                }
	                
	                boolean deleted = true;
	                deleted = localPolFile.delete();
	                deleted = usExportPolFile.delete();
	                
	                if(!deleted) {
	                	JOptionPane.showMessageDialog(null, "Error removing default Java security policy files. Are you root/administrator?", "Error", JOptionPane.ERROR_MESSAGE);
	                	System.exit(1);
	                }
	                
	                FileUtils.unzipFileInputStream(Boot.class.getResourceAsStream("/jce-7.zip"), new File(jreSecurityFilePath));
	                
	                JOptionPane.showMessageDialog(null, "Successfully Installed JCE Policy files! Restart is required.", "Success", JOptionPane.INFORMATION_MESSAGE);
	                System.exit(0);
	            } catch(Exception ex) { ex.printStackTrace(); JOptionPane.showMessageDialog(null, "Error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE); System.exit(-1); }
	        } else if(r == JOptionPane.CANCEL_OPTION) {
	            int r2 = JOptionPane.showConfirmDialog(null, "Would you like to continue without the Policy files (you will most likely recieve errors)?", "Warning", JOptionPane.OK_CANCEL_OPTION, JOptionPane.WARNING_MESSAGE);
	            
	            if(r2 == JOptionPane.CANCEL_OPTION) System.exit(0);
	            else if(r2 == JOptionPane.CLOSED_OPTION) System.exit(0);
	        } else if(r == JOptionPane.CLOSED_OPTION) System.exit(0);
	    }
	}
	
	
	private static boolean hasJCE() {
	    int maxKeyLength = 0;
		try {
		    maxKeyLength = Cipher.getMaxAllowedKeyLength("AES");
		} catch(Exception ex) {
			ex.printStackTrace();
			JOptionPane.showMessageDialog(null, "Error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
		}
		return maxKeyLength > 256;
	}

}
