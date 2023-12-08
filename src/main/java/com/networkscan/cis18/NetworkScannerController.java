package com.networkscan.cis18;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.List;
import javax.swing.SwingWorker;
public class NetworkScannerController {
    private NetworkScannerModel model;
    private NetworkScannerView view;
    private portScanner portScanner;
    private PingDecorator pingDecorator;
    private hostDisco hostDisco;

    public NetworkScannerController(NetworkScannerModel model, NetworkScannerView view,
                                    portScanner portScanner, PingDecorator pingDecorator, hostDisco hostDisco) {
        this.model = model;
        this.view = view;
        this.portScanner = portScanner;
        this.pingDecorator = pingDecorator;
        this.hostDisco = hostDisco;
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
        SwingWorker<List<String>, Integer> worker = new SwingWorker<List<String>, Integer>() {
            @Override
            protected List<String> doInBackground() throws Exception {
                int totalHosts = hostDisco.calculateDevices();
                List<String> discoveredHosts = new ArrayList<>();
                InetAddress[] addresses = hostDisco.generateHostAddresses(totalHosts);
                
                for (InetAddress address : addresses) {
                    view.getResultArea().append("Scanning " + totalHosts + " hosts" + "on" + model.getIpAddress() + "\n");
                    //System.out.println("Scanning " + address.getHostAddress());
                    pingDecorator.addPingResolution(address.getHostAddress());
                    String[] results = pingDecorator.getPingResults(model.getIpAddress());
                    for (String result : results) {
                        if (result != null && result.contains("reply from")) {
                            discoveredHosts.add(address.getHostAddress());
                            break;
                        } else {
                            //System.out.println("No reply from " + address.getHostAddress());
                        }
                    }
                }
                
                return discoveredHosts;
            }

            @Override
            protected void done() {
                try {
                    view.getResultArea().setText("Host discovery complete.");
                    List<String> discoveredHosts = get();
                    for (String host : discoveredHosts) {
                        view.getResultArea().append(host + "\n");
                    }
                    view.getResultArea().append(String.join("\n", discoveredHosts));
                } catch (Exception ex) {
                    view.getResultArea().append("Error in host discovery: " + ex.getMessage());
                }
            }
        };

        worker.execute();
    }

    private void performPortScan() {
        String portScanResult = portScanner.scanIp(model.getIpAddress(), 1, 65535);
        view.getResultArea().setText(portScanResult);
    }

    private void performPing() {
        //String pingResult = pingDecorator.scanIp(model.getIpAddress(), 1, 1);
        String[] result = pingDecorator.getPingResults(model.getIpAddress());
             for (String results : result) {
               view.getResultArea().append(results + "\n");
        
        }
    }

    private static class ScanMethods {
        public static final String HOST_DISCOVERY = "host discovery";
        public static final String PORT_SCAN = "Port Scan";
        public static final String TRACERT_WHOIS = "Tracert, whoIs, ETC.";
        public static final String PING = "Ping";
    }
}
