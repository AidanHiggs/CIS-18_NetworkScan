package com.networkscan.cis18;

public class hostFactory {
    private static int hostCount = 0;

    public static hostImpl createHost(String ipAddress) {
        hostCount++;
        hostImpl host = new hostImpl();
        host.setIpAddress(ipAddress);
        return host;
    }
    public static int getHostCount() {
        return hostCount;
    }
}