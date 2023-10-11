package com.networkscan.cis18;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JTextField;

public class hostUpdate extends Thread {
    private JTextField ipAddressField;
    private JTextField subnetField;
    public List<host> hosts = new ArrayList();
    public hostUpdate(JTextField ipAddressField, JTextField subnetField) {
        this.ipAddressField = ipAddressField;
    }
    public List<host> getHosts() {
        return hosts;
    }
        String initialHost = ipAddressField.getText();
        String ipaddr = ipAddressField.getText();
        String subnet = subnetField.getText();
        while (true){
            String host = ipAddressField.getText();
            if (!host.equals(initialHost) && host.matches("\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}")&&!host.matches("\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}")) {
                host hostInstance = hostFactory.createHost(host);
                hosts.add(hostInstance);
            
            if (!host.equals(initialHost) && host.matches("\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}")) {
                hostImpl hostInstance = hostFactory.createHost(host);
                initialHost = host;
                System.out.println(hostInstance);
                }
                }
        }
        
    }

