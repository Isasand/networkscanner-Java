/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package networkscanner;

import com.jcabi.ssh.Shell;
import com.jcabi.ssh.SSH;
import com.jcabi.ssh.SSHByPassword; 
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.net.UnknownHostException;

/**
 *
 * @author Isa
 */
public class SSHClient {
    public static Shell shell;
    public static String key;  
    
    public static void ReadCertificate() throws FileNotFoundException, IOException{
        FileReader fileReader  = new FileReader("cert/id_rsa");
        BufferedReader bufferedReader = new BufferedReader(fileReader);
        String line = null; 
        while((line = bufferedReader.readLine()) != null) {
            key += line + "\n"; 
        }
      }
    
    public static void main(String[] args) throws UnknownHostException, IOException{
        ReadCertificate(); 
        
        shell = new SSH("192.168.0.114", 22, "pi", key); 
        String stdout = new Shell.Plain(shell).exec("ls");
        System.out.println(stdout);
    }
     
    public void addNewHost(String ip, int port, String usr, String psw) throws UnknownHostException{
        shell = new SSHByPassword(ip, port, usr, psw); 
    }
    
    public void Login(String ip, int port, String usr) throws UnknownHostException{
        shell = new SSH(ip, port, usr, key); 
    }
}
