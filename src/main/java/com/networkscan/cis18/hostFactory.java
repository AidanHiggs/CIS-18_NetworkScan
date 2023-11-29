package com.networkscan.cis18;
public class hostFactory extends NetworkScannerGUI {
    public static volatile int id = 0;
    private static host[] hosts;
    public static host createHost(String ipAddress, String subnet) {
        host host = new hostImpl();
        host.setIpAddress(ipAddress);
        host.setSubnet(subnet);
        //host.setSubnet = subnetField.getText(); 
        host.setId(id++);
        
        
        return host;

    }}
