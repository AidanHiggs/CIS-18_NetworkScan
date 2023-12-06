package com.networkscan.cis18;
public class hostFactory extends NetworkScannerGUI {
    public static volatile int id = 0;
    private static host[] hosts;
    public static host createHost(String ipAddress, int subnet) {
        host host = new hostImpl();
        host.setIpAddress(ipAddressField.getText());
        host.setSubnet(Integer.parseInt(subnetField.getText()));
        host.setId(id++);
        System.out.println("New Host`s ID: " + host.getHostId());
        return host;

    }
}
