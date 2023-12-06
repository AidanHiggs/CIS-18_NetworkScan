package com.networkscan.cis18;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

public class portScanner {
    private Map<String, Service> portServicesMap = new HashMap<>();

    public portScanner() {
        loadPortServices("src/main/resources/com/networkscan/cis18/ports.txt"); // Ensure this path is correct in your context
    }

    private void loadPortServices(String filename) {
        try (BufferedReader br = new BufferedReader(new FileReader(filename))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(" ");
                if (parts.length >= 3) {
                    String keyParts = parts[0];
                    Service svc = new Service(parts[1], parts[2]);
                    portServicesMap.put(keyParts, svc);
                }
            }
        } catch (IOException e) {
            System.err.println("Error loading port services: " + e.getMessage());
        }
    }

    public String scanIp(String host, int startPort, int endPort) {
        StringBuilder sb = new StringBuilder();
        for (int port = startPort; port <= endPort; port++) {
            try (Socket socket = new Socket(host, port)) {
                socket.setSoTimeout(5000);
                Service service = portServicesMap.getOrDefault(String.valueOf(port), new Service("Unknown", "Unknown"));
                sb.append("Port ").append(port).append(" is open - ").append(service).append("\n");
            } catch (IOException e) {
                // Ignore exceptions for closed ports
            }
        }
        return sb.toString();
    }
}
