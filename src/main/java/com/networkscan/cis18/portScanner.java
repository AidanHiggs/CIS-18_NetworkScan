package com.networkscan.cis18;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

import javax.swing.JTextArea;
import javax.swing.SwingUtilities;
import javax.swing.SwingWorker;


public class portScanner {
    static Scanner input = new Scanner(System.in);
    static Map<String, String> portServicesMap = new HashMap<>();

    public static void main(String[] args) {
        loadPortServices("src/main/java/com/networkscan/cis18/ports2.txt");
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
    public static void loadPortServices(String filename) {
        try (BufferedReader br = new BufferedReader(new FileReader(filename))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(" ");
                if (parts.length >= 3) {
                    String key = parts[0] + "-" + parts[1]; // e.g., "22-tcp"
                    String service = "";
                    for (int i = 2; i < parts.length; i++) {
                        service += parts[i] + " ";
                    }
                    service = service.trim(); 
                    portServicesMap.put(key, service);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    

    public static String getService(int port, String protocol) {
        return portServicesMap.getOrDefault(port + "-" + protocol, "Unknown");
    }


    // Method to scan the IP address for open ports
    public static void scanIp(int startPort, int endPort, String ipAddr, JTextArea resultArea) {
        SwingWorker<String, Void> worker = new SwingWorker<String, Void>() {
            @Override
            protected String doInBackground() throws Exception {
                StringBuilder sb = new StringBuilder();
                for (int port = startPort; port <= endPort; port++) {
                    try {
                        Socket socket = new Socket();
                        socket.connect(new InetSocketAddress(ipAddr, port), 5000);
                        String protocol = "tcp"; // You can change this to "udp" if needed
                        String key = port + "-" + protocol;
                        String service = getService(port, protocol);
                        if (service.equals("Unknown")) {
                            System.out.println("Service not found for key: " + key);
                        }
                        sb.append("Port " + port + " is open - " + service + "\n");
                        socket.close();
                    } catch (IOException e) {
                        // Ignore exceptions for closed ports
                    }
                }
                return sb.toString();
            }
    
            @Override
            protected void done() {
                try {
                    String scanResult = get();
                    SwingUtilities.invokeLater(() -> resultArea.append(scanResult));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
    
        };
    
        worker.execute();
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
}
