/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package networkscanner;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
/**
 *
 * @author Isa
 */
public class NetworkScanner implements Runnable{
    
    static private List<Device> connected_devices = new ArrayList<>(); 
    static private List<Integer> open_ports = new ArrayList<>(); 
    
    static String state = "ipscanner"; 
    static String ip_to_scan; 
        
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
            Ping("192.168.0."+String.valueOf(lastBytesOfIp));
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
        
    public void ScanPureJava(){
        String ipAddress = "192.168.0.";
        ipAddress = ipAddress.substring(0, ipAddress.lastIndexOf('.')) + ".";
        for (int i = 0; i < 256; i++) {
            String otherAddress = ipAddress + String.valueOf(i);
            try {
                if (InetAddress.getByName(otherAddress).isReachable(50)) {
                    System.out.println(otherAddress);
                }
            } catch (UnknownHostException e) {
                System.out.println(e.getMessage()); 
            } catch (IOException e) {
                System.out.println(e.getMessage()); 
            }
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
       
        public Boolean HasNoneOpenPorts(){
            return open_ports.isEmpty(); 
        }
        
        public List<Integer> getOpenPorts(){
            return open_ports; 
        }
        
    /**
     * @param args the command line arguments
     */
    
    
}
