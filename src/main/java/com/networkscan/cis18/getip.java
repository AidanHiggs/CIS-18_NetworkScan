package com.networkscan.cis18;

public class getip implements IpAddressStrategy {
    @Override
    public String getIpAddress(NetworkScannerModel model) {
        return model.getIpAddress();
    }
}
