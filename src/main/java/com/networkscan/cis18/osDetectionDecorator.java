package com.networkscan.cis18;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class osDetectionDecorator {
    public void loadFingerprintsFromFile() {
        List<String> fingerprints = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new FileReader("src/main/resources/com/networkscan/cis18/svn.nmap.org_nmap_nmap-os-db.txt"))) {
            String line;
            while ((line = br.readLine()) != null) {
                fingerprints.add(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        
        int numberOfLines = fingerprints.size();
        System.out.println("Number of lines: " + numberOfLines);
        }
    

      public void sendPackets() {
          
      }
    }
