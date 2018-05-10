/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package networkscanner;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/**
 *
 * @author Isa
 */
public class Device implements Comparator<Device>, Comparable<Device>{
    
    private List<Integer> ports = new ArrayList<>(); 
    private String ip; 
    
    public Device(){};
    
    public Device(String ip){
        this.ip = ip; 
    }
    
    public List<Integer> ports(){
        return this.ports; 
    }
    
    public String getIp(){
        return this.ip; 
    }
    
    public int getLastBytesOfIp(){
        return Integer.parseInt(this.ip.substring(this.ip.lastIndexOf('.')+1));
    }
    
    public int compareTo(Device d) {
      return (this.getIp()).compareTo(d.getIp());
    }
    
    public int compare(Device d1, Device d2){
        return d1.getLastBytesOfIp() - d2.getLastBytesOfIp(); 
    }
   
}
