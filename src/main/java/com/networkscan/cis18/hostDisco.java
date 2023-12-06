package com.networkscan.cis18;

import java.io.IOException;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeoutException;

import org.pcap4j.*;
import org.pcap4j.core.NotOpenException;
import org.pcap4j.core.PcapHandle;
import org.pcap4j.core.PcapNativeException;
import org.pcap4j.packet.Packet;
import org.pcap4j.packet.TcpPacket;
import org.pcap4j.packet.namednumber.TcpPort;
import org.pcap4j.core.PcapHandle;
import org.pcap4j.core.BpfProgram;

public class hostDisco {
    private NetworkScannerModel model;
    private setNet networkSettings;

    public hostDisco(NetworkScannerModel model, setNet networkSettings) {
        this.model = model;
        this.networkSettings = networkSettings;
    }

    public List<String> tcpSweep() throws Exception {
        int totalHosts = calculateDevices();
        networkSettings.setPcapNetworkInterface();
        PcapHandle networkInt = (PcapHandle) networkSettings.getNetworkHandle();
        ArrayList<InetAddress> hostAddresses = generateHostAddresses(networkInt, totalHosts);
        List<String> discoveredHosts = new ArrayList<>();

        for (InetAddress hostAddr : hostAddresses) {
            if (sendTcpPacketAndCheckResponse(hostAddr, networkInt)) {
                discoveredHosts.add(hostAddr.getHostAddress());
            }
        }

        networkInt.close(); // Ensure the handle is closed after use
        return discoveredHosts;
    }

    private int calculateDevices() throws IllegalArgumentException, PcapNativeException {
        int subnetBits = Integer.parseInt(model.getSubnetMask());
        if (subnetBits <= 0) {
            throw new IllegalArgumentException("Subnet is empty or invalid");
        }
        int hostBits = 32 - subnetBits;
        return (int) Math.pow(2, hostBits) - 2;
    }

    private boolean sendTcpPacketAndCheckResponse(InetAddress hostAddr, PcapHandle networkInt) 
            throws PcapNativeException, IOException, NotOpenException, TimeoutException, InterruptedException {
        TcpPacket.Builder tcpPacketBuilder = new TcpPacket.Builder();
        tcpPacketBuilder.srcPort(TcpPort.getInstance((short) 1234))
                        .dstPort(TcpPort.HTTP)
                        .dstAddr(hostAddr)
                        .syn(true);

        TcpPacket tcpPacket = tcpPacketBuilder.build();
        networkInt.sendPacket(tcpPacket);
        int pcap_set_timeout = 500;       
        Packet response = networkInt.getNextPacketEx(); // Get the next packet
        return response != null;
    }

    private ArrayList<InetAddress> generateHostAddresses(PcapHandle networkInt, int totalHosts) throws IOException {
        ArrayList<InetAddress> hostAddresses = new ArrayList<>();
        String ipAddressString = model.getIpAddress();
        InetAddress ipAddress = InetAddress.getByName(ipAddressString);
        byte[] ipAddressBytes = ipAddress.getAddress();

        for (int i = 1; i <= totalHosts; i++) {
            ipAddressBytes[3] = (byte) i;
            InetAddress hostAddress = InetAddress.getByAddress(ipAddressBytes);
            hostAddresses.add(hostAddress);
        }

        return hostAddresses;
    }
}
