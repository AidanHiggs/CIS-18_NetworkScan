package com.networkscan.cis18;
import javax.swing.SwingUtilities;


import javax.swing.SwingUtilities;

public class main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                NetworkScannerGUI scanner = new NetworkScannerGUI();
                scanner.setVisible(true);
            }
        });
    }
}
