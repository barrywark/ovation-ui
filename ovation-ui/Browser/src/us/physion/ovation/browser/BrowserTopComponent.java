/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package us.physion.ovation.browser;

import com.physion.ebuilder.ExpressionBuilder;
import com.physion.ebuilder.expression.ExpressionTree;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import javax.swing.Action;
import org.netbeans.api.settings.ConvertAsProperties;
import org.openide.actions.CopyAction;
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
import org.openide.util.actions.SystemAction;
import org.openide.util.lookup.Lookups;
import org.openide.windows.WindowManager;
import ovation.IAuthenticatedDataStoreCoordinator;
import ovation.IEntityBase;
import us.physion.ovation.interfaces.ConnectionProvider;
import us.physion.ovation.interfaces.ConnectionListener;

/**
 * Top component which displays something.
 */
@ConvertAsProperties(dtd = "-//us.physion.ovation.browser//Browser//EN",
autostore = false)
@TopComponent.Description(preferredID = "BrowserTopComponent",
//iconBase="SET/PATH/TO/ICON/HERE",
persistenceType = TopComponent.PERSISTENCE_ALWAYS)
@TopComponent.Registration(mode = "explorer", openAtStartup = true)
@ActionID(category = "Window", id = "us.physion.ovation.browser.BrowserTopComponent")
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

    private Lookup l;
    private ExplorerManager em = new ExplorerManager();
    public BrowserTopComponent() {
        initComponents();
        setName(Bundle.CTL_BrowserTopComponent());
        setToolTipText(Bundle.HINT_BrowserTopComponent());

        l = ExplorerUtils.createLookup(em, getActionMap());
        associateLookup(l);
        BrowserUtilities.initBrowser(em, true);
        ((BeanTreeView)treeViewPane).setRootVisible(false);
        
        //TODO: extend existing CopyAction somehow
        /*
        BrowserUtilities.myCopyAction().setEnabled(true);

        CopyAction globalCopyAction = SystemAction.get (CopyAction.class);
        Object key = globalCopyAction.getActionMapKey(); // key is a special value defined by all CallbackSystemActions
        getActionMap().put (key, BrowserUtilities.myCopyAction());
        */
    }

    @Override
    public Lookup getLookup()
    {
        return l;
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
        insertItemButton = new javax.swing.JButton();
        removeItemButton = new javax.swing.JButton();

        org.openide.awt.Mnemonics.setLocalizedText(insertItemButton, org.openide.util.NbBundle.getMessage(BrowserTopComponent.class, "BrowserTopComponent.insertItemButton.text")); // NOI18N

        org.openide.awt.Mnemonics.setLocalizedText(removeItemButton, org.openide.util.NbBundle.getMessage(BrowserTopComponent.class, "BrowserTopComponent.removeItemButton.text")); // NOI18N

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(treeViewPane, javax.swing.GroupLayout.DEFAULT_SIZE, 605, Short.MAX_VALUE)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(insertItemButton, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(removeItemButton, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(treeViewPane, javax.swing.GroupLayout.DEFAULT_SIZE, 651, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(insertItemButton)
                    .addComponent(removeItemButton))
                .addContainerGap())
        );
    }// </editor-fold>//GEN-END:initComponents

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton insertItemButton;
    private javax.swing.JSplitPane jSplitPane1;
    private javax.swing.JButton removeItemButton;
    private javax.swing.JScrollPane treeViewPane;
    // End of variables declaration//GEN-END:variables
    @Override
    public void componentOpened() {
         //BrowserUtilities.createTreeComponent(em, true);
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
