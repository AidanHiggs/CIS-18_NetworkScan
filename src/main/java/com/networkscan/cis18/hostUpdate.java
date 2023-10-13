package com.networkscan.cis18;

import java.util.ArrayList;
import java.util.List;

import javax.swing.JTextField;

public class hostUpdate extends Thread {
    private JTextField ipAddressField;
    public List<host> hosts = new ArrayList();
    public hostUpdate(JTextField ipAddressField) {
        this.ipAddressField = ipAddressField;
    }
    public List<host> getHosts() {
        return hosts;
    }

    @Override
    public void run() {
        String initialHost = ipAddressField.getText();
        while (true){
            String host = ipAddressField.getText();
            if (!host.equals(initialHost) && host.matches("\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}")&&!host.matches("\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}")) {
                host hostInstance = hostFactory.createHost(host);
                hosts.add(hostInstance);
            
                initialHost = host;
                }
        }
        
    }
}
