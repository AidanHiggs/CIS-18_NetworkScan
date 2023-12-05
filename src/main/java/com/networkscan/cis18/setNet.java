package com.networkscan.cis18;

import java.io.IOException;
import java.net.NetworkInterface;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

import org.pcap4j.core.BpfProgram;
import org.pcap4j.core.NotOpenException;
import org.pcap4j.core.PcapHandle;
import org.pcap4j.core.PcapNativeException;
import org.pcap4j.packet.Packet;
import org.pcap4j.packet.TcpPacket;

public class setNet {
    public static PcapHandle networkInt = null;
    public static NetworkInterface networkInterface = null;
    public static PcapHandle setPcapNetworkInterface() throws NotOpenException {
        List<String> networkInterf = new ArrayList<>();
        List<NetworkInterface> networkInterfaces = new ArrayList<>();
        try {
            Enumeration<NetworkInterface> interfaces = NetworkInterface.getNetworkInterfaces();
            while (interfaces.hasMoreElements()) {
                NetworkInterface ni = interfaces.nextElement();
                if (ni.isUp() && !ni.isLoopback()) {
                    networkInterfaces.add(ni);
                    networkInterf.add(ni.getName());
                }
            }
            for (String networkInterface : networkInterf) {
                if (canReachInternet(networkInterface)) {
                    networkInt = new PcapHandle.Builder(networkInterface).build();
                    networkInt.setFilter("tcp", BpfProgram.BpfCompileMode.OPTIMIZE);
                    setNetworkInterface(networkInterface);
                    return networkInt;
                
                }
            }
        } catch (PcapNativeException | IOException e) {
            e.printStackTrace();
        }

        return networkInt;
    }

    private static boolean canReachInternet(String networkInterface) {
        try {
            PcapHandle handle = new PcapHandle.Builder(networkInterface).build();
            handle.setFilter("tcp", BpfProgram.BpfCompileMode.OPTIMIZE);

            Packet packet = handle.getNextPacket();
            if (packet instanceof TcpPacket) {
                TcpPacket tcpPacket = (TcpPacket) packet;
                if (tcpPacket.getHeader().getSyn()) {
                    return true;
                }
            }

            handle.close();
        } catch (PcapNativeException | NotOpenException e) {
            e.printStackTrace();
        }

        return false;

        
    }
    private static void setNetworkInterface(String networkInterfaceName) {
        try {
            networkInterface = NetworkInterface.getByName(networkInterfaceName);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
