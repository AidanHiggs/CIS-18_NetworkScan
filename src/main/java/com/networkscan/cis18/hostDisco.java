package com.networkscan.cis18;
import java.io.IOException;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

import org.pcap4j.core.NotOpenException;
import org.pcap4j.core.PcapHandle;
import org.pcap4j.core.PcapNativeException;
import org.pcap4j.core.PcapNetworkInterface;
import org.pcap4j.packet.Packet;
import org.pcap4j.packet.TcpPacket;
import org.pcap4j.packet.namednumber.TcpPort;
import org.pcap4j.util.NifSelector;
import javax.swing.JOptionPane;
import javax.swing.JTextArea;
public class hostDisco extends NetworkScannerGUI {

    private static int calculateDevices() throws NotOpenException {
        System.out.println("calculating devices");
        
        int subnetBits = new setSubnet().getSubnet();
        System.out.println(subnetBits);
        if (subnetBits == 0) {
            throw new IllegalArgumentException("Subnet is empty");
        }
        int hostBits = 32 - subnetBits;
        int totalHosts = (int) Math.pow(2, hostBits) - 2;
        System.out.println(totalHosts);
        return totalHosts;
    }
    private static List<String> tcpSweep() throws Exception {
        int totalHosts = calculateDevices();
        setNet.setPcapNetworkInterface();
        NetworkInterface networkInterface = setNet.networkInterface;
        PcapHandle networkInt = setNet.networkInt;
        ArrayList<InetAddress> hostAddresses = generateHostAddresses(networkInt, totalHosts);
        List<String>discoveredHosts = new ArrayList<>();
        for (InetAddress hostAddr : hostAddresses) {
            System.out.println("scannning");
            TcpPacket.Builder tcpPacketBuilder = new TcpPacket.Builder();
            tcpPacketBuilder
                    .srcPort(TcpPort.getInstance((short) 1234))
                    .dstPort(TcpPort.HTTP)
                    .dstAddr(hostAddr)
                    .syn(true);

            TcpPacket tcpPacket = tcpPacketBuilder.build();
            try {
                networkInt.sendPacket(tcpPacket);
                System.out.println("!!!sent!!!");
                Packet responsePacket = networkInt.getNextPacketEx(); 
                System.out.println("!!!got!!!");
                String synPacket = responsePacket.toString();
                discoveredHosts.add(hostAddr.getHostAddress());
            } catch (Exception e) {
                e.printStackTrace();
                throw e;
            } finally {
                networkInt.close();
            }
        }
        for (String ip : discoveredHosts) {
            System.out.println("Discovered IP: " + ip);
        }
    
        // Return the discovered IPs
        return discoveredHosts;
    }
    

    private static ArrayList<InetAddress> generateHostAddresses(PcapHandle networkInt, int totalHosts) throws Exception {
        ArrayList<InetAddress> hostAddresses = new ArrayList<>();
    
    
        String ipAddressString = new getip().getIpAddress(); // Get the IP address string from the ipAddressField
    
        InetAddress ipAddress = InetAddress.getByName(ipAddressString);
    
        byte[] ipAddressBytes = ipAddress.getAddress();
    
        for (int i = 1; i <= totalHosts; i++) {
            ipAddressBytes[3] = (byte) i;
            InetAddress hostAddress = InetAddress.getByAddress(ipAddressBytes);
            hostAddresses.add(hostAddress);
        }
    
        return hostAddresses;
    }
 
    public void main(String selectedScanMethod) throws Exception {
        System.out.println("Made IT INTO DISCO");
        if (selectedScanMethod.equals("host discovery")) {
            List<String> discoveredHosts = tcpSweep();
            StringBuilder sb = new StringBuilder();
            for (String ip : discoveredHosts) {
                sb.append("Discovered IP: ").append(ip).append("\n");
            }
            resultArea.setText(sb.toString());
        }
    }
}