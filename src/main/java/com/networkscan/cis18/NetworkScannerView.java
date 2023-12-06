package com.networkscan.cis18;

import javax.swing.*;
import java.awt.*;

public class NetworkScannerView extends JFrame {
    private JTextField ipAddressField = new JTextField(20);
    private JTextField subnetField = new JTextField(20);
    private JTextArea resultArea = new JTextArea(30, 30);
    private JButton scanButton = new JButton("Press to Scan");
    private JComboBox<String> scanMethodComboBox;

    public NetworkScannerView() {
        setTitle("Network Scanner for CIS-18");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1080, 720);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        JPanel homePanel = new JPanel();
        homePanel.setLayout(new GridLayout(4, 2));
        String[] scanMethods = {"Ping", "host discovery", "Port Scan", "Tracert, whoIs, ETC."};
        scanMethodComboBox = new JComboBox<>(scanMethods);

        homePanel.add(new JLabel("Select Your Scan Method:"));
        homePanel.add(scanMethodComboBox);
        homePanel.add(new JLabel("Enter IP Address Here:"));
        homePanel.add(ipAddressField);
        homePanel.add(new JLabel("Enter Subnet Mask Here:"));
        homePanel.add(subnetField);
        resultArea.setEditable(false);
        homePanel.add(scanButton);

        add(homePanel, BorderLayout.CENTER);
        add(new JScrollPane(resultArea), BorderLayout.NORTH);
    }

    public JTextField getIpAddressField() {
        return ipAddressField;
    }

    public JTextField getSubnetField() {
        return subnetField;
    }

    public JTextArea getResultArea() {
        return resultArea;
    }

    public JButton getScanButton() {
        return scanButton;
    }

    public JComboBox<String> getScanMethodComboBox() {
        return scanMethodComboBox;
    }
}
