package com.networkscan.cis18;
import javax.swing.SwingUtilities;

public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                // Create and display the NetworkScannerGUI
                NetworkScannerGUI scanner = new NetworkScannerGUI();
                scanner.setVisible(true);

                // Instantiate and use PingDecorator
                PingDecorator myPing = new PingDecorator();
                myPing.printStuff();
            }
        });
    }
}
