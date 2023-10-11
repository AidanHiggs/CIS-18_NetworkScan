package com.networkscan.cis18;

import javax.swing.JTextField;

public class hostUpdate extends Thread {
    private JTextField ipAddressField;

    public hostUpdate(JTextField ipAddressField) {
        this.ipAddressField = ipAddressField;
    }

    @Override
    public void run() {
        String initialHost = ipAddressField.getText();
        while (true){
            String host = ipAddressField.getText();
            if (!host.equals(initialHost) && host.matches("\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}")) {
                hostImpl hostInstance = hostFactory.createHost(host);
                initialHost = host;
                } 
        }
        
    }
}
