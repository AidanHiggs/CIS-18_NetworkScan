import java.io.*;
import java.net.*; 
import java.util.Scanner;

public class portScanner {

    public static void main(String[] args) {
        portScanner scanner = new portScanner();
        String ip = scanner.getIp();
        scanner.pingEr(ip);
    }

    public String getIp() {
        Scanner input = new Scanner(System.in);
        System.out.println("Enter Ip address");
        String ipAddr = input.nextLine();
        input.close();
        return ipAddr;
    }

    public void pingEr(String ipAddr) { 
        System.out.println(ipAddr);
    } 
}
