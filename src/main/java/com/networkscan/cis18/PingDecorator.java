package com.networkscan.cis18;
import java.util.Arrays;

public class PingDecorator{
    static {
        System.loadLibrary("pingjni"); // Load the shared library
    }

    private String[] pingResults;
    public PingDecorator() {
        //super(host);
        this.pingResults = new String[10];  // I don't expect any more than 10 results.
    }

    //@Override
    //public void connect() {
      //  super.connect();
    //    addPingResolution();
    //}



    private native void pingHost(String host, String[] results);

    private void addPingResolution() {
        //System.out.println(this.host.getDomainName());
        System.out.println("Adding Host Ping capability...");
        this.pingHost("74.6.143.25", this.pingResults);
        //if(Arrays.stream(this.pingResults).findAny().isPresent()) {
        //notifyWatchers();
        //}
        // Adding some printing just for fun.
        for (String result : this.pingResults) {
            if (result != null) {
                System.out.print("Ping Result: ");
                System.out.println(result);
            }
        }
    }

    public void printStuff() {
        addPingResolution();
    }
}