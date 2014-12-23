package com.mrlolethan.stegstore.gui;

import java.awt.Frame;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.io.File;
import java.math.BigInteger;
import java.security.SecureRandom;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;

import com.mrlolethan.stegstore.Boot;
import com.mrlolethan.stegstore.cryptography.Cryptography;
import com.mrlolethan.stegstore.steganography.Steganography;

import net.miginfocom.swing.MigLayout;

public class CreateWizard extends JDialog implements ActionListener {
	private static final long serialVersionUID = 1L;
    
    private JPanel currentPnl;
    
    private JPanel pnl1 = new JPanel();
        private JButton nextBtn1 = new JButton("Next");
        private JButton backBtn1 = new JButton("Back");
        private JButton chooseFileBtn = new JButton("Choose the File to hide data in (The file will be copied)");
        private JTextField fileTxtField = new JTextField("");
        
    private JPanel pnl2 = new JPanel();
        private JButton nextBtn2 = new JButton("Next");
        private JButton backBtn2 = new JButton("Back");
        private JTextArea dataTxtArea = new JTextArea();
        private JScrollPane dataScrollPane = new JScrollPane(dataTxtArea);
    
    private JPanel pnl3 = new JPanel();
        private JButton nextBtn3 = new JButton("Next");
        private JButton backBtn3 = new JButton("Back");
        private JTextField keyTxtField = new JTextField("");
        
    private JPanel pnl4 = new JPanel();
        private JButton nextBtn4 = new JButton("Next");
        private JButton backBtn4 = new JButton("Back");
        private JTextField ivTxtField = new JTextField("");
        
    private JPanel pnl5 = new JPanel();
        private JButton nextBtn5 = new JButton("Finish");
        private JButton backBtn5 = new JButton("Back");
        private JTextField outTxtField = new JTextField("");
        private JButton changeOutBtn = new JButton("Change");
    
	public CreateWizard(Frame owner) {
		super(owner);
	    SwingUtilities.invokeLater(new Runnable() { public void run() {
	    
		setTitle("Create File Wizard");
		setSize(700,440);
		setDefaultCloseOperation(HIDE_ON_CLOSE);
		
		add(pnl1);
		    currentPnl = pnl1;
		initPnls();
		
		setModal(true);
		setResizable(false);
		setLocationRelativeTo(null);
		setVisible(true);
		
	    }});
	}
	
	
	private void initPnls() {
	    pnl1.setLayout(new MigLayout());
	        pnl1.add(backBtn1, "split 2, span, grow, pushx");
	            backBtn1.addActionListener(this);
	            backBtn1.setEnabled(false);
	        pnl1.add(nextBtn1, "grow, pushx");
	            nextBtn1.addActionListener(this);
	        
	        pnl1.add(fileTxtField, "split 2");
    	        fileTxtField.setColumns(30);
	            fileTxtField.setEditable(false);
	        pnl1.add(chooseFileBtn);
	            chooseFileBtn.addActionListener(this);
	    
	    pnl2.setLayout(new MigLayout());
	        pnl2.add(backBtn2, "split 2, span, grow, pushx");
	            backBtn2.addActionListener(this);
	        pnl2.add(nextBtn2, "grow, pushx");
	            nextBtn2.addActionListener(this);
	        pnl2.add(new JLabel("Data:"), "wrap");
	        pnl2.add(dataScrollPane);
	            dataScrollPane.setPreferredSize(getSize());
	    
	    pnl3.setLayout(new MigLayout());
	        pnl3.add(backBtn3, "split 2, span, grow, pushx");
	            backBtn3.addActionListener(this);
	        pnl3.add(nextBtn3, "grow, pushx");
	            nextBtn3.addActionListener(this);
	        pnl3.add(new JLabel("Key: "), "split 2");
	        pnl3.add(keyTxtField);
	            keyTxtField.setColumns(30);
	    
	    pnl4.setLayout(new MigLayout());
	        pnl4.add(backBtn4, "split 2, span, grow, pushx");
	            backBtn4.addActionListener(this);
	        pnl4.add(nextBtn4, "grow, pushx");
	            nextBtn4.addActionListener(this);
	        pnl4.add(new JLabel("Initialization Vector: "), "split 3");
	        pnl4.add(ivTxtField);
	            ivTxtField.setColumns(16);
	            ivTxtField.setEditable(false);
	            ivTxtField.setText("0000000000000000");
	        pnl4.add(new JLabel("(Move your cursor around to randomize)"));
	    
	    pnl5.setLayout(new MigLayout());
	        pnl5.add(backBtn5, "split 2, span, grow, pushx");
	            backBtn5.addActionListener(this);
	        pnl5.add(nextBtn5, "grow, pushx");
	            nextBtn5.addActionListener(this);
	        pnl5.add(new JLabel("Output File: "), "split 3");
	        pnl5.add(outTxtField);
	            outTxtField.setColumns(30);
	            outTxtField.setEditable(false);
	        pnl5.add(changeOutBtn);
	            changeOutBtn.addActionListener(this);
	    
	    addMouseMotionListener(new MouseMotionListener() {
			public void mouseMoved(MouseEvent e) {
			    if(currentPnl.equals(pnl4)) {
			        SecureRandom random = new SecureRandom();
			        
			        String ivStr = new BigInteger(80, random).toString(32);
			        
			        if(ivStr.length() != 16) return;
			        
		            ivTxtField.setText(ivStr);
			    }
			}
			public void mouseDragged(MouseEvent arg0) {}
		});
	}
    
    
	public void actionPerformed(ActionEvent e) {
		Object src = e.getSource();
		
		if(currentPnl.equals(pnl1)) {
		    if(src.equals(nextBtn1)) {
		        if(!fileTxtField.getText().isEmpty() && new File(fileTxtField.getText()).exists()) {
		            remove(pnl1);
		            add(pnl2);
		            currentPnl = pnl2;
		            update();
		        } else {
		            Toolkit.getDefaultToolkit().beep();
		        }
		    }
		    else if(src.equals(chooseFileBtn)) {
		        JFileChooser fileChooser = new JFileChooser();
		        int r = fileChooser.showOpenDialog(this);
		        
		        if(r == JFileChooser.APPROVE_OPTION) {
		            File file = fileChooser.getSelectedFile();
		            fileTxtField.setText(file.getAbsolutePath());
		        }
		    }
		}
		else if(currentPnl.equals(pnl2)) {
		    if(src.equals(backBtn2)) {
		        remove(pnl2);
		        add(pnl1);
		        currentPnl = pnl1;
		        update();
		    }
		    else if(src.equals(nextBtn2)) {
		        remove(pnl2);
		        add(pnl3);
		        currentPnl = pnl3;
		        update();
		    }
		}
		else if(currentPnl.equals(pnl3)) {
		    if(src.equals(backBtn3)) {
		        remove(pnl3);
		        add(pnl2);
		        currentPnl = pnl2;
		        update();
		    }
		    else if(src.equals(nextBtn3)) {
		        if(!keyTxtField.getText().isEmpty()) {
		            remove(pnl3);
		            add(pnl4);
		            currentPnl = pnl4;
		            update();
		        } else {
		            Toolkit.getDefaultToolkit().beep();
		        }
		    }
		}
		else if(currentPnl.equals(pnl4)) {
		    if(src.equals(backBtn4)) {
		        remove(pnl4);
		        add(pnl3);
		        currentPnl = pnl3;
		        update();
		    }
		    else if(src.equals(nextBtn4)) {
		        if(!ivTxtField.getText().isEmpty()) {
		            remove(pnl4);
		            add(pnl5);
		            currentPnl = pnl5;
		            update();
		        } else {
		            Toolkit.getDefaultToolkit().beep();
		        }
		    }
		}
		else if(currentPnl.equals(pnl5)) {
		    if(src.equals(backBtn5)) {
		        remove(pnl5);
		        add(pnl4);
		        currentPnl = pnl4;
		        update();
		    }
		    else if(src.equals(nextBtn5)) {
		        if(!outTxtField.getText().isEmpty()) {
		            if(new File(outTxtField.getText()).exists()) {
		                int r = JOptionPane.showConfirmDialog(Boot.mainFrame, "File already exists. Do you want to overwrite?", "Warning", JOptionPane.WARNING_MESSAGE);
		                if(r != JOptionPane.YES_OPTION) return;
		            }
		            try {
		                Steganography.hideDataInFile(new File(fileTxtField.getText()), new File(outTxtField.getText()), dataTxtArea.getText(), Cryptography.getSHA512(keyTxtField.getText()), ivTxtField.getText().getBytes());
		            } catch(Exception ex) { ex.printStackTrace(); JOptionPane.showMessageDialog(this.getOwner(), "Error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE); }
		            dispose();
		        } else {
		            Toolkit.getDefaultToolkit().beep();
		        }
		    }
		    else if(src.equals(changeOutBtn)) {
		        JFileChooser fileChooser = new JFileChooser();
		        int r = fileChooser.showSaveDialog(this);
		        
		        if(r == JFileChooser.APPROVE_OPTION) {
		            File file = fileChooser.getSelectedFile();
		            outTxtField.setText(file.getAbsolutePath());
		        }
		    }
		}
	}
    
    
    private void update() {
        setSize(getSize().width - 1, getSize().height - 1);
		setSize(getSize().width + 1, getSize().height + 1);
    }
    
    
}
