//This is the NetworkScnnerGUI.java class
package com.networkscan.cis18; 

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Scanner;

public class NetworkScannerGUI extends JFrame {
    // Fields
    protected static JTextField ipAddressField;
    protected static JTextField subnetField;
    protected static JTextArea resultArea;
    protected static JButton scanButton;
    protected static JComboBox<String> scanMethodComboBox;

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
                String scanResult = portScanner.getInputs();
                resultArea.append(scanResult);
            }
        });

        // Makes the home screen appear in the middle of the screen instead of bottom right
        add(homePanel, BorderLayout.CENTER);
        // Puts the result above the home screen
        add(new JScrollPane(resultArea), BorderLayout.NORTH);
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
