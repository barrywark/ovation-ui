/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package us.physion.ovation.detailviews;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.util.Arrays;
import javax.swing.*;
import javax.swing.event.TableModelListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;
import javax.swing.tree.DefaultTreeModel;
import org.openide.util.Exceptions;
import ovation.Ovation;
import us.physion.ovation.interfaces.EventQueueUtilities;

/**
 *
 * @author huecotanks
 */
public class EditableTable extends javax.swing.JPanel implements TablePanel {

    private JTable table;
    private TreeWithTableRenderer treeUtils;
    private UserPropertySet propertySet;
    /**
     * Creates new form EditableTable
     */
    public EditableTable(JTable table, TreeWithTableRenderer t, UserPropertySet p) {
        initComponents();
        jScrollPane1.getViewport().add(table, null);
        jScrollPane1.setBorder(BorderFactory.createEmptyBorder());
        this.table = table;
        this.treeUtils = t;
        propertySet = p;
        //this.setBorder(BorderFactory.createEtchedBorder());
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jScrollPane1 = new javax.swing.JScrollPane();
        addButton = new javax.swing.JButton();
        deleteButton = new javax.swing.JButton();

        setBackground(new java.awt.Color(255, 255, 255));

        addButton.setText(org.openide.util.NbBundle.getMessage(EditableTable.class, "EditableTable.addButton.text")); // NOI18N
        addButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                addButtonActionPerformed(evt);
            }
        });

        deleteButton.setText(org.openide.util.NbBundle.getMessage(EditableTable.class, "EditableTable.deleteButton.text")); // NOI18N
        deleteButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                deleteButtonActionPerformed(evt);
            }
        });

        org.jdesktop.layout.GroupLayout layout = new org.jdesktop.layout.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jScrollPane1)
            .add(layout.createSequentialGroup()
                .add(addButton, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 28, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(deleteButton, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 28, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .add(0, 386, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(layout.createSequentialGroup()
                .add(jScrollPane1, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 445, Short.MAX_VALUE)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(addButton, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 23, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(deleteButton, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 23, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );
    }// </editor-fold>//GEN-END:initComponents

    private void addButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_addButtonActionPerformed
        //add a blank row to the table, make sure tableChanged deals with it appropriately
        int last = ((DefaultTableModel)table.getModel()).getRowCount() -1;
        
        String lastKey;
        if (last == -1)
            lastKey = "not null";
        else{
            lastKey = (String)table.getValueAt(last, 0);
        }
        if ( (lastKey != null && !lastKey.isEmpty()))
        {
                EventQueueUtilities.runOffEDT(new Runnable()
                {
                    @Override
                    public void run() {
                        ((DefaultTableModel)table.getModel()).addRow(new Object[]{"", ""});
                        //speeds up performance!
                        JScrollPane sp = ((JScrollPane)table.getParent().getParent());
                        sp.setSize(sp.getPreferredSize());
                        //((JScrollPane)table.getParent().getParent()).setViewportView(table);
                        EditableTable.this.setSize(EditableTable.this.getPreferredSize());
                        table.getSelectionModel().setSelectionInterval(table.getRowCount()-1, table.getRowCount()-1);
                        propertySet.setBlankRow(true);
                    }
                });
        }
    }//GEN-LAST:event_addButtonActionPerformed

    private void deleteButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_deleteButtonActionPerformed
       
         //There is a bug in getListeners - it doesnt find the PropertyTableModelListener if you pass is PropertyTableModelListener.class
        TableModelListener[] listeners = ((DefaultTableModel) table.getModel()).getListeners(TableModelListener.class);
        for (TableModelListener l : listeners) {
            if (l instanceof PropertyTableModelListener) {
                ((PropertyTableModelListener) l).deleteProperty((DefaultTableModel) table.getModel(), table.getSelectedRows());
                break;
            }
        }
    }//GEN-LAST:event_deleteButtonActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton addButton;
    private javax.swing.JButton deleteButton;
    private javax.swing.JScrollPane jScrollPane1;
    // End of variables declaration//GEN-END:variables

    @Override
    public JTable getTable() {
        return table;
    }
    
    @Override
    public JPanel getPanel() {
        return this;
    }

    @Override
    public Dimension getPreferredSize(){  
        int height = (table.getRowCount()+1)*table.getRowHeight() + 24 + addButton.getHeight();
                
        Dimension actual = new Dimension(treeUtils.getCellWidth(), height);
        return actual;  
    }
    
    protected JScrollPane getScrollPane()
    {
       return jScrollPane1;
    }
}
