package com.networkscan.cis18;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JTextField;

public class hostUpdate extends Thread {
    private JTextField ipAddressField;
    private JTextField subnetField;
    public List<host> hosts = new ArrayList();
    int ip = 0;
    public hostUpdate(JTextField ipAddressField, JTextField subnetField) {
        this.ipAddressField = ipAddressField;
        this.subnetField = subnetField;
        
    }
    public List<host> getHosts() {
        return hosts;
    }

    @Override
    public void run() {
        String initialHost = ipAddressField.getText();
        String ip = ipAddressField.getText();
        int subnet = (Integer.parseInt(subnetField.getText()));
        while (true){
            String host = ipAddressField.getText();
            if (!host.equals(initialHost)&&!host.matches("\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}")) {
                host hostInstance = hostFactory.createHost(ip, subnet);
                hosts.add(hostInstance);
            
                initialHost = host;
                System.out.println(hostInstance);
                }
        }
        
    }
}
