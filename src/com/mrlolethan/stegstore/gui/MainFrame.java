
package com.mrlolethan.stegstore.gui;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.UIManager.LookAndFeelInfo;

import net.miginfocom.swing.MigLayout;

import com.mrlolethan.stegstore.Boot;
import com.mrlolethan.stegstore.cryptography.Cryptography;
import com.mrlolethan.stegstore.steganography.Steganography;
import com.mrlolethan.stegstore.util.StringUtils;
import com.sun.org.apache.xml.internal.security.utils.Base64;

public class MainFrame extends JFrame implements ActionListener {
  private static final long serialVersionUID = 1L;
    
    private static final String TITLE = Boot.NAME + " v" + Boot.VERSION;
    private static final Dimension FRAME_SIZE = new Dimension(740, 480);
    
    
    private File loadedFile;
    private byte[] loadedFileData;
    private String loadedFileKey;
    private byte[] loadedFileIV;
    
    private JPanel pnl = new JPanel(new MigLayout());
        private JTextArea viewTxtArea = new JTextArea();
        private JScrollPane viewScrollPane = new JScrollPane(viewTxtArea);
        private JButton openFileBtn = new JButton("Open File");
        private JButton createFileBtn = new JButton("Create File");
        private JButton saveFileBtn = new JButton("Save File");
        private JButton closeFileBtn = new JButton("Close File");
    
    
	public MainFrame() {
	    SwingUtilities.invokeLater(new Runnable() {
	        public void run() {
	            setTitle(TITLE);
	            setSize(FRAME_SIZE);
	            setDefaultCloseOperation(EXIT_ON_CLOSE);
	            
	            add(pnl);
    	            initPnl();
	            
	            setResizable(false);
	            setLaf("Nimbus");
	            setLocationRelativeTo(null);
	            setVisible(true);
	        }
	    });
	}
	
	private void setLaf(String name) {
	    for(LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
	        if(info.getName().equalsIgnoreCase(name)) {
	            try { UIManager.setLookAndFeel(info.getClassName()); } catch(Exception ex) {}
	            break;
	        }
	    }
	    SwingUtilities.updateComponentTreeUI(this);
	}
	
	
	private void initPnl() {
	    pnl.add(viewScrollPane, "span, wrap");
	        viewScrollPane.setPreferredSize(FRAME_SIZE);
	        viewTxtArea.setEnabled(false);
	    
	    pnl.add(openFileBtn);
	        openFileBtn.addActionListener(this);
	    pnl.add(createFileBtn);
	        createFileBtn.addActionListener(this);
	    pnl.add(saveFileBtn);
	        saveFileBtn.addActionListener(this);
	        saveFileBtn.setEnabled(false);
	    pnl.add(closeFileBtn);
	        closeFileBtn.addActionListener(this);
	        closeFileBtn.setEnabled(false);
	}
	
	
	public void actionPerformed(ActionEvent e) {
		Object src = e.getSource();
		
		if(src.equals(openFileBtn)) {
		    JFileChooser fc = new JFileChooser();
		    int r = fc.showOpenDialog(this);
		    
		    if(r == JFileChooser.APPROVE_OPTION) {
		        File file = fc.getSelectedFile();
		        loadedFile = file;
		        JPasswordField passField = new JPasswordField();
		        int ret = JOptionPane.showConfirmDialog(this, passField, "Please enter the encryption key", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
		        if(ret == JOptionPane.CANCEL_OPTION) return;
		        
		        String key = Cryptography.getSHA512(new String(passField.getPassword()));
		        
		        try {
		            loadedFileIV = Base64.decode(Cryptography.decryptAESwithExceptions(StringUtils.getBetween(Steganography.readFileAsString(file), Boot.IV_TAG), key, Boot.IV_AES_IV));
		            viewTxtArea.setText(Cryptography.decryptAESwithExceptions(StringUtils.getBetween(Steganography.readFileAsString(file), Boot.DATA_TAG), key, loadedFileIV));
		            loadedFileData = Steganography.readFileBytes(file);
		            
		            byte[] loadedFileDataCP = loadedFileData;
		            loadedFileData = new byte[loadedFileDataCP.length - (Boot.DATA_TAG + StringUtils.getBetween(Steganography.readFileAsString(file), Boot.DATA_TAG) + Boot.DATA_TAG).length()];
		            for(int i = 0; i < loadedFileData.length; i++) {
		                loadedFileData[i] = loadedFileDataCP[i];
		            }
		            
		            loadedFileKey = key;
		            
		            viewTxtArea.setEnabled(true);
		            saveFileBtn.setEnabled(true);
		            closeFileBtn.setEnabled(true);
		        } catch(Exception ex) {
		        	ex.printStackTrace();
		        	JOptionPane.showMessageDialog(this, "Error: Invalid key or not a valid StegStore container.", "Error", JOptionPane.PLAIN_MESSAGE);
		        	viewTxtArea.setEnabled(false);
		            saveFileBtn.setEnabled(false);
		            closeFileBtn.setEnabled(false);
		            
		            loadedFile = null;
		            loadedFileData = null;
		            loadedFileKey = null;
		            loadedFileIV = null;
		        }
		    }
		}
		else if(src.equals(createFileBtn)) {
		    new CreateWizard(this);
		}
		else if(src.equals(saveFileBtn)) {
		    Steganography.hideDataInFile(loadedFileData, loadedFile, viewTxtArea.getText(), loadedFileKey, loadedFileIV);
		    JOptionPane.showMessageDialog(this, "Successfully saved data!", "Saved", JOptionPane.PLAIN_MESSAGE);
		}
		else if(src.equals(closeFileBtn)) {
		    viewTxtArea.setText("");
		    viewTxtArea.setEnabled(false);
		    saveFileBtn.setEnabled(false);
		    closeFileBtn.setEnabled(false);
		    
		    loadedFile = null;
		    loadedFileData = null;
		    loadedFileKey = null;
		    loadedFileIV = null;
		}
	}
    
    
}
