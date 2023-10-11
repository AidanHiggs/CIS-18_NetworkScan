package com.networkscan.cis18;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import org.pcap4j.core.PcapHandle;
import org.pcap4j.core.PcapNativeException;
import org.pcap4j.packet.Packet;
import org.pcap4j.packet.TcpPacket;
import org.pcap4j.packet.namednumber.TcpPort;
import java.net.SocketException;
import java.net.UnknownHostException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
public class osDetectionDecorator {
    public static PcapHandle networkInt = null;
    
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
                      .dstAddr(InetAddress.getByName("192.168.1.176"))
                      .syn(true);
          } catch (UnknownHostException e) {
              // Handle exception
              e.printStackTrace();
              throw e;
          }
          
          TcpPacket tcpPacket = tcpPacketBuilder.build();
          PcapHandle handle = networkInt;
          try {
              handle.sendPacket(tcpPacket);
              Packet responsePacket = handle.getNextPacket();
              String synPacket = responsePacket.toString();
              return synPacket;
          } catch (Exception e) {
              // Handle exception
              e.printStackTrace();
              throw e;
          } finally {
              handle.close();
          }
      }

      //public static void 
    
    public static void main(String[] args) throws Exception {
        osDetectionDecorator loadFingerprints = new osDetectionDecorator();
        loadFingerprints.loadFingerprints();
        setNetworkInterface();
        tcpSyn();
        
    }

    private static void setNetworkInterface() {
        List<String> networkInterfaces = new ArrayList<String>();
        try {
            Enumeration<NetworkInterface> interfaces = NetworkInterface.getNetworkInterfaces();
            while (interfaces.hasMoreElements()) {
                NetworkInterface ni = interfaces.nextElement();
                networkInterfaces.add(ni.getName());
            }
            for (String networkInterface : networkInterfaces) {
                if (networkInterface.matches("^(wlan|eth).*")) {
                    networkInt = new PcapHandle.Builder(networkInterface).build();
                    break;
                }
            }
        } catch (SocketException | PcapNativeException e) {
            e.printStackTrace();
        }
    }
  
}