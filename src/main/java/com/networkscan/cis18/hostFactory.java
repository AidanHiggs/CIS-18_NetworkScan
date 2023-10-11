package com.networkscan.cis18;

public class hostFactory {
<<<<<<< HEAD
<<<<<<< HEAD
    private static int hostCount = 0;

    public static hostImpl createHost(String ipAddress) {
        hostCount++;
        hostImpl host = new hostImpl();
=======
    public static volatile int id = 0;
    public static host createHost(String ipAddress) {
        host host = new hostImpl();
>>>>>>> e362fa1 (	modified:   src/main/java/com/networkscan/cis18/Main.java)
=======
    public static volatile int id = 0;
    public static host createHost(String ipAddress) {
        host host = new hostImpl();
=======
    private static int hostCount = 0;

    public static hostImpl createHost(String ipAddress) {
        hostCount++;
        hostImpl host = new hostImpl();
>>>>>>> 5fb6d38 (	modified:   src/main/java/com/networkscan/cis18/Main.java)
>>>>>>> 45d4d14 (	modified:   src/main/java/com/networkscan/cis18/Main.java)
        host.setIpAddress(ipAddress);
        host.setId(id++);
        return host;
    }
    public static int getHostCount() {
        return hostCount;
    }
}