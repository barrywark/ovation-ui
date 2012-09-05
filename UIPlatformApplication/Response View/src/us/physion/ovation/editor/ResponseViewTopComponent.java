/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package us.physion.ovation.editor;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Font;
import java.util.*;
import java.util.concurrent.FutureTask;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.SwingUtilities;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.CategoryAxis;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.axis.ValueAxis;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.Plot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.title.TextTitle;
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
import org.jfree.data.*;
import org.jfree.data.category.CategoryDataset;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.general.Dataset;
import org.jfree.data.xy.DefaultXYDataset;
import org.jfree.ui.RectangleInsets;
import org.openide.explorer.ExplorerUtils;
import org.openide.explorer.view.BeanTreeView;
import us.physion.ovation.interfaces.EventQueueUtilities;
import us.physion.ovation.interfaces.IEntityWrapper;


/**
 * Top component which displays something.
 */
@ConvertAsProperties(dtd = "-//us.physion.ovation.editor//ResponseView//EN",
autostore = false)
@TopComponent.Description(preferredID = "ResponseViewTopComponent",
//iconBase="SET/PATH/TO/ICON/HERE", 
persistenceType = TopComponent.PERSISTENCE_ALWAYS)
@TopComponent.Registration(mode = "editor", openAtStartup = true)
@ActionID(category = "Window", id = "us.physion.ovation.editor.ResponseViewTopComponent")
@ActionReference(path = "Menu/Window" /*
 * , position = 333
 */)
@TopComponent.OpenActionRegistration(displayName = "#CTL_ResponseViewAction",
preferredID = "ResponseViewTopComponent")
@Messages({
    "CTL_ResponseViewAction=Response View",
    "CTL_ResponseViewTopComponent=Response Viewer",
    "HINT_ResponseViewTopComponent=This plots the currently selected numeric Response data"
})
public final class ResponseViewTopComponent extends TopComponent {
    
    Lookup.Result global;
    List<ResponsePanel> responsePanels = new ArrayList<ResponsePanel>();
    ChartTableModel chartModel = new ChartTableModel(responsePanels);
    Lookup l;
    
    private LookupListener listener = new LookupListener() {

        @Override
        public void resultChanged(LookupEvent le) {
            
            //TODO: we should have some other Interface for things that can update the tags view
            //then we could get rid of the Library dependancy on the Explorer API
            if (TopComponent.getRegistry().getActivated() instanceof ExplorerManager.Provider)
            {
                updateEntitySelection();
            }
        }
    };
    public ResponseViewTopComponent() {
        initTopComponent();
    }
    
    private void initTopComponent()
    {
        initComponents();
        setName(Bundle.CTL_ResponseViewTopComponent());
        setToolTipText(Bundle.HINT_ResponseViewTopComponent());
        global = Utilities.actionsGlobalContext().lookupResult(IEntityWrapper.class);
        global.addLookupListener(listener);
        jTable1.setDefaultRenderer(ResponsePanel.class, new ResponseCellRenderer());
    }
   
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        responseListPane = new BeanTreeView();
        jTable1 = new javax.swing.JTable();

        jTable1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        responseListPane.setViewportView(jTable1);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(responseListPane, javax.swing.GroupLayout.DEFAULT_SIZE, 510, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(responseListPane, javax.swing.GroupLayout.DEFAULT_SIZE, 527, Short.MAX_VALUE)
        );
    }// </editor-fold>//GEN-END:initComponents

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTable jTable1;
    private javax.swing.JScrollPane responseListPane;
    // End of variables declaration//GEN-END:variables
    @Override
    public void componentOpened() {
        // TODO add custom code on component opening
        responseListPane.setVisible(false);
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
    protected void updateEntitySelection()
    {
        updateEntitySelection(global.allInstances());
    }
    
    protected List<ResponseGroupWrapper> updateEntitySelection(Collection<? extends IEntityWrapper> entities) {
        if (entities.size() == 0)
        {
            responseListPane.setVisible(false);
            return null;
        }

        LinkedList<ResponseGroupWrapper> responseGroupList = new LinkedList<ResponseGroupWrapper>();
        for (IEntityWrapper ew: entities)
        {
            if (ew.getType().isAssignableFrom(Epoch.class))
            {
                Epoch epoch = (Epoch)(ew.getEntity());//getEntity gets the context for the given thread
                for (String name: epoch.getResponseNames())
                {
                    ResponseWrapper entity = ResponseWrapper.createIfDisplayable(epoch.getResponse(name), name);
                    if (entity == null)
                    {
                        continue;
                    }
                    ResponseGroupWrapper current = null;
                    for (ResponseGroupWrapper wrapper : responseGroupList)
                    {
                        if (wrapper instanceof ChartWrapper) {
                            ChartWrapper chart = (ChartWrapper) wrapper;
                            if (entity.xUnits().equals(chart.getXAxis()) && entity.yUnits().equals(chart.getYAxis())) {
                                chart.setTitle("Responses for " + epoch.getProtocolID());
                                addXYDataset(chart.getDataset(), entity, name);
                                current = chart;
                                break;
                            }
                        } 
                    }
                    if (current == null)// response doesn't need to be added to existing chart
                    {
                        if (entity.isPlottable())
                            addXYDataset(entity.cw.getDataset(), entity, name);
                        
                        responseGroupList.add(entity);
                    }
                }
                
            }
            else if (ew.getType().isAssignableFrom(Response.class)) {
                ResponseWrapper entity = ResponseWrapper.createIfDisplayable(ew, ew.getDisplayName());
                if (entity == null) {
                    continue;
                }
                
                if (entity.isPlottable)
                {
                    responseGroupList.add(entity);
                    addXYDataset(entity.cw.getDataset(), entity, ew.getDisplayName());
                }
            }
        }
        
        EventQueueUtilities.runOnEDT(updateChartRunnable(responseGroupList));
        return responseGroupList;
    }
    
     private Runnable updateChartRunnable(final List<ResponseGroupWrapper> reponseGroups)
    {
        final int height = this.getHeight();
        return new Runnable(){

            @Override
            public void run() {
                if (reponseGroups.size() == 0) {
                    responseListPane.setVisible(false);
                    return;
                }
                
                int initialSize = responsePanels.size();
                while (!responsePanels.isEmpty()) {
                        responsePanels.remove(0);
                }
                if (reponseGroups.size() < initialSize) {
                    chartModel.fireTableRowsDeleted(reponseGroups.size() +1, initialSize - 1);
                }
                
                int rowheight = (height / reponseGroups.size());
                    if (rowheight >= 1) {
                        jTable1.setRowHeight(rowheight);
                    }
                for (ResponseGroupWrapper c : reponseGroups) {
                    JPanel p = c.generatePanel();

                    responsePanels.add(new ResponsePanel(p));
                    
                }

                chartModel.fireTableDataChanged();
                responseListPane.setVisible(true);
            }
        };
    }

    protected static void addXYDataset(DefaultXYDataset ds, ResponseWrapper rw, String name)
    {
        NumericData d = rw.getData();
        double samplingRate = rw.getSamplingRate();
        long[] shape = d.getShape();
        long size = 1;
        for (int dimension = 0; dimension<shape.length; dimension++)
        {
            size = size*shape[dimension];
        }
        
        if (shape.length == 1)
        {
            if (d.getDataFormat() == NumericDataFormat.FloatingPointDataType)
            {
                double[] floatingData = d.getFloatingPointData();
                double[][] data = new double[2][(int) size];
                for (int i = 0; i < (int) size; ++i) {
                    data[1][i] = floatingData[i];
                    data[0][i] = i/samplingRate;
                }
                ds.addSeries(name, data);
            }
            else if (d.getDataFormat() == NumericDataFormat.SignedFixedPointDataType)
            {
                int[] integerData = d.getIntegerData();
                double[][] data = new double[(int) size][2];
                for (int i = 0; i < (int) size; ++i) {
                    data[1][i] = integerData[i];
                    data[0][i] = i/samplingRate;
                }
                ds.addSeries(name, data);
            }
            else if (d.getDataFormat() == NumericDataFormat.UnsignedFixedPointDataType)
            {
                long[] longData = d.getUnsignedIntData();
                double[][] data = new double[(int) size][2];
                for (int i = 0; i < (int) size; ++i) {
                    data[1][i] = longData[i];
                    data[0][i] = i/samplingRate;
                }
                ds.addSeries(name, data);
            }
            
            else{
                Ovation.getLogger().debug("NumericData object has unknown type: " + d.getDataFormat());
            }
        }
    }
    //for testing
    protected ChartTableModel getChartTableModel()
    {
        return chartModel;
    }

}
