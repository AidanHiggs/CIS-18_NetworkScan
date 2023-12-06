package com.networkscan.cis18;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Scanner;

public class PingDecorator extends portScanner {
    private portScanner scanner;
    private String[] pingResults;

    static {
        loadNativeLibrary("pingjni");
    }

    public PingDecorator(portScanner scanner) {
        this.scanner = scanner;
        this.pingResults = new String[10]; // we can add a slectionbox tochange that number of ping results if we want
    }

    private static void loadNativeLibrary(String libraryName) {
        try {
            String libName = "native/" + System.mapLibraryName(libraryName);
            InputStream is = ClassLoader.getSystemResourceAsStream(libName);
            if (is == null) {
                throw new UnsatisfiedLinkError("Library not found on classpath: " + libraryName);
            }
            File tempFile = File.createTempFile(libraryName, "." + System.mapLibraryName(libraryName));
            tempFile.deleteOnExit();

            try (FileOutputStream out = new FileOutputStream(tempFile)) {
                byte[] buffer = new byte[8192];
                int bytesRead;
                while ((bytesRead = is.read(buffer)) != -1) {
                    out.write(buffer, 0, bytesRead);
                }
            }
            System.load(tempFile.getAbsolutePath());
        } catch (IOException e) {
            throw new UnsatisfiedLinkError("Failed to load library: " + e.getMessage());
        }
    }

    private native void pingHost(String host, String[] results);

    public void addPingResolution(String pingAddress) {
        System.out.println("Adding Host Ping capability...");
        pingHost(pingAddress, this.pingResults);
        for (String result : this.pingResults) {
            if (result != null) {
                System.out.println("Ping Result: " + result);
            }
        }
    }

    @Override
    public String scanIp(String host, int startPort, int endPort) {
        // Perform regular port scanning
        String scanResult = scanner.scanIp(host, startPort, endPort);

        // Add ping resolution
        addPingResolution(host);

        return scanResult;
    }
}
