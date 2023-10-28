package com.networkscan.cis18;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.Scanner;

public class PingDecorator{
    static {
        String libraryName = "pingjni";
        try {
            String libName = "native/" + System.mapLibraryName(libraryName);
            InputStream is = ClassLoader.getSystemResourceAsStream(libName);
            /* Alternate ways to load files from a jar file.
             * Method 1:
             * ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
             * InputStream is = classLoader.getResourceAsStream(libName);
             * Method 2:
             * InputStream inputStream = YourClass.class.getResourceAsStream("/"+libName);
             */
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

    private String[] pingResults;
    public PingDecorator() {
        this.pingResults = new String[10];  // I don't expect any more than 10 results.
    }

    private native void pingHost(String host, String[] results);

    private void addPingResolution() {
        // Get user input for ping
        System.out.println("Enter address to ping");
        Scanner input = new Scanner(System.in);
        String PingAddress;
        PingAddress = input.nextLine();
        //System.out.println(this.host.getDomainName());
        System.out.println("Adding Host Ping capability...");
        this.pingHost(PingAddress , this.pingResults);
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