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
    try (BufferedReader br = new BufferedReader(new FileReader(filename))) { // Open the file for reading
        String line;
        while ((line = br.readLine()) != null) { // Read each line from the file
            String[] parts = line.split(" "); // Split the line by spaces
            if (parts.length >= 3) { // Check if there are at least 3 parts
                String key = parts[0] + "-" + parts[1]; // Create a key by concatenating the first two parts
                String service = ""; // Initialize an empty string for the service
                for (int i = 2; i < parts.length; i++) { // Iterate over the remaining parts
                    service += parts[i] + " "; // Concatenate each part with a space
                }
                service = service.trim(); // Remove leading and trailing spaces from the service
                portServicesMap.put(key, service); // Add the key-value pair to the map
            }
        }
    } catch (IOException e) { // Catch any IOException that occurs during file reading
        e.printStackTrace(); // Print the stack trace for debugging
    }
}
    

    public static String getService(int port, String protocol) {
        return portServicesMap.getOrDefault(port + "-" + protocol, "Unknown");
    }


    // Method to scan the IP address for open ports
    // Method to scan the IP address for open ports
public static void scanIp(int startPort, int endPort, String ipAddr, JTextArea resultArea) {
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
                    socket.connect(new InetSocketAddress(ipAddr, port), 5000);
                    // Specify the protocol (tcp or udp)
                    String protocol = "tcp";
                    // Create a key to identify the service based on the port and protocol
                    String key = port + "-" + protocol;
                    // Get the service associated with the port and protocol
                    String service = getService(port, protocol);
                    // If the service is unknown, print a message
                    if (service.equals("Unknown")) {
                        System.out.println("Service not found for key: " + key);
                    }
                    // Append the open port and service to the scan results
                    sb.append("Port " + port + " is open - " + service + "\n");
                    // Close the socket
                    socket.close();
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
                // Update the UI with the scan results using the Event Dispatch Thread
                SwingUtilities.invokeLater(() -> resultArea.append(scanResult));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    };

    // Execute the SwingWorker to start the scanning task
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
