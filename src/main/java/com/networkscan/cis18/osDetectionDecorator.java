package com.networkscan.cis18;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.List;
import org.pcap4j.core.PcapHandle;
import org.pcap4j.core.PcapNativeException;
import org.pcap4j.packet.Packet;
import org.pcap4j.packet.TcpPacket;
import org.pcap4j.packet.UnknownPacket;
import org.pcap4j.packet.namednumber.TcpPort;

public class osDetectionDecorator {
    public static PcapHandle networkInt = null;

    private static void parseFingerprints() {
       
    }

    public void loadFingerprints() {
        List<String> fingerprints = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new FileReader("src/main/resources/com/networkscan/cis18/svn.nmap.org_nmap_nmap-os-db.txt"))) {
            String line;
            while ((line = br.readLine()) != null) {
                fingerprints.add(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static String tcpSyn() throws Exception {
        TcpPacket.Builder tcpPacketBuilder = new TcpPacket.Builder();
        try {
            tcpPacketBuilder
                    .srcPort(TcpPort.getInstance((short) 1234))
                    .dstPort(TcpPort.HTTP)
                    .dstAddr(InetAddress.getByName("1.1.1.1"))
                    .syn(true);
        } catch (IOException e) {
            e.printStackTrace();
            throw e;
        }

        TcpPacket tcpPacket = tcpPacketBuilder.build();
        PcapHandle handle = networkInt;
        try {
            handle.sendPacket(tcpPacket);
            Packet responsePacket = handle.getNextPacketEx(); // Changed to getNextPacketEx() to handle exceptions
            String synPacket = responsePacket.toString();
            return synPacket;
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        } finally {
            handle.close();
        }
    }
public static List<TcpPacket> sendFragmentedPackets(
        TcpPort srcPort, TcpPort dstPort, InetAddress dstAddr, 
        boolean syn, PcapHandle networkInt, byte[] data, 
        int fragSize) throws Exception {
    
    List<TcpPacket> tcpPackets = new ArrayList<>();
    TcpPacket.Builder tcpPacketBuilder = new TcpPacket.Builder();
    
    for (int offset = 0; offset < data.length; offset += fragSize) {
        boolean isLastFragment = offset + fragSize >= data.length;
        int fragLength = isLastFragment ? data.length - offset : fragSize;
        byte[] fragData = Arrays.copyOfRange(data, offset, offset + fragLength);
        TcpPacket fragPacket = tcpPacketBuilder
                .srcPort(srcPort)
                .dstPort(dstPort)
                .dstAddr(dstAddr)
                .syn(syn)
                .payloadBuilder(
                        new UnknownPacket.Builder().rawData(fragData))
                .build();
        
        tcpPackets.add(fragPacket);
    }
    
    // Send all packets
    for (TcpPacket packet : tcpPackets) {
        networkInt.sendPacket(packet);
    }
    
    // Receive response packet
    Packet responsePacket = networkInt.getNextPacketEx(); // Changed to getNextPacketEx() to handle exceptions
    String synPacket = responsePacket.toString();
    networkInt.close();
    
    return tcpPackets;
}


        
       

    

    public static String christmasTreePac() throws Exception {
        TcpPacket.Builder tcpPacketBuilder = new TcpPacket.Builder();
        try {
            tcpPacketBuilder
                    .srcPort(TcpPort.getInstance((short) 1234))
                    .dstPort(TcpPort.HTTP)
                    .dstAddr(InetAddress.getByName("127.0.0.1"))
                    .syn(true)
                    .fin(true)
                    .psh(true)
                    .urg(true);
        } catch (IOException e) {
            e.printStackTrace();
            throw e;
        }
        TcpPacket tcpPacket = tcpPacketBuilder.build();
        PcapHandle handle = networkInt;
        try {
            handle.sendPacket(tcpPacket);
            Packet responsePacket = handle.getNextPacketEx(); // Changed to getNextPacketEx() to handle exceptions
            return responsePacket.toString();
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        } finally {
            handle.close();
        }
    }

    public static void main(String[] args) throws Exception {
        osDetectionDecorator loadFingerprints = new osDetectionDecorator();

        loadFingerprints.loadFingerprints();
        setNetworkInterface();
        tcpSyn();
        christmasTreePac(); 
        List<TcpPacket> frag = sendFragmentedPackets(null, null, null, false, networkInt, null, 0);
        for (TcpPacket packet : frag) {
            System.out.println(packet.toString());
        }
    }

private static void setNetworkInterface() {
    List<String> networkInterfaces = new ArrayList<>();
    try {
        Enumeration<NetworkInterface> interfaces = NetworkInterface.getNetworkInterfaces();
        while (interfaces.hasMoreElements()) {
            NetworkInterface ni = interfaces.nextElement();
            if (ni.isUp() && !ni.isLoopback()) {
                networkInterfaces.add(ni.getName());
            }
        }
        for (String networkInterface : networkInterfaces) {
            if (canReachInternet(networkInterface)) {
                networkInt = new PcapHandle.Builder(networkInterface).build();
                break;
            }
        }
    } catch (PcapNativeException | IOException e) {
        e.printStackTrace();
    }
}

private static boolean canReachInternet(String networkInterface) {
    
    return false; // Placeholder return value; modify as needed
}
   
}
