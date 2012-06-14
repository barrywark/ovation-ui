/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package us.physionconsulting.ovation.browser;

import com.physion.ebuilder.ExpressionBuilder;
import com.physion.ebuilder.expression.ExpressionTree;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import org.netbeans.api.settings.ConvertAsProperties;
import org.openide.awt.ActionID;
import org.openide.awt.ActionReference;
import org.openide.explorer.ExplorerManager;
import org.openide.explorer.ExplorerUtils;
import org.openide.explorer.view.BeanTreeView;
import org.openide.nodes.AbstractNode;
import org.openide.nodes.Children;
import org.openide.nodes.Node;
import org.openide.util.Lookup;
import org.openide.util.Lookup.Result;
import org.openide.util.LookupEvent;
import org.openide.util.LookupListener;
import org.openide.windows.TopComponent;
import org.openide.util.NbBundle.Messages;
import org.openide.util.Utilities;
import org.openide.util.lookup.Lookups;
import ovation.IAuthenticatedDataStoreCoordinator;
import ovation.IEntityBase;
import us.physion.ovation.interfaces.ConnectionProvider;
import us.physion.ovation.interfaces.ConnectionListener;

/**
 * Top component which displays something.
 */
@ConvertAsProperties(dtd = "-//us.physionconsulting.ovation.browser//Browser//EN",
autostore = false)
@TopComponent.Description(preferredID = "BrowserTopComponent",
//iconBase="SET/PATH/TO/ICON/HERE", 
persistenceType = TopComponent.PERSISTENCE_ALWAYS)
@TopComponent.Registration(mode = "explorer", openAtStartup = true)
@ActionID(category = "Window", id = "us.physionconsulting.ovation.browser.BrowserTopComponent")
@ActionReference(path = "Menu/Window" /*
 * , position = 333
 */)
@TopComponent.OpenActionRegistration(displayName = "#CTL_BrowserAction",
preferredID = "BrowserTopComponent")
@Messages({
    "CTL_BrowserAction=Project Navigator",
    "CTL_BrowserTopComponent=Project Navigator",
    "HINT_BrowserTopComponent=Browse your Ovation Database"
})
public final class BrowserTopComponent extends TopComponent implements ExplorerManager.Provider{

    private final ExplorerManager em = new ExplorerManager();
    private final HashMap<String, Node> browserMap = new HashMap<String, Node>();
    public BrowserTopComponent() {
        initComponents();
        setName(Bundle.CTL_BrowserTopComponent());
        setToolTipText(Bundle.HINT_BrowserTopComponent());
        
        associateLookup(ExplorerUtils.createLookup(em, getActionMap()));
        
        BrowserUtilities.initBrowser(em, browserMap, true);
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jSplitPane1 = new javax.swing.JSplitPane();
        treeViewPane = new BeanTreeView();
        queryButton = new javax.swing.JButton();

        org.openide.awt.Mnemonics.setLocalizedText(queryButton, org.openide.util.NbBundle.getMessage(BrowserTopComponent.class, "BrowserTopComponent.queryButton.text")); // NOI18N
        queryButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                queryButtonActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(treeViewPane)
            .addGroup(layout.createSequentialGroup()
                .addComponent(queryButton)
                .addGap(0, 524, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(treeViewPane, javax.swing.GroupLayout.DEFAULT_SIZE, 581, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(queryButton)
                .addGap(10, 10, 10))
        );
    }// </editor-fold>//GEN-END:initComponents

    private void queryButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_queryButtonActionPerformed
        BrowserUtilities.queryActionPerformed(((BeanTreeView)treeViewPane), getExplorerManager(), browserMap, true);
    }//GEN-LAST:event_queryButtonActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JSplitPane jSplitPane1;
    private javax.swing.JButton queryButton;
    private javax.swing.JScrollPane treeViewPane;
    // End of variables declaration//GEN-END:variables
    @Override
    public void componentOpened() {
         //root node in tree view. true = asynchronously
         BrowserUtilities.recreateTreeComponent(em, browserMap, true);
    }

    @Override
    public void componentClosed() {
        // TODO add custom code on component closing
    }

    void writeProperties(java.util.Properties p) {
        // better to version settings since initial version as advocated at
        // http://wiki.apidesign.org/wiki/PropertyFiles
        p.setProperty("version", "1.0");
        // TODO store your settings
    }

    void readProperties(java.util.Properties p) {
        String version = p.getProperty("version");
        // TODO read your settings according to their version
    }

    @Override
    public ExplorerManager getExplorerManager() {
        return em;
    }
}
