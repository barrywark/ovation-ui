/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package us.physion.ovation.detailviews;

import java.util.*;
import javax.swing.AbstractListModel;
import javax.swing.JFileChooser;
import javax.swing.JPanel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.filechooser.FileFilter;
import org.netbeans.api.settings.ConvertAsProperties;
import org.openide.awt.ActionID;
import org.openide.awt.ActionReference;
import org.openide.explorer.ExplorerManager;
import org.openide.util.Lookup;
import org.openide.util.LookupEvent;
import org.openide.util.LookupListener;
import org.openide.windows.TopComponent;
import org.openide.util.NbBundle.Messages;
import org.openide.util.Utilities;
import ovation.*;
import us.physion.ovation.interfaces.ConnectionProvider;
import us.physion.ovation.interfaces.IEntityWrapper;

/**
 * Top component which displays something.
 */
@ConvertAsProperties(dtd = "-//us.physion.ovation.detailviews//ResourceView//EN",
autostore = false)
@TopComponent.Description(preferredID = "ResourceViewTopComponent",
//iconBase="SET/PATH/TO/ICON/HERE", 
persistenceType = TopComponent.PERSISTENCE_ALWAYS)
@TopComponent.Registration(mode = "properties", openAtStartup = true)
@ActionID(category = "Window", id = "us.physion.ovation.detailviews.ResourceViewTopComponent")
@ActionReference(path = "Menu/Window" /*
 * , position = 333
 */)
@TopComponent.OpenActionRegistration(displayName = "#CTL_ResourceViewAction",
preferredID = "ResourceViewTopComponent")
@Messages({
    "CTL_ResourceViewAction=ResourceView",
    "CTL_ResourceViewTopComponent=Resource View",
    "HINT_ResourceViewTopComponent=This window displays the Resource objects associated with the selected Ovation entity"
})
public final class ResourceViewTopComponent extends TopComponent {

    private LookupListener listener = new LookupListener() {

        @Override
        public void resultChanged(LookupEvent le) {
            
            //TODO: we should have some other Interface for things that can update the tags view
            //then we could get rid of the Library dependancy on the Explorer API
            if (TopComponent.getRegistry().getActivated() instanceof ExplorerManager.Provider)
            {
                updateResources();
            }
        }

    };
    protected Lookup.Result<IEntityWrapper> global;
    protected Collection<? extends IEntityWrapper> entities;
    protected ResourceListModel listModel;
        
    public ResourceViewTopComponent() {
        initComponents();
        setName(Bundle.CTL_ResourceViewTopComponent());
        setToolTipText(Bundle.HINT_ResourceViewTopComponent());

        global = Utilities.actionsGlobalContext().lookupResult(IEntityWrapper.class);
        global.addLookupListener(listener);
        
    }
    
    protected void updateResources()
    {
        entities = global.allInstances();
        ConnectionProvider cp = Lookup.getDefault().lookup(ConnectionProvider.class);
        cp.getConnection().getContext(); //getContext 
        List<ResourceWrapper> resources = new LinkedList();
        for (IEntityWrapper e: entities)
        {
            for (Resource r : e.getEntity().getResourcesIterable())
            {
                resources.add(new ResourceWrapper(r));
            }
        }
         
        listModel.setResources(resources);
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jScrollPane1 = new javax.swing.JScrollPane();
        jList1 = new javax.swing.JList();
        jButton1 = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();

        listModel = new ResourceListModel();
        jList1.setModel(listModel);
        jScrollPane1.setViewportView(jList1);

        org.openide.awt.Mnemonics.setLocalizedText(jButton1, org.openide.util.NbBundle.getMessage(ResourceViewTopComponent.class, "ResourceViewTopComponent.jButton1.text")); // NOI18N
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        org.openide.awt.Mnemonics.setLocalizedText(jButton2, org.openide.util.NbBundle.getMessage(ResourceViewTopComponent.class, "ResourceViewTopComponent.jButton2.text")); // NOI18N
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jScrollPane1)
                        .addContainerGap())
                    .addGroup(layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButton2, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(34, 34, 34))))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(31, 31, 31)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButton1)
                    .addComponent(jButton2))
                .addContainerGap(17, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        //addButton
        JFileChooser chooser = new JFileChooser();
        int returnVal = chooser.showOpenDialog(new JPanel());
        if (returnVal == JFileChooser.APPROVE_OPTION) {
            //add to preferences
            //TODO add a dialog that asks for uti type
            String path = chooser.getSelectedFile().getAbsolutePath();
            Resource r = null;
            for (IEntityWrapper e : entities)
            {
                r = e.getEntity().addResource(Response.NUMERIC_DATA_UTI, path);
            }
            if (r != null)
            {
                listModel.addResource(new ResourceWrapper(r));
            }
        }
    }//GEN-LAST:event_jButton1ActionPerformed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        //Delete selected resources
        for (Object o :jList1.getSelectedValues())
        {
            if (o instanceof ResourceWrapper)
            {
                Resource r = ((ResourceWrapper)o).getEntity();
                if (r.canWrite())
                {
                    for (IEntityWrapper e : entities)
                    {
                        IEntityBase eb = e.getEntity();
                        for (String name : eb.getResourceNames()) {
                            if (name.equals(r.getName())) {
                                eb.removeResource(r);
                                updateResources();
                                break;
                            }
                        }
                    }
                }
            }
        }
    }//GEN-LAST:event_jButton2ActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JList jList1;
    private javax.swing.JScrollPane jScrollPane1;
    // End of variables declaration//GEN-END:variables
    @Override
    public void componentOpened() {
        // TODO add custom code on component opening
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

   private class ResourceListModel extends AbstractListModel
    {
        List<ResourceWrapper> resources = new LinkedList<ResourceWrapper>();

        @Override
        public int getSize() {
            return resources.size();
        }

        @Override
        public Object getElementAt(int i) {
            if (i < resources.size())
                return resources.get(i);
            return null;
        }
        
        protected void setResources(List<ResourceWrapper> newResources)
        {
            int length = Math.max(resources.size(), newResources.size());
            resources = newResources;
            this.fireContentsChanged(this, 0, length);
        }
        
        protected void addResource(ResourceWrapper resource)
        {
            resources.add(resource);
            this.fireContentsChanged(this, resources.size(), resources.size());
        }
    };
    
    protected class ResourceWrapper
    {
        String uri;
        String name;
        public ResourceWrapper(Resource r)
        {
            uri = r.getURIString();
            name = r.getName();
        }
        public String toString(){
            return name;
        }
        
        public Resource getEntity()
        {
            IAuthenticatedDataStoreCoordinator dsc = Lookup.getDefault().lookup(ConnectionProvider.class).getConnection();
            DataContext c = dsc.getContext();
            
            return (Resource)c.objectWithURI(uri);
        }

    };
}
