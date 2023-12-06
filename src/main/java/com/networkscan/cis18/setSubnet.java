package com.networkscan.cis18;

public class setSubnet implements SubnetStrategy {
    @Override
    public int getSubnet(NetworkScannerModel model) {
        System.out.println("pulling subnet");
        try {
            String subnetStr = model.getSubnetMask();
            System.out.println("Subnet: " + subnetStr);
            return Integer.parseInt(subnetStr);
        } catch (NumberFormatException e) {
            System.out.println("no subnet or invalid format");
            return -1; // Return a default value indicating failure
        }
    }
}
