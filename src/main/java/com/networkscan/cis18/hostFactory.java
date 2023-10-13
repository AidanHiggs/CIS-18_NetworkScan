package com.networkscan.cis18;

public class hostFactory {
    public static volatile int id = 0;
    public static host createHost(String ipAddress) {
        host host = new hostImpl();
        host.setIpAddress(ipAddress);
        host.setId(id++);
        return host;
    }
}