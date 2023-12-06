package com.networkscan.cis18;

import java.util.ArrayList;
import java.util.List;

public class hostUpdate {
    private NetworkScannerModel model;
    private hostFactory factory;
    private List<host> hosts;

    public hostUpdate(NetworkScannerModel model, hostFactory factory) {
        this.model = model;
        this.factory = factory;
        this.hosts = new ArrayList<>();
    }

    public synchronized List<host> getHosts() {
        return new ArrayList<>(hosts);
    }

    public void updateHosts() {
        String currentIP = model.getIpAddress();
        int subnet = Integer.parseInt(model.getSubnetMask());

        if (!currentIP.matches("\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}")) {
            return; // Invalid IP format
        }

        host hostInstance = factory.createHost(currentIP, subnet);
        synchronized (this) {
            if (!hosts.contains(hostInstance)) {
                hosts.add(hostInstance);
                System.out.println("Updated host: " + hostInstance);
            }
        }
    }
}
