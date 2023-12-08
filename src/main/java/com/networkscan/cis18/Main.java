package com.networkscan.cis18;

import javax.swing.SwingUtilities;

public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                
                NetworkScannerModel model = new NetworkScannerModel();
                NetworkScannerView view = new NetworkScannerView();
                
                portScanner portScanner = new portScanner(); 
                PingDecorator pingDecorator = new PingDecorator(portScanner); 

                hostDisco hostDiscovery = new hostDisco(model, pingDecorator);

                NetworkScannerController controller = new NetworkScannerController(model, view, portScanner, pingDecorator, hostDiscovery);
                
                view.setVisible(true);
            }
        });
    }
}
