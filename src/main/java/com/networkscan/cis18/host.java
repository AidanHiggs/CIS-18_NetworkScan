package com.networkscan.cis18;

    public interface host {
        String getIpAddress();
        void setIpAddress(String ipAddress);
        
        String getHostName();
        void setHostName(String hostName);

        String getPorts();
        void setPorts(String ports);

        String getServices();
        void setServices(String services);
        
}


