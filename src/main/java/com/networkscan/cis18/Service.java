package com.networkscan.cis18;

public class Service {
    private String protocol;
    private String serviceName;

    public Service(String protocol, String serviceName) {
        this.protocol = protocol;
        this.serviceName = serviceName;
    }

    public String getProtocol() {
        return protocol;
    }

    public void setProtocol(String protocol) {
        this.protocol = protocol;
    }

    public String getServiceName() {
        return serviceName;
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }

}