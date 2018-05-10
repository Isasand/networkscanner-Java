/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package networkscanner;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import networkscanner.NetworkScanner; 
import networkscanner.Device; 
/**
 *
 * @author Isa
 */
public class ScannerGUI extends javax.swing.JFrame {

    /**
     * Creates new form ScannerGUI
     */
    
    
    public ScannerGUI() {
        initComponents();
    }
    
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        scanButton = new javax.swing.JButton();
        hostCombobox = new javax.swing.JComboBox<>();
        portScanButton = new javax.swing.JButton();
        jScrollPane3 = new javax.swing.JScrollPane();
        connectedDevicesArea = new javax.swing.JTextArea();
        jScrollPane4 = new javax.swing.JScrollPane();
        openPortsArea = new javax.swing.JTextArea();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Network scanner");

        scanButton.setText("SCAN NETWORK");
        scanButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                scanButtonActionPerformed(evt);
            }
        });

        hostCombobox.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "CHOOSE HOST" }));
        hostCombobox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                hostComboboxActionPerformed(evt);
            }
        });

        portScanButton.setText("SCAN PORTS");
        portScanButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                portScanButtonActionPerformed(evt);
            }
        });

        connectedDevicesArea.setColumns(20);
        connectedDevicesArea.setRows(5);
        jScrollPane3.setViewportView(connectedDevicesArea);

        openPortsArea.setColumns(20);
        openPortsArea.setRows(5);
        jScrollPane4.setViewportView(openPortsArea);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(12, 12, 12)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(scanButton, javax.swing.GroupLayout.PREFERRED_SIZE, 165, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(81, 81, 81)
                        .addComponent(hostCombobox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(portScanButton))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jScrollPane4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(11, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addGap(14, 14, 14)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(hostCombobox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(portScanButton)
                    .addComponent(scanButton))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jScrollPane3, javax.swing.GroupLayout.DEFAULT_SIZE, 301, Short.MAX_VALUE)
                    .addComponent(jScrollPane4))
                .addContainerGap(22, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void scanButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_scanButtonActionPerformed
        sc.ChangeState("ipscanner"); 
        sc.ClearConnectedDevices();  
        connectedDevicesArea.setText(""); 
        threads.clear(); 
        
        for(int i = 0; i <255; i++){
        threads.add(new Thread(new NetworkScanner(i, null, 0))); 
        }
        
        for (Thread thread: threads){
            thread.start();
            
        }
        
        for(Thread th:threads){
            try {
                th.join();
            } catch (InterruptedException ex) {
                Logger.getLogger(ScannerGUI.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        
        sc.PrintConnectedDevices();
        
        sc.SortConnectedDevices(); 
        List<Device> connected = sc.getConnectedDevices(); 
        connectedDevicesArea.append("Connected devices:\n"); 
        hostCombobox.removeAllItems();
        for(Device dev: connected){
            connectedDevicesArea.append(dev.getIp()+"\n"); 
            hostCombobox.addItem(dev.getIp()); 
        }
    }//GEN-LAST:event_scanButtonActionPerformed

    private void portScanButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_portScanButtonActionPerformed
   
        openPortsArea.setText(""); 
        threads.clear();
        String ip = hostCombobox.getItemAt(hostCombobox.getSelectedIndex());
        openPortsArea.setText("Scanning ports for ip \n" + ip);
        sc.setIpToScan(ip); 
        sc.ChangeState("portscanner"); 
        
        for(int i = 1; i<1024; i++){
            threads.add(new Thread(new NetworkScanner(0, sc.getIpToScan(), i))); 
        }
        
        for (Thread th: threads){
            th.start(); 
        }
        
        for(Thread th:threads){
            try {
                th.join();
            } catch (InterruptedException ex) {
                Logger.getLogger(ScannerGUI.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        
        if (sc.HasNoneOpenPorts()){
            openPortsArea.append("\nDevice has no open ports"); 
        }
        else{
            openPortsArea.append("\nscan done, open ports:\n");
        }
        
        List<Integer> open_ports = sc.getOpenPorts(); 
        for (int port: open_ports){
            System.out.println(Integer.toString(port)); 
            openPortsArea.append(Integer.toString(port)+"\n");
        }
    }//GEN-LAST:event_portScanButtonActionPerformed

    private void hostComboboxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_hostComboboxActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_hostComboboxActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(ScannerGUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(ScannerGUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(ScannerGUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(ScannerGUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new ScannerGUI().setVisible(true);
            }
        });
    }
    
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTextArea connectedDevicesArea;
    private javax.swing.JComboBox<String> hostCombobox;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JTextArea openPortsArea;
    private javax.swing.JButton portScanButton;
    private javax.swing.JButton scanButton;
    // End of variables declaration//GEN-END:variables
    String state = "closed"; 
    List<Thread> threads = new ArrayList<>(); 
    NetworkScanner sc = new NetworkScanner(0, null, 0); 
}
