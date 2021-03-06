/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package us.physion.ovation.detailviews;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import org.openide.util.Lookup;
import ovation.DataContext;
import ovation.IAuthenticatedDataStoreCoordinator;
import ovation.IEntityBase;
import ovation.User;
import us.physion.ovation.interfaces.ConnectionProvider;

/**
 *
 * @author huecotanks
 */
class UserPropertySet implements Comparable{
    String username;
    String userURI;
    boolean isOwner;
    boolean current;
    boolean blankRow;
    Map<String, Object> properties;
    Set<String> uris;

    UserPropertySet(User u, boolean isOwner, boolean currentUser, Map<String, Object> props, Set<String> uris)
    {
        username = u.getUsername();
        this.isOwner = isOwner;
        this.properties = props;
        this.current = currentUser;
        this.uris = uris;
        userURI = u.getURIString();
    }
    
    void refresh(IAuthenticatedDataStoreCoordinator dsc) {
        DataContext c = dsc.getContext();
        User u = (User)c.objectWithURI(userURI);
        
        boolean owner = false;
        String uuid = u.getUuid();
        Map<String, Object> props = new HashMap<String, Object>();
        for (String uri: uris)
        {
            IEntityBase eb = c.objectWithURI(uri);
            if (eb.getOwner().getUuid().equals(uuid))
            {
                owner = true;
            }
            props.putAll(eb.getUserProperties(u));
        }
        
        username = u.getUsername();
        this.isOwner = owner;
        this.properties = props;
        this.current = c.currentAuthenticatedUser().getUuid().equals(u.getUuid());
    }

    String getDisplayName() {
        String s = username + "'s Properties";
        if (isOwner) {
            return s + " (owner)";
        }
        return s;
    }
    
    String getURI()
    {
        return userURI;
    }

    boolean isCurrentUser() {
        return current;
    }

    Map<String, Object> getProperties() {
        return properties;
    }
    
    String getUsername()
    {
        return username;
    }
    
    void setBlankRow(boolean b)
    {
        blankRow = b;
    }

    @Override
    public int compareTo(Object t) {
        if (t instanceof UserPropertySet)
        {
            UserPropertySet s = (UserPropertySet)t;
            
            if (s.isCurrentUser())
            {
                if (this.isCurrentUser())
                    return 0;
                return 1;
            }
            if (this.isCurrentUser())
                return -1;
            return this.getUsername().compareTo(s.getUsername());
        }
        else{
            throw new UnsupportedOperationException("Object type '" + t.getClass() + "' cannot be compared with object type " + this.getClass());
        }
    }
    
}
