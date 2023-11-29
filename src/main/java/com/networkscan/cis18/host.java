package com.networkscan.cis18;
import java.util.List;

public interface host {
        String getIpAddress();
        void setIpAddress(String ipAddress);

        String getSubnet();
        
         void setSubnet(String subnet);
        
        String getHostName();
        void setHostName(String hostName);

        List<Integer> getOpenPorts();
        void addOpenPort(int ports);

        List<String> getServices();
        void setServices(String services);

        int getHostId();
        void setId(int id);

        
        
}


