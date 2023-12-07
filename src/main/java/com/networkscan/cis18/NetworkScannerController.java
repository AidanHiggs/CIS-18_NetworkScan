package com.networkscan.cis18;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.List;
import javax.swing.SwingWorker;
<<<<<<< HEAD

import org.pcap4j.core.PcapHandle;
=======
>>>>>>> origin/refac

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
<<<<<<< HEAD
                hostDisco.setNetworkSettings();
                int totalHosts = hostDisco.calculateDevices();
                PcapHandle networkInt = (PcapHandle) setNet.getNetworkHandle(); // Get PcapHandle from networkSettings

                List<InetAddress> addresses = hostDisco.generateHostAddresses(networkInt, totalHosts);
                List<String> discoveredHosts = new ArrayList<>();
                int count = 0;

                for (InetAddress addr : addresses) {
                    if (isCancelled()) {
                        break;
                    }
                    if (hostDisco.sendTcpPacketAndCheckResponse(addr, networkInt)) {
                        discoveredHosts.add(addr.getHostAddress());
                        publish(++count * 100 / addresses.size());
                    }
                }

                networkInt.close(); // Ensure the handle is closed after use
=======
                int totalHosts = hostDisco.calculateDevices();
                List<String> discoveredHosts = new ArrayList<>();
                InetAddress[] addresses = hostDisco.generateHostAddresses(totalHosts);
                
                for (InetAddress address : addresses) {
                    System.out.println("Scanning " + address.getHostAddress());
                    pingDecorator.addPingResolution(address.getHostAddress());
                    String[] results = pingDecorator.getPingResults();
                    for (String result : results) {
                        if (result != null && result.contains("Reply from")) {
                            discoveredHosts.add(address.getHostAddress());
                            break;
                        } else {
                            System.out.println("No reply from " + address.getHostAddress());
                        }
                    }
                }
>>>>>>> origin/refac
                return discoveredHosts;
            }

            @Override
<<<<<<< HEAD
            protected void process(List<Integer> chunks) {
                int latestPercentage = chunks.get(chunks.size() - 1);
                view.getResultArea().append("Scan completion: " + latestPercentage + "%\n");
            }

            @Override
            protected void done() {
                try {
                    List<String> discoveredHosts = get();
                    view.getResultArea().append("Scan completed. Discovered hosts:\n");
                    view.getResultArea().append(String.join("\n", discoveredHosts));
=======
            protected void done() {
                try {
                    List<String> discoveredHosts = get();
                    view.getResultArea().setText(String.join("\n", discoveredHosts));
>>>>>>> origin/refac
                } catch (Exception ex) {
                    view.getResultArea().setText("Error in host discovery: " + ex.getMessage());
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
