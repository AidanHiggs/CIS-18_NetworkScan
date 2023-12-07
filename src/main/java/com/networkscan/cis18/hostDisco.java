package com.networkscan.cis18;

import java.io.IOException;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.List;

public class hostDisco {
    private NetworkScannerModel model;
    private PingDecorator pingDecorator;

    public hostDisco(NetworkScannerModel model, PingDecorator pingDecorator) {
        this.model = model;
        this.pingDecorator = pingDecorator;
    }

    public List<String> hostDiscovery() throws IOException {
        int totalHosts = calculateDevices();
        List<String> discoveredHosts = new ArrayList<>();

        for (InetAddress hostAddr : generateHostAddresses(totalHosts)) {
            if (pingHost(hostAddr.getHostAddress())) {
                discoveredHosts.add(hostAddr.getHostAddress());
            }
        }
        return discoveredHosts;
    }

    private boolean pingHost(String hostAddress) {
        pingDecorator.addPingResolution(hostAddress);
        String[] results = pingDecorator.getPingResults();
        for (String result : results) {
            if (result != null && result.contains("Reply from")) {
                return true;
            }
        }
        return false;
    }

    public int calculateDevices() {
        int subnetBits = Integer.parseInt(model.getSubnetMask());
        if (subnetBits <= 0) {
            throw new IllegalArgumentException("Subnet is empty or invalid");
        }
        int hostBits = 32 - subnetBits;
        return (int) Math.pow(2, hostBits) - 2;
    }

    public InetAddress[] generateHostAddresses(int totalHosts) throws IOException {
        InetAddress[] hostAddresses = new InetAddress[totalHosts];
        String ipAddressString = model.getIpAddress();
        InetAddress ipAddress = InetAddress.getByName(ipAddressString);
        byte[] ipAddressBytes = ipAddress.getAddress();

        for (int i = 1; i <= totalHosts; i++) {
            ipAddressBytes[3] = (byte) i;
            hostAddresses[i-1] = InetAddress.getByAddress(ipAddressBytes);
        }
        return hostAddresses;
    }
}
