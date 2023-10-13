package com.networkscan.cis18;

import java.util.ArrayList;
import java.util.List;

public class hostImpl implements host {
    public String ipAddress;
    private String hostName;
    private List<String> services;
    private List<Integer> openPorts;
    public int id;

   public hostImpl() {
        openPorts = new ArrayList<>();
    }
    
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
    
    public List<String> getServices() {
        return services;
    }
    
    public void setServices(String service) {
        services.add(service);
    }

    public void addOpenPort(int port) {
        openPorts.add(port);
    }

    public List<Integer> getOpenPorts() {
        return openPorts;
    }

    public int getHostId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}

   