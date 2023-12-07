package com.networkscan.cis18;

import javax.swing.SwingUtilities;

public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                // Initialize Model, View, and other components
                NetworkScannerModel model = new NetworkScannerModel();
                NetworkScannerView view = new NetworkScannerView();
                
                // Initialize the portScanner and PingDecorator
                portScanner portScanner = new portScanner(); // Assuming default constructor
                PingDecorator pingDecorator = new PingDecorator(portScanner); // Assuming this is the correct way to initialize

                // Initialize hostDisco with PingDecorator instead of setNet
                hostDisco hostDiscovery = new hostDisco(model, pingDecorator);

                // Pass the properly initialized components to the controller
                NetworkScannerController controller = new NetworkScannerController(model, view, portScanner, pingDecorator, hostDiscovery);
                
                // Set the view visible
                view.setVisible(true);
            }
        });
    }
}
