package com.networkscan.cis18;

public class hostFactory {
    private static volatile int id = 0;

    public static host createHost(String ipAddress, int subnet) {
        hostImpl host = hostImpl.getInstance(ipAddress);
        host.setSubnet(subnet);
        synchronized (hostFactory.class) {
            host.setId(id++);
        }
        System.out.println("New Host's ID: " + host.getHostId());
        return host;
    }
}
