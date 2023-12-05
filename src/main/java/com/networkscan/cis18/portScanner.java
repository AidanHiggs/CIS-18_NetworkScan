package com.networkscan.cis18;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.URISyntaxException;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

import javax.swing.JOptionPane;
import javax.swing.JTextArea;
import javax.swing.SwingUtilities;
import javax.swing.SwingWorker;


public class portScanner extends NetworkScannerGUI{
    static Scanner input = new Scanner(System.in);
    static Map<String, Service> portServicesMap = new HashMap<>();

    public static void main(String[] args) {
        loadPortServices("src/main/java/com/networkscan/cis18/ports.txt");
       // try {
           // loadPortServices(Paths.get(portScanner.class.getResource("ports.txt").toURI()).toFile());
        //} catch (URISyntaxException e) {
         //   throw new RuntimeException(e);
        //}
    }

// Method to get the IP address from the user
public static String getIpAddress() {
    String ipAddr = new hostImpl().getIpAddress();
    return ipAddr;
}

    // Method to get the start port from the user

/**
 * Loads port services from a file and populates the portServicesMap.
 *
 * @param  string  the name of the file containing the port services
 */
public static void loadPortServices(String filename) {
    System.out.printf("File: %s%n", filename.toString());
    try (BufferedReader br = new BufferedReader(new FileReader(filename))) {
        String line;
        while ((line = br.readLine()) != null) {
            String[] parts = line.split(" ");
            if (parts.length >= 3) {
                String keyParts = parts[0];
                Service svc = new Service(parts[1], parts[2]);

                if (!keyParts.isEmpty()) {
                    portServicesMap.put(keyParts, svc);
                }
            }
        }
    } catch (IOException e) {
        e.printStackTrace();
    }
}
    

    public static Service getService(int port) {
        return portServicesMap.getOrDefault(String.valueOf(port),new Service("Unknown", "Unknown"));
    }


    // Method to scan the IP address for open ports
    // Method to scan the IP address for open ports
    public static void scanIp(int startPort, int endPort, String host, JTextArea resultArea) {
        // Create a SwingWorker to perform the scanning task asynchronously
        SwingWorker<String, Void> worker = new SwingWorker<String, Void>() {
            // Override the doInBackground method to perform the scanning task
            @Override
            protected String doInBackground() throws Exception {
                // Create a StringBuilder to store the scan results
                StringBuilder sb = new StringBuilder();
                // Iterate through the range of ports to scan
                for (int port = startPort; port <= endPort; port++) {
                    try {
                        // Create a socket and connect to the IP address and port
                        Socket socket = new Socket();
                        socket.connect(new InetSocketAddress(host, port), 5000);
                        // Create a key to identify the service based on the port and protocol
                        int key = port;
                        // Get the service associated with the port and protocol
                        Service service = getService(port);
                        // If the service is unknown, print a message
                        if (service.getServiceName().equals("Unknown")) {
                            System.out.println("Service not found for key: " + key);
                        }
                        // Append the open port and service to the scan results
                        sb.append("Port " + port + " is open - " + service.toString() + "\n");
                        // Close the socket
                        socket.close();
                        // Add the open port to the hostImpl instance
                        hostImpl hostInstance = new hostImpl();
                        hostInstance.addOpenPort(port);
                    } catch (IOException e) {
                        // Ignore exceptions for closed ports
                    }
                }
                // Return the scan results
                return sb.toString();
            }
    
            // Override the done method to update the UI with the scan results
            @Override
            protected void done() {
                try {
                    // Get the scan results from the doInBackground method
                    String scanResult = get();
                    // Update the UI with the scan results
                    SwingUtilities.invokeLater(() -> {
                        resultArea.append(scanResult);
                        String[] ports = scanResult.split("\n");
                        for (String port : ports) {
                            host host = new hostImpl();
                           
                            System.out.println(port);
                            
                        }
                        hostImpl hostInstance = new hostImpl();
                        String ipAddress = hostInstance.getIpAddress();
                        String hostName = hostInstance.getHostName();
                        List<String> services = hostInstance.getServices();
                        List<Integer> openPorts = hostInstance.getOpenPorts();
                        System.out.println("IP Address: " + ipAddress);
                        System.out.println("Host Name: " + hostName);
                        System.out.println("Services: " + services);
                        System.out.println("Open Ports: " + openPorts);
                    });
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };
    
        // Call the execute method here
        worker.execute();
    }

public static String resolveHostname(String host, JTextArea resultArea) {
    StringBuilder sb = new StringBuilder();
    int retries = 0;
    int maxRetries = 4;

    while (retries < maxRetries) {
        try {
            InetAddress address = InetAddress.getByName(host);
            host h = new hostImpl();
            h.setHostName(host);
            return address.getHostAddress();

        } catch (Exception e) {
            sb.append("The hostname " + host + " could not be resolved, please try again");
            resultArea.append(sb.toString());
            retries++;
        }
    }

    System.out.println("Maximum number of failures reached, exiting...");
    return null;
}



    // Method to close the input scanner
    public static void closeInput() {
        // Close the input scanner
        input.close();
    }
    static String getInputs() {
        String host = ipAddressField.getText();
        hostImpl h = new hostImpl();
        h.setIpAddress(host);
        int startPort;
        int endPort;
        if (scanMethodComboBox.getSelectedItem().equals("Port Scan")) {
            String startPortInput = JOptionPane.showInputDialog("Enter the start port");
            String endPortInput = JOptionPane.showInputDialog("Enter the end port");
            loadPortServices("src/main/resources/com/networkscan/cis18/ports.txt");
            if (startPortInput == null || endPortInput == null) {
                // User canceled the input, return an empty string
                return "";
            }
    
            startPort = Integer.parseInt(startPortInput);
            endPort = Integer.parseInt(endPortInput);
        } else {
            startPort = 1;
            endPort = 65535;
        }
    
        String scanResult;
    
        if (host.matches("\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}")) {
            // If it's an IP address, directly call the scanIp method
            
            scanResult = "Scanning IP: " + host + "\n";
            scanIp(startPort, endPort, host, resultArea);
            
        } else {
            // If it's a hostname, resolve it to an IP address using the resolveHostname method
            String ipAddr = resolveHostname(host, resultArea);
            scanResult = "Scanning hostname: " + host + " (resolved to IP: " + ipAddr + ")";
            System.out.println(ipAddr);
            scanIp(startPort, endPort, ipAddr, resultArea);
        }
        return scanResult;
    
    
    }

    public static Scanner getInput() {
        return input;
    }
    
    public static void setInput(Scanner input) {
        portScanner.input = input;
    }
    
    
}














