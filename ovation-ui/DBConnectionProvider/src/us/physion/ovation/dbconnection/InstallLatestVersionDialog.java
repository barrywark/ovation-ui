/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package us.physion.ovation.dbconnection;

import java.awt.Color;
import java.awt.Desktop;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import javax.swing.JFrame;
import org.openide.ErrorManager;
import org.openide.util.Exceptions;
import us.physion.ovation.interfaces.EventQueueUtilities;

/**
 *
 * @author huecotanks
 */
public class InstallLatestVersionDialog extends javax.swing.JDialog {

    /**
     * Creates new form InstallLatestVersionDialog
     */
    public InstallLatestVersionDialog() {
        super(new JFrame(), true);
        initComponents();
        
        this.getRootPane().setDefaultButton(okButton);
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        okButton = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();
        downloadLink = new javax.swing.JButton();
        jLabel2 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        okButton.setText(org.openide.util.NbBundle.getMessage(InstallLatestVersionDialog.class, "InstallLatestVersionDialog.okButton.text")); // NOI18N
        okButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                okButtonActionPerformed(evt);
            }
        });

        jLabel1.setFont(new java.awt.Font("Lucida Grande", 1, 14)); // NOI18N
        jLabel1.setText(org.openide.util.NbBundle.getMessage(InstallLatestVersionDialog.class, "InstallLatestVersionDialog.jLabel1.text")); // NOI18N

        downloadLink.setForeground(new java.awt.Color(11, 0, 128));
        downloadLink.setText(org.openide.util.NbBundle.getMessage(InstallLatestVersionDialog.class, "InstallLatestVersionDialog.downloadLink.text")); // NOI18N
        downloadLink.setBorderPainted(false);
        downloadLink.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                downloadLinkActionPerformed(evt);
            }
        });

        jLabel2.setText(org.openide.util.NbBundle.getMessage(InstallLatestVersionDialog.class, "InstallLatestVersionDialog.jLabel2.text")); // NOI18N

        org.jdesktop.layout.GroupLayout layout = new org.jdesktop.layout.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(layout.createSequentialGroup()
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(layout.createSequentialGroup()
                        .add(20, 20, 20)
                        .add(jLabel2))
                    .add(layout.createSequentialGroup()
                        .add(333, 333, 333)
                        .add(okButton))
                    .add(layout.createSequentialGroup()
                        .add(221, 221, 221)
                        .add(downloadLink, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                    .add(layout.createSequentialGroup()
                        .add(256, 256, 256)
                        .add(jLabel1)))
                .addContainerGap(17, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(org.jdesktop.layout.GroupLayout.TRAILING, layout.createSequentialGroup()
                .add(12, 12, 12)
                .add(jLabel1, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 16, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .add(18, 18, 18)
                .add(jLabel2)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED, 14, Short.MAX_VALUE)
                .add(downloadLink, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.UNRELATED)
                .add(okButton)
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void downloadLinkActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_downloadLinkActionPerformed
        if (Desktop.isDesktopSupported()) {
            try {
                URI uri = new URI("mailto", "support@physionconsulting.com", "subject=Ovation UI Issue Reporting");
                //URI uri = new URI("mailto:support@physionconsulting.com?subject=Ovation UI Issue Reporting");
                Desktop.getDesktop().mail(uri);
                downloadLink.setForeground(new Color(102, 51, 102));

            } catch (URISyntaxException ex) {
                Exceptions.printStackTrace(ex);
                ErrorManager.getDefault().notify(ex);
            } catch (IOException ex) {
                Exceptions.printStackTrace(ex);
                ErrorManager.getDefault().notify(ex);
            }

        }
    }//GEN-LAST:event_downloadLinkActionPerformed

    private void okButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_okButtonActionPerformed
        dispose();
    }//GEN-LAST:event_okButtonActionPerformed

    protected void disposeOnEDT() {
        EventQueueUtilities.runOnEDT(new Runnable() {

            @Override
            public void run() {
                InstallLatestVersionDialog.this.dispose();
            }
        });
    }

    public void showDialog() {
        try {
            EventQueueUtilities.runAndWaitOnEDT(new Runnable() {

                @Override
                public void run() {
                    InstallLatestVersionDialog.this.setLocationRelativeTo(null);
                    InstallLatestVersionDialog.this.pack();
                    InstallLatestVersionDialog.this.setVisible(true);
                }
            });
        } catch (InterruptedException ex) {
            this.disposeOnEDT();
            Exceptions.printStackTrace(ex);
            ErrorManager.getDefault().notify(ex);
        }


    }

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /*
         * Set the Nimbus look and feel
         */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /*
         * If Nimbus (introduced in Java SE 6) is not available, stay with the
         * default look and feel. For details see
         * http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(InstallLatestVersionDialog.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(InstallLatestVersionDialog.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(InstallLatestVersionDialog.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(InstallLatestVersionDialog.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /*
         * Create and display the dialog
         */
        java.awt.EventQueue.invokeLater(new Runnable() {

            public void run() {
                InstallLatestVersionDialog dialog = new InstallLatestVersionDialog();
                dialog.addWindowListener(new java.awt.event.WindowAdapter() {

                    @Override
                    public void windowClosing(java.awt.event.WindowEvent e) {
                        e.getWindow().dispose();
                    }
                });
                dialog.setVisible(true);
            }
        });
    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton downloadLink;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JButton okButton;
    // End of variables declaration//GEN-END:variables
}