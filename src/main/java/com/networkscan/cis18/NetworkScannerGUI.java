//This is the NetworkScnnerGUI.java class
package com.networkscan.cis18; 

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Scanner;

public class NetworkScannerGUI extends JFrame {
    // Fields
    private JTextField ipAddressField;
    private JTextField subnetField;
    private JTextArea resultArea;
    private JButton scanButton;
    private JComboBox<String> scanMethodComboBox;

    public NetworkScannerGUI() {
        // Set window properties
        setTitle("Network Scanner for CIS-18");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1080, 720);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // Create home screen panel
        JPanel homePanel = new JPanel();
        homePanel.setLayout(new GridLayout(4, 2));

        // Label for scan methods and add combo box selections to choose from
        JLabel scanMethodLabel = new JLabel("Select Your Scan Method:");
        String[] scanMethods = {"Ping", "Ping Sweep", "Port Scan", "Tracert"};
        scanMethodComboBox = new JComboBox<>(scanMethods);
        homePanel.add(scanMethodLabel);
        homePanel.add(scanMethodComboBox);

        // IP Address Label and size
        JLabel ipAddressLabel = new JLabel("Enter IP Address Here:");
        ipAddressField = new JTextField(20);
        homePanel.add(ipAddressLabel);
        homePanel.add(ipAddressField);

        //Subnet Mask Label and Size
        JLabel subnetLabel = new JLabel("Enter Subnet Mask Here:");
        subnetField = new JTextField(20);
        homePanel.add(subnetLabel);
        homePanel.add(subnetField);

        //Scan button label and response text field
        scanButton = new JButton("Press to Scan");
        resultArea = new JTextArea(30, 30);
        resultArea.setEditable(false);
        homePanel.add(scanButton);

           scanButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String scanResult = getInputs();
                resultArea.append(scanResult);
            }
        });

        // Makes the home screen appear in the middle of the screen instead of bottom right
        add(homePanel, BorderLayout.CENTER);
        // Puts the result above the home screen
        add(new JScrollPane(resultArea), BorderLayout.NORTH);
    }
    
    private String getInputs() {
        String host = ipAddressField.getText();
        int startPort;
        int endPort;
        portScanner scanner = new portScanner(); // Create an instance of portScanner
        if (scanMethodComboBox.getSelectedItem().equals("Port Scan")) {
            String startPortInput = JOptionPane.showInputDialog("Enter the start port");
            String endPortInput = JOptionPane.showInputDialog("Enter the end port");
    
            if (startPortInput == null || endPortInput == null) {
                // User canceled the input, return an empty string
                return "";
            }
    
            startPort = Integer.parseInt(startPortInput);
            endPort = Integer.parseInt(endPortInput);
        } else {
            startPort = portScanner.getStartPort(null);
            endPort = portScanner.getEndPort(null);;
        }
    
        String scanResult;
    
        if (host.matches("\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}")) {
            // If it's an IP address, directly call the scanIp method
            scanResult = "Scanning IP: " + host;
            portScanner.scanIp(startPort, endPort, host, resultArea);
            
        } else {
            // If it's a hostname, resolve it to an IP address using the resolveHostname method
            String ipAddr = portScanner.resolveHostname(host, resultArea);
            scanResult = "Scanning hostname: " + host + " (resolved to IP: " + ipAddr + ")";
            portScanner.scanIp(startPort, endPort, ipAddr, resultArea);
        }
        return scanResult;
    }
    

    
    

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                NetworkScannerGUI scanner = new NetworkScannerGUI();
                scanner.setVisible(true);
            }
        });
    }
}
