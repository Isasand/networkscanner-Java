/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package networkscanner;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Isa
 */
public class PortClient {
    
    int maxPorts = 1000; 
    List<Port> customPorts = new ArrayList<>(); 
    
    public PortClient(){
        
    }
    
    
    public void readPortsFromFile() throws FileNotFoundException, IOException{
        FileReader fileReader  = new FileReader("ports.txt");
        BufferedReader bufferedReader = new BufferedReader(fileReader);
        String line = null; 
        String name; 
        String port; 
        while((line = bufferedReader.readLine()) != null) {
            
            name = line.substring(line.lastIndexOf(':'));
            //customPorts.add()
        }
    }
    
    public void PrintPorts(){
       this.customPorts.forEach((port)->{
           System.out.println(port.getName()); 
    });
    }
    
    public static void main(String[] args){
        
    }
    
    public class Port{
        String name; 
        String info; 
        int port; 
        
        public Port(String name, int port){
            
        }
        
        public void setName(String name){
            this.name = name; 
        }
        
        public String getName(){
            return this.name; 
        }
        
        public void setInfo(String info){
            this.info = info; 
        }
        
        public String getInfo(){
            return this.info; 
        }
        
        public void setPort(int port){
            this.port = port; 
        }
        
        public int getPort(){
            return this.port; 
        }
    }
}
