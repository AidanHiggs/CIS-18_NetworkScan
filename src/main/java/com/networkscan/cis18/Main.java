//main
package com.networkscan.cis18;

import javax.swing.SwingUtilities;

public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                NetworkScannerGUI scanner = new NetworkScannerGUI();
                scanner.setVisible(true);
               //osDetectionDecorator loadFingerprints = new osDetectionDecorator();
            
           
                    hostImpl hostInstance = new hostImpl();
                    // Get the values from the host instance
                    String ipAddress = hostInstance.getIpAddress();
                    String hostName = hostInstance.getHostName();
                    String ports = hostInstance.getPorts();
                    String services = hostInstance.getServices();
                    
                    // Print the values
                    System.out.println("IP Address: " + ipAddress);
                    System.out.println("Host Name: " + hostName);
                    System.out.println("Ports: " + ports);
                    System.out.println("Services: " + services);
            }
        });
    }
}
