/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package us.physion.ovation.browser;

import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.Callable;
import org.openide.util.Lookup;
import ovation.*;
import us.physion.ovation.interfaces.ConnectionProvider;
import us.physion.ovation.interfaces.IEntityWrapper;

/**
 *
 * @author huecotanks
 */
public class EntityWrapper implements IEntityWrapper {
    
    private String uri;
    private Class type;
    private String displayName;
    private Callable<IEntityBase> retrieveEntity;
    
    public EntityWrapper(IEntityBase e)
    {
        uri = e.getURIString();
        type = e.getClass();
        displayName = inferDisplayName(e);
        
        System.out.println("Done constructing entity wrapper (" + type.getName() + ")");
    }
    
    //used by the PerUserEntityWrapper object
    protected EntityWrapper(String name, Class clazz, String uri)
    {
        type = clazz;
        displayName = name;
        this.uri = uri;
    }
    
    //If EntityWrapper is unique, it's corresponding Node in a tree view should *not* be a FilterNode
    public Boolean isUnique()
    {
        return false;
    }
    
    //we have to look up the entitybase on creation, because I index on the object's URI. 
    //If this changes, we should use this constructor for large objects
    /*public EntityWrapper(String name, Class clazz, Callable<IEntityBase> toCall)
    {
        type = clazz;
        displayName = name;
        retrieveEntity = toCall;
    }*/
    
    @Override
    public IEntityBase getEntity(){
        IAuthenticatedDataStoreCoordinator dsc = Lookup.getDefault().lookup(ConnectionProvider.class).getConnection();
        DataContext c = dsc.getContext();
        if (uri == null)
        {
            try {
                return retrieveEntity.call();
            } catch (Exception ex) {
                return null;
            }
        }
        return c.objectWithURI(uri);
    }
    @Override
    public String getURI()
    {
        return uri;
    }
    @Override
    public String getDisplayName() {return displayName;}
    @Override
    public Class getType() { return type;}

   
    private String inferDisplayName(IEntityBase e) {
        
        if (type.isAssignableFrom(Source.class))
        {
            return ((Source)e).getLabel();
        }
        else if (type.isAssignableFrom(Project.class))
        {
            return ((Project)e).getName();
        }else if (type.isAssignableFrom(Experiment.class))
        {
            return ((Experiment)e).getStartTime().toString("MM/dd/yyyy-hh:mm:ss");
        }
        else if (type.isAssignableFrom(EpochGroup.class))
        {
            return ((EpochGroup)e).getLabel();
        }
        else if (type.isAssignableFrom(Epoch.class))
        {
            return ((Epoch)e).getProtocolID();
        }
        else if (type.isAssignableFrom(Response.class))
        {
            return ((Response)e).getExternalDevice().getName();
        }
        else if (type.isAssignableFrom(Stimulus.class))
        {
            return ((Stimulus)e).getExternalDevice().getName();
        }
        else if (type.isAssignableFrom(DerivedResponse.class))
        {
            return ((DerivedResponse)e).getName();
        }
        else if (type.isAssignableFrom(AnalysisRecord.class))
        {
            return ((AnalysisRecord)e).getName();
        }
        return "<no name>";
    }
}