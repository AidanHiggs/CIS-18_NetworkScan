package com.networkscan.cis18;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class hostImpl implements host {
    private String ipAddress;
    private String hostName;
    private List<String> services;
    private List<Integer> openPorts;
    private int id;
    private int subnet;

    public hostImpl() {
        openPorts = new CopyOnWriteArrayList<>();
        services = new CopyOnWriteArrayList<>();
    }

    @Override
    public String getIpAddress() {
        return ipAddress;
    }

    @Override
    public void setIpAddress(String ipAddress) {
        this.ipAddress = ipAddress;
    }

    @Override
    public String getHostName() {
        return hostName;
    }

    @Override
    public void setHostName(String hostName) {
        this.hostName = hostName;
    }

    @Override
    public List<String> getServices() {
        return Collections.unmodifiableList(services);
    }

    @Override
    public void setServices(String service) {
        services.add(service);
    }

    @Override
    public void addOpenPort(int port) {
        openPorts.add(port);
    }

    @Override
    public List<Integer> getOpenPorts() {
        return Collections.unmodifiableList(openPorts);
    }

    @Override
    public int getHostId() {
        return id;
    }

    @Override
    public void setId(int id) {
        this.id = id;
    }

    @Override
    public void setSubnet(int subnet) {
        this.subnet = subnet;
    }

    @Override
    public int getSubnet() {
        return subnet;
    }

    public static hostImpl getInstance(String ipAddress2) {
        return null;
    }
}
