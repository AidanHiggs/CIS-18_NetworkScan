package com.networkscan.cis18;

import java.io.IOException;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeoutException;

import org.pcap4j.core.NotOpenException;
import org.pcap4j.core.PcapHandle;
import org.pcap4j.core.PcapNativeException;
import org.pcap4j.packet.Packet;
import org.pcap4j.packet.TcpPacket;
import org.pcap4j.packet.namednumber.TcpPort;

public class hostDisco {
    private NetworkScannerModel model;
    private setNet networkSettings;

    public hostDisco(NetworkScannerModel model, setNet networkSettings) {
        this.model = model;
        this.networkSettings = networkSettings;
    }

    public void setNetworkSettings() throws PcapNativeException, IOException, NotOpenException {
        // Using setNet to initialize network settings and PcapHandle
        networkSettings.setPcapNetworkInterface();
    }

    public List<String> tcpSweep() throws Exception {
        int totalHosts = calculateDevices();
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

    public int calculateDevices() throws IllegalArgumentException, PcapNativeException {
        int subnetBits = Integer.parseInt(model.getSubnetMask());
        if (subnetBits <= 0) {
            throw new IllegalArgumentException("Subnet is empty or invalid");
        }
        int hostBits = 32 - subnetBits;
        return (int) Math.pow(2, hostBits) - 2;
    }

    public boolean sendTcpPacketAndCheckResponse(InetAddress hostAddr, PcapHandle networkInt) 
            throws PcapNativeException, IOException, NotOpenException {
        TcpPacket.Builder tcpPacketBuilder = new TcpPacket.Builder();
        tcpPacketBuilder.srcPort(TcpPort.getInstance((short) 1234))
                        .dstPort(TcpPort.HTTP)
                        .dstAddr(hostAddr)
                        .syn(true);

        TcpPacket tcpPacket = tcpPacketBuilder.build();
        networkInt.sendPacket(tcpPacket);
        try {
            Packet response = networkInt.getNextPacketEx();
            return response != null;
        } catch (NotOpenException | TimeoutException e) {
            e.printStackTrace();
            return false;
        }
    }

    public ArrayList<InetAddress> generateHostAddresses(PcapHandle networkInt, int totalHosts) throws IOException {
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
