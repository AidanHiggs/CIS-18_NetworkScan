package com.networkscan.cis18;

public final class Service {
    private final String protocol;
    private final String serviceName;

    public Service(String protocol, String serviceName) {
        this.protocol = protocol;
        this.serviceName = serviceName;
    }

    public String getProtocol() {
        return protocol;
    }

    public String getServiceName() {
        return serviceName;
    }
}
