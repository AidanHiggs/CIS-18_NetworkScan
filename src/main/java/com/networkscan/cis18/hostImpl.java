package com.networkscan.cis18;

public class hostImpl implements host {
    public String ipAddress;
    private String hostName;
    private String services;
    private String ports;
    
    public String getIpAddress() {
        return ipAddress;
    }
    
    public void setIpAddress(String ipAddress) {
        this.ipAddress = ipAddress;
    }
    
    public String getHostName() {
        return hostName;
    }
    
    public void setHostName(String hostName) {
        this.hostName = hostName;
    }
    

    
    public void setPorts(String ports) {
        this.ports= ports;
    }
    
    public String getServices() {
        return services;
    }
    
    public void setServices(String services) {
        this.services = services;
    }

    @Override
    public String getPorts() {
         return ports;
    }
}

   