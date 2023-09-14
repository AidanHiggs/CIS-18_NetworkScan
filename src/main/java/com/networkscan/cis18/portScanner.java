package com.networkscan.cis18;
import com.networkscan.cis18.NetworkScannerGUI;
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
        //loadPortServices("src/main/java/com/networkscan/cis18/ports.txt");
        try {
            loadPortServices(Paths.get(portScanner.class.getResource("ports.txt").toURI()).toFile());
            scanIp(22,  25, "23.185.0.3", null);
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
    }

    // Method to get the IP address from the user
    public static String getIpAddress() {
        Scanner input = new Scanner(System.in);
        System.out.println("Enter IP address");
        String ipAddr = input.nextLine();
        return ipAddr;
    }

    // Method to get the start port from the user
    public static int getStartPort(Scanner input) {
        System.out.println("Enter the start port");
        int startPort = input.nextInt();
        return startPort;
    }

    // Method to get the end port from the user
    public static int getEndPort(Scanner input) {
        System.out.println("Enter the end port");
        int endPort = input.nextInt();
        return endPort;
    }

/**
 * Loads port services from a file and populates the portServicesMap.
 *
 * @param  filename  the name of the file containing the port services
 */
public static void loadPortServices(File filename) {
    //System.out.printf("File: %s%n", filename.toString());
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
public static void scanIp(int startPort, int endPort, String ipAddr, JTextArea resultArea) {
    // Create a SwingWorker to perform the scanning task asynchronously
    System.out.println("I am alive!");
    SwingWorker<String, Void> worker = new SwingWorker<String, Void>() {
        // Override the doInBackground method to perform the scanning task
        @Override
        protected String doInBackground() throws Exception {
            System.out.println("I am alive!");
            // Create a StringBuilder to store the scan results
            StringBuilder sb = new StringBuilder();
            // Iterate through the range of ports to scan
            for (int port = startPort; port <= endPort; port++) {
                System.out.printf("Port: %d%n", port);
                try {
                    // Create a socket and connect to the IP address and port
                    Socket socket = new Socket();
                    socket.connect(new InetSocketAddress(ipAddr, port), 5000);
                    // Create a key to identify the service based on the port and protocol
                    int key = port;
                    // Get the service associated with the port and protocol
                    Service service = getService(port);
                    System.out.println(service.toString());
                    // If the service is unknown, print a message
                    if (service.getServiceName().equals("Unknown")) {
                        System.out.println("Service not found for key: " + key);
                    }
                    // Append the open port and service to the scan results
                    sb.append("Port " + port + " is open - " + service.toString() + "\n");
                    // Close the socket
                    socket.close();
                } catch (IOException e) {
                    // Ignore exceptions for closed ports
                    System.out.println(e.getMessage());
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
                // Update the UI with the scan results using the Event Dispatch Thread
                //SwingUtilities.invokeLater(() -> resultArea.append(scanResult));
                SwingUtilities.invokeLater(() -> System.out.println(scanResult));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    };

    // Execute the SwingWorker to start the scanning task
    //worker.execute();
    worker.run();
}


    // Method to resolve the hostname to an IP address
   public static String resolveHostname(String host, JTextArea resultArea) {
       StringBuilder sb = new StringBuilder();
       // Initialize a counter for retries
       int retries = 0;
       int maxRetries = 4;

       while (retries < maxRetries) {
           try { // Use InetAddress class to get the IP address of the hostname
               InetAddress address = InetAddress.getByName(host);
               String hostIp = address.getHostAddress();
               return hostIp;
           } catch (Exception e) {
               // If an exception occurs, inform the user
               sb.append("The hostname " + host + " could not be resolved, please try again");
               resultArea.append(sb.toString());
               // Increment the retry counter
               retries++;
           }
       }

       // If the maximum number of retries is reached, inform the user and return null
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
        int startPort;
        int endPort;
        if (scanMethodComboBox.getSelectedItem().equals("Port Scan")) {
            String startPortInput = JOptionPane.showInputDialog("Enter the start port");
            String endPortInput = JOptionPane.showInputDialog("Enter the end port");
    
            if (startPortInput == null || endPortInput == null) {
                // User canceled the input, return an empty string
                return "";
            }
    
            startPort = Integer.parseInt(startPortInput);
            endPort = Integer.parseInt(endPortInput);
        } else {
            startPort = getStartPort(null);
            endPort = getEndPort(null);;
        }
    
        String scanResult;
    
        if (host.matches("\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}")) {
            // If it's an IP address, directly call the scanIp method
            scanResult = "Scanning IP: " + host;
            scanIp(startPort, endPort, host, resultArea);
            
        } else {
            // If it's a hostname, resolve it to an IP address using the resolveHostname method
            String ipAddr = portScanner.resolveHostname(host, resultArea);
            scanResult = "Scanning hostname: " + host + " (resolved to IP: " + ipAddr + ")";
            scanIp(startPort, endPort, ipAddr, resultArea);
        }
        return scanResult;
    }
    
}
