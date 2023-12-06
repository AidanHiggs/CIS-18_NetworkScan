package com.networkscan.cis18;

public class setSubnet extends NetworkScannerGUI {
    public int getSubnet() {
        System.out.println("pulling subnet");
        try {
            System.out.println(subnetField.getText());
        } catch (Exception e) {
            System.out.println("no subnet");
        }
        int subnet = (Integer.parseInt(subnetField.getText()));
        System.out.println("Subnet: " + subnet);
        return subnet;
    }   
}