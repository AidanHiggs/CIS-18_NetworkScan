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

    public static PcapHandle setPcapNetworkInterface() throws PcapNativeException, IOException, NotOpenException {
        List<String> networkInterfacesNames = new ArrayList<>();
        Enumeration<NetworkInterface> interfaces = NetworkInterface.getNetworkInterfaces();
        while (interfaces.hasMoreElements()) {
            NetworkInterface ni = interfaces.nextElement();
            if (ni.isUp() && !ni.isLoopback()) {
                networkInterfacesNames.add(ni.getName());
            }
        }

        for (String networkInterfaceName : networkInterfacesNames) {
            if (canReachInternet(networkInterfaceName)) {
                networkInt = new PcapHandle.Builder(networkInterfaceName).timeoutMillis(500).build() ;
                networkInt.setFilter("tcp", BpfProgram.BpfCompileMode.OPTIMIZE);
                setNetworkInterface(networkInterfaceName);
                return networkInt;
            }
        }
        throw new IOException("No suitable network interface found.");
    }

    private static boolean canReachInternet(String networkInterfaceName) throws PcapNativeException, NotOpenException, IOException {
        try (PcapHandle handle = new PcapHandle.Builder(networkInterfaceName).build()) {
            handle.setFilter("tcp", BpfProgram.BpfCompileMode.OPTIMIZE);
            Packet packet = handle.getNextPacket();
            if (packet instanceof TcpPacket) {
                TcpPacket tcpPacket = (TcpPacket) packet;
                return tcpPacket.getHeader().getSyn();
            }
        }
        return false;
    }

    private static void setNetworkInterface(String networkInterfaceName) throws IOException {
        networkInterface = NetworkInterface.getByName(networkInterfaceName);
    }

    public Object getNetworkHandle() {
        return null;
    }
}
