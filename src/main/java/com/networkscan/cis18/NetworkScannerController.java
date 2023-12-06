package com.networkscan.cis18;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

public class NetworkScannerController {
    private NetworkScannerModel model;
    private NetworkScannerView view;
    private portScanner portScanner; // Make sure this is initialized
    private PingDecorator pingDecorator; // Make sure this is initialized
    private hostDisco hostDisco;

    public NetworkScannerController(NetworkScannerModel model, NetworkScannerView view,
                                    portScanner portScanner, PingDecorator pingDecorator, hostDisco hostDisco) {
        this.model = model;
        this.view = view;
        this.portScanner = portScanner; // Ensure portScanner is provided here
        this.pingDecorator = pingDecorator; // Ensure pingDecorator is provided here
        initView();
    }

    private void initView() {
        view.getScanButton().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String selectedScanMethod = view.getScanMethodComboBox().getSelectedItem().toString();
                model.setIpAddress(view.getIpAddressField().getText());
                model.setSubnetMask(view.getSubnetField().getText());

                switch (selectedScanMethod) {
                    case ScanMethods.HOST_DISCOVERY:
                        performHostDiscovery();
                        break;
                    case ScanMethods.PORT_SCAN:
                        performPortScan();
                        break;
                    case ScanMethods.TRACERT_WHOIS:
                        view.getResultArea().setText("Tracert, whoIs, ETC. functionality not implemented yet.");
                        break;
                    case ScanMethods.PING:
                        performPing();
                        break;
                    default:
                        view.getResultArea().setText("Unknown scan method selected.");
                        break;
                }
            }
        });
    }

    private void performHostDiscovery() {
        try {
            List<String> discoveredHosts = hostDisco.tcpSweep(model.getSubnetMask());
            view.getResultArea().setText(String.join("\n", discoveredHosts));
        } catch (Exception ex) {
            view.getResultArea().setText("Error in host discovery: " + ex.getMessage());
        }
    }

    private void performPortScan() {
        String portScanResult = portScanner.scanIp(model.getIpAddress(), 1, 65535);
        view.getResultArea().setText(portScanResult);
    }

    private void performPing() {
        String pingResult = pingDecorator.scanIp(model.getIpAddress(), 1, 1);
        view.getResultArea().setText(pingResult);
    }

    private static class ScanMethods {
        public static final String HOST_DISCOVERY = "host discovery";
        public static final String PORT_SCAN = "Port Scan";
        public static final String TRACERT_WHOIS = "Tracert, whoIs, ETC.";
        public static final String PING = "Ping";
    }
}