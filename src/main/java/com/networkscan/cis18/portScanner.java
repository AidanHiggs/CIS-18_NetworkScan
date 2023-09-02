package com.networkscan.cis18;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.Scanner;

public class portScanner {
    Scanner input = new Scanner(System.in);

    public static void main(String[] args) {
        getInputs();
    }

    public static String getIpAddress() {
        Scanner input = new Scanner(System.in);
        System.out.println("Enter IP address");
        String ipAddr = input.nextLine();
        return ipAddr;
    }

    public static int getStartPort() {
        Scanner input = new Scanner(System.in);
        int startPort = input.nextInt();
        return startPort;
    }

    public static int getStartPort(Scanner input) {
    System.out.println("Enter the start port");
    int startPort = input.nextInt();
    return startPort;
}

public static int getEndPort(Scanner input) {
    System.out.println("Enter the end port");
    int endPort = input.nextInt();
    return endPort;
}

   public static void scanIp(int startPort, int endPort, String ipAddr) {
       for (int port = startPort; port <= endPort; port++) {
           try {
               Socket socket = new Socket();
               socket.connect(new InetSocketAddress(ipAddr, port), 1000);
               System.out.println("Port " + port + " is open");
               socket.close();
           } catch (IOException e) {
               
           }
       }
   }
   
    public static String resolveHostname(String host) {
        int retries  = 0;
        int maxRetries = 3;
        while (retries < maxRetries) {
          try{
            InetAddress adress = InetAddress.getByName(host);
            String hostIp = adress.getHostAddress();
            return hostIp;
        }catch(Exception e){
            System.out.println("The hostname" + host + " could not be resolved, please try again");
            getInputs();
            maxRetries++;

        }  
        System.out.println("Maximim number of failures reached, exiting...");
        return null;
        }
        return host;
    }
   public static void getInputs() {
    Scanner input = new Scanner(System.in);
    System.out.println("Enter the IP or Hostname of the target");
    String host = input.next();
    int startPort = getStartPort(input);
    int endPort = getEndPort(input);
    input.close();

    if (host.matches("\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}")) {
        scanIp(startPort, endPort, host);
    } else {
        String ipAddr = resolveHostname(host);
        scanIp(startPort, endPort, ipAddr);
    }
}
    public void closeInput() {
        input.close();
    }
}


