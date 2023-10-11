package com.networkscan.cis18;

public class hostFactory {
    public static hostImpl createHost(String ipAddress) {
        hostImpl host = new hostImpl();
        host.setIpAddress(ipAddress);
        return host;
    }
}