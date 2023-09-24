import csv

input_file_path = '/home/flabbydino/Desktop/fall 2023/CIS-18_NetworkScan/src/main/resources/com/networkscan/cis18/svn.nmap.org_nmap_nmap-os-db.txt'
output_file_path = '/home/flabbydino/Desktop/fall 2023/CIS-18_NetworkScan/src/main/resources/com/networkscan/cis18/output.csv'

# Open the output CSV file for writing
with open(output_file_path, 'w', newline='') as csvfile:
    # Define the fieldnames for the CSV file
    fieldnames = ['os_class', 'os_name', 'features']
    writer = csv.DictWriter(csvfile, fieldnames=fieldnames)
    
    # Write the header to the CSV file
    writer.writeheader()
    
    # Open the input file for reading
    with open(input_file_path, 'r', errors='ignore') as file:
        # Initialize variables to hold the extracted information
        os_class = ''
        os_name = ''
        features = []
        
        for line in file:
            line = line.strip()
            
            # Skip comments and empty lines
            if line.startswith('#') or not line:
                continue
            
            # Extract information from Class line
            if line.startswith('Class'):
                os_class = line
                os_name = line.split('|')[-1].strip()  # Extracting OS name from the Class line
            
            # Extract information from other lines as features
            elif line.startswith(('SEQ', 'OPS', 'WIN', 'ECN', 'T1', 'T2', 'T3', 'T4', 'T5', 'T6', 'T7', 'U1', 'IE')):
                features.append(line)
            
            # Write the extracted information to the CSV file at the end of each fingerprint block
            elif line.startswith('Fingerprint'):
                if os_class and os_name and features:
                    writer.writerow({'os_class': os_class, 'os_name': os_name, 'features': '|'.join(features)})
                # Reset variables for the next fingerprint block
                os_class = ''
                os_name = ''
                features = []

    # Write the last fingerprint block to the CSV file
    if os_class and os_name and features:
        writer.writerow({'os_class': os_class, 'os_name': os_name, 'features': '|'.join(features)})