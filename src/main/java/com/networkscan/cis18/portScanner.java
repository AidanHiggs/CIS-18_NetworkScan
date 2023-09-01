package com.networkscan.cis18;

import java.io.*;
import java.net.*; 
import java.util.Scanner;

public class PortScanner {

    public static void main(String[] args) {
        PortScanner scanner = new PortScanner();
        String ip = scanner.getIpAddress();
        scanner.scanIp(ip);
    }

    public String getIpAddress() {
        Scanner input = new Scanner(System.in);
        System.out.println("Enter IP address");
        String ipAddr = input.nextLine();
        input.close();
        return ipAddr;
    }

    public void scanIp(String ipAddr) {
        // TODO: Implement port scanning logic here
    }
}