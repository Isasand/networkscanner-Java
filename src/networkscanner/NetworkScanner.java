/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package networkscanner;

import it.sauronsoftware.ftp4j.FTPAbortedException;
import it.sauronsoftware.ftp4j.FTPClient;
import it.sauronsoftware.ftp4j.FTPDataTransferException;
import it.sauronsoftware.ftp4j.FTPException;
import it.sauronsoftware.ftp4j.FTPFile;
import it.sauronsoftware.ftp4j.FTPIllegalReplyException;
import it.sauronsoftware.ftp4j.FTPListParseException;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.InterfaceAddress;
import java.net.NetworkInterface;
import java.net.Socket;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Isa
 */
public class NetworkScanner implements Runnable{
    static public FTPClient client = new FTPClient();
    static private List<Device> connected_devices = new ArrayList<>(); 
    static private List<Integer> open_ports = new ArrayList<>(); 
    
    static String state = "ipscanner"; 
    static String ip_to_scan; 
    static int networkPrefixLenght; 
    static InetAddress localHost; 
    static String subnetMask; 
    static String ip_prefix_to_scan; 
    static String networkName; 
    
    int lastBytesOfIp; 
    int port; 
    
    
    
    public NetworkScanner(int var, String ip, int port){
        this.lastBytesOfIp = var; 
        this.ip_to_scan = ip; 
        this.port = port; 
    }
    
    public String getIpToScan(){
        return this.ip_to_scan;
    }
        
    public void setPortToScan(int port){
        this.port = port; 
    }
        
    public void setIpToScan(String ip){
        this.ip_to_scan = ip; 
    }
        
    public void ChangeState(String state){
        this.state = state; 
    }
        
    @Override
    public void run(){
        if (this.state == "ipscanner"){
            ScanPureJava(ip_prefix_to_scan);
        } 
        else if (this.state == "portscanner"){
            try { 
                ScanPort(this.ip_to_scan, this.port);
            } catch (UnknownHostException ex) {
                Logger.getLogger(NetworkScanner.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
        
    public void ClearConnectedDevices(){
        this.connected_devices.clear(); 
    }
        
    public void Ping(String ip){
        try{
            String s = null;
            List<String> commands = new ArrayList<String>();
               
            commands.add("ping"); 
            commands.add("-n");
            commands.add("1"); 
            commands.add("-w"); 
            commands.add("50"); 
            commands.add(ip);
            ProcessBuilder processbuilder = new ProcessBuilder(commands);
            Process process = processbuilder.start();
            BufferedReader stdInput = new BufferedReader(new InputStreamReader(process.getInputStream()));
            while ((s = stdInput.readLine()) != null){
                if (s.contains("Reply")){
                    Device dev = new Device(ip);
                    connected_devices.add(dev); 
                }
            }
        }
        catch (IOException e) {
            System.out.println("Error sending ping");
            System.out.println(e.getMessage()); 
        }
    }
        
    public void SortConnectedDevices(){
        Collections.sort(connected_devices, new Device()); 
    }
        
    public void ScanPureJava(String ip){
        String ipAddress = ip;
        ipAddress = ipAddress.substring(0, ipAddress.lastIndexOf('.')) + ".";
        String otherAddress = ipAddress + String.valueOf(lastBytesOfIp);
        try {
            if (InetAddress.getByName(otherAddress).isReachable(50)) {
                    System.out.println(otherAddress);
                    Device dev = new Device(otherAddress);
                    connected_devices.add(dev); 
            }
        } catch (UnknownHostException e) {
                System.out.println(e.getMessage()); 
        } catch (IOException e) {
            System.out.println(e.getMessage()); 
        }
    }
        
    public void PrintConnectedDevices(){
        connected_devices.forEach((d) -> {
            System.out.println(d.getIp());
    });
    }
        
    public List<Device> getConnectedDevices(){
        return connected_devices; 
    }
        
    public void ScanPort(String ip, int port) throws UnknownHostException{
        InetAddress ipAddress = InetAddress.getByName(ip);
            
        try {
            Socket s = new Socket(); 
            s.connect(new InetSocketAddress(ipAddress, port),50); 
            open_ports.add(port);
            System.out.println("Port " + port + " is open");
        } 
        catch (IOException ex) {
        }
            
    }
    
    public Boolean ScanSSH(String ip) throws UnknownHostException{
        
        try{
            ScanPort(ip, 22); 
            return true; 
        }
        catch(IOException ex){
            return false; 
        }
    }
       
    
    public void ScanPortsFromList(String ip, List<Integer> ports) throws UnknownHostException{
        for (Integer port: ports){
            ScanPort(ip, port); 
        }
        
    }
    
    public void ClearOpenPorts(){
        this.open_ports.clear(); 
    }
    
    public void ScanFTP(String ip) throws UnknownHostException, IOException, IllegalStateException, FTPIllegalReplyException{
       
        try { 
            client.connect(ip);
            //client.login("user", "password"); 
            System.out.println("found ftp server on " + ip); 
        } catch (FTPException ex) {
            Logger.getLogger(NetworkScanner.class.getName()).log(Level.SEVERE, null, ex);
            
        }
    }
    
   /* public void LoginFTP(String usr, String pass, String ip) throws IllegalStateException, IOException, FTPIllegalReplyException, FTPException{
        FTPClient client = new FTPClient();
        client.connect(ip);
        client.login(usr, ip);
        try {
            FTPFile[] list = client.list();
        } catch (FTPDataTransferException ex) {
            Logger.getLogger(NetworkScanner.class.getName()).log(Level.SEVERE, null, ex);
        } catch (FTPAbortedException ex) {
            Logger.getLogger(NetworkScanner.class.getName()).log(Level.SEVERE, null, ex);
        } catch (FTPListParseException ex) {
            Logger.getLogger(NetworkScanner.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    */
    
    public Boolean HasNoneOpenPorts(){
        return open_ports.isEmpty(); 
    }
        
    public List<Integer> getOpenPorts(){
        return open_ports; 
    }
    
    public void InitNetwork() throws SocketException, UnknownHostException{
        //read localhost into variable 
        localHost = Inet4Address.getLocalHost(); 
        NetworkInterface networkInterface = NetworkInterface.getByInetAddress(localHost); 
        networkPrefixLenght = networkInterface.getInterfaceAddresses().get(0).getNetworkPrefixLength(); 
        if (networkPrefixLenght == 24){
            subnetMask = "255.255.255"; 
        }
        else{
            System.out.println("too many IPs to scan"); 
        }
        StripLocalHost(); 
    }
    
    public String getSubnetMask(){
        return subnetMask; 
    }
    
    public InetAddress getLocalHost(){
        return localHost; 
    }
    
    public void StripLocalHost() throws UnknownHostException{
         String ipAddress  = localHost.getHostAddress();
         ipAddress = ipAddress.substring(0, ipAddress.lastIndexOf('.')) + ".";
         ip_prefix_to_scan = ipAddress; 
    }
    
    public static void main(String[] args) throws IOException{
        
    }
}
