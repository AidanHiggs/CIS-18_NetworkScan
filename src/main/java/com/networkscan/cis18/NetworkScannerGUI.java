package com.networkscan.cis18;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

class NetworkScannerGUI extends JFrame {
    private JTextField ipAddressField;
    private JTextField subnetField;
    private JTextField exampleFeild;
    private JTextArea resultArea;
    private JButton scanButton;
    private JComboBox<String> scanMethodComboBox;

    // GUI Name and Size, and set to close on exit
    public NetworkScannerGUI() {
        setTitle("Network Scanner for CIS-18");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1080, 720);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());


        // Home screen panel
        JPanel homePanel = new JPanel();
        homePanel.setLayout(new FlowLayout());

        //Label for scan methods and add combo box selections to choose from
        JLabel scanMethodLabel = new JLabel("Select Your Scan Method:");
        String[] scanMethods = {"Ping","Ping Sweep", "Port Scan", "Tracert",};
        scanMethodComboBox = new JComboBox<>(scanMethods);

        //IP Address Label and size
        JLabel ipAddressLabel = new JLabel("Enter IP Address Here:");
        ipAddressField = new JTextField(20);

        //Subnet Mask Label and Size
        JLabel subnetLabel = new JLabel("Enter Subnet Mask Here:");
        subnetField = new JTextField(20);

        //Scan button label and response text field
        scanButton = new JButton("Press to Scan");
        resultArea = new JTextArea(30, 30);
        resultArea.setEditable(false);

        //Adding all labels, fields and combo box to the home screen
        homePanel.add(scanMethodLabel);
        homePanel.add(scanMethodComboBox);
        homePanel.add(ipAddressLabel);
        homePanel.add(ipAddressField);
        homePanel.add(subnetLabel);
        homePanel.add(subnetField);
        homePanel.add(scanButton);


        //Response to the scan button functionality and override to change the result area text
        scanButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                resultArea.setText("Scanning...\n"); // Just a placeholder message
            }
        });

        //Makes the home screen appear in middle of the screen instead of bottom right
        add(homePanel, BorderLayout.CENTER);
        //Puts the result above the home screen
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