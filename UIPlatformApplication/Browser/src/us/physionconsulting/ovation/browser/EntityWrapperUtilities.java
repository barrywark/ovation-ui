/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package us.physionconsulting.ovation.browser;

import java.beans.PropertyVetoException;
import java.util.*;
import javax.swing.SwingUtilities;
import javax.swing.tree.TreePath;
import org.openide.explorer.ExplorerManager;
import org.openide.explorer.view.BeanTreeView;
import org.openide.nodes.AbstractNode;
import org.openide.nodes.Children;
import org.openide.nodes.FilterNode;
import org.openide.nodes.Node;
import org.openide.util.Exceptions;
import org.openide.util.Lookup;
import org.openide.util.Utilities;
import org.openide.util.lookup.Lookups;
import ovation.*;

/**
 *
 * @author huecotanks
 */
public class EntityWrapperUtilities {

    private static String SEPARATOR = ";";
    
    
    protected static Map<String, Node> createNodesFromQuery(ExplorerManager mgr, Iterator<IEntityBase> itr)
    {
        Map<String, Node> treeMap = BrowserUtilities.getNodeMap();
        while (itr.hasNext()) {
            IEntityBase e = itr.next();
            Stack<EntityWrapper> p= new Stack();
            p.push(new EntityWrapper(e));
            Set<Stack<EntityWrapper>> paths = getParentsInTree(e, p);
            
            for (Stack<EntityWrapper> path : paths)
            {
                EntityWrapper first = path.pop();
                Node parentInTree = treeMap.get(first);
                if (parentInTree == null) {
                    parentInTree = mgr.getRootContext();
                    path.push(first);
                }
                QueryChildren q = (QueryChildren) (parentInTree.getChildren());
                q.addPath(path);
            }
        }
        return treeMap;
    }
            
    /*protected static Set<Node> existingNodesFromQuery(Map<String, Node> treeMap, Iterator<IEntityBase> itr, ExplorerManager mgr) {
        HashSet<Node> selectedNodes = new HashSet();
        while (itr.hasNext()) {
            IEntityBase e = itr.next();
            String key = e.getURIString();
            Node n;
            if (!treeMap.containsKey(key)) {
                String pathToOpenParent = getParentInTree(e, treeMap, e.getURIString());
                String[] uris = pathToOpenParent.split(SEPARATOR);

                Node parentInTree = treeMap.get(uris[0]);
                if (parentInTree == null) {
                    parentInTree = mgr.getRootContext();
                } else {
                    uris = Arrays.copyOfRange(uris, 1, uris.length);
                }

                for (String uri : uris) {
                    Node[] nodes = parentInTree.getChildren().getNodes(true);
                    for (Node node : nodes) {
                        EntityWrapper ew = (EntityWrapper) node.getLookup().lookup(EntityWrapper.class);
                        if (ew.getURI().equals(uri)) {
                            parentInTree = node;
                            break;
                        }
                    }
                }
                selectedNodes.add(parentInTree);
            } else {
                n = treeMap.get(key);
                selectedNodes.add(n);
            }
        }
        return selectedNodes;
    }*/

    //expand each path from root to the given set of nodes in the tree view
    /*protected static void expandNodes(Set<Node> nodes, BeanTreeView btv, ExplorerManager mgr) {
        
        //btv = Utilities.actionsGlobalContext().lookup(BeanTreeView.class); 
        Set<Node> toExpand = new HashSet<Node>();
        for (Node n : nodes) {
            ArrayList<Node> ancestors = new ArrayList();
            Node parent = n.getParentNode();
            while (parent != null && !toExpand.contains(parent)) {
                ancestors.add(parent);
                toExpand.add(parent);
                parent = parent.getParentNode();
            }
            Node[] ancestorNodes = ancestors.toArray(new Node[0]);
            for (int i = ancestorNodes.length - 1; i >= 0; --i) {
                    btv.expandNode(ancestorNodes[i]);
            }
        }
        try {
            mgr.setSelectedNodes((Node[]) (nodes.toArray(new Node[0])));
        } catch (PropertyVetoException ex) {
        } 
    }*/

    /*protected static TreePath createTreePath(Node currentNode, List<Node> ancestors) {
        ancestors.add(currentNode);
        if (currentNode.getParentNode() == null) {
            return new TreePath(ancestors);
        }
        return createTreePath(currentNode.getParentNode(), ancestors);
    }*/

    //not tail recursive but oh well
    protected static Set<Stack<EntityWrapper>> getParentsInTree(IEntityBase e, Stack<EntityWrapper> path)
    {
        Set<Stack<EntityWrapper>> paths = new HashSet<Stack<EntityWrapper>>();
        String uri = e.getURIString();

        Set<IEntityBase> parents = getParents(e);
        if (parents.isEmpty())
        {
            paths.add(path);
            return paths;
        }
        
        for (IEntityBase parent: parents)
        {
            Stack newPath = new Stack();
            for (int i = 0; i < path.size(); i++) {
                newPath.push(path.get(i));
            }
            newPath.push(new EntityWrapper(parent));
            paths.addAll(getParentsInTree(parent, newPath));
        }
       
        return paths;
       
    }

    private static Set<IEntityBase> getParents(IEntityBase entity) {
        Set<IEntityBase> parents = new HashSet();
        Class type = entity.getClass();
        if (type.isAssignableFrom(Source.class)) 
        {
            Source parent = ((Source) entity).getParent();
            if (parent != null)
            {
                parents.add(parent);
            }
        } else if (type.isAssignableFrom(Experiment.class)) {
            for (Project p : ((Experiment) entity).getProjects())
            {
                parents.add(p);
            }
            
            for (Source p : ((Experiment) entity).getSources())
            {
                parents.add(p);
            }
        } else if (type.isAssignableFrom(EpochGroup.class)) {
            EpochGroup parent = ((EpochGroup) entity).getParent();
            if (parent == null) {
                parents.add(((EpochGroup) entity).getExperiment());
            }
            else{
                parents.add(parent);
            }
        } else if (type.isAssignableFrom(Epoch.class)) {
            parents.add(((Epoch) entity).getEpochGroup());
        } else if (type.isAssignableFrom(Response.class)) {
            parents.add(((Response) entity).getEpoch());
        } else if (type.isAssignableFrom(Stimulus.class)) {
            parents.add( ((Stimulus) entity).getEpoch());
        } else if (type.isAssignableFrom(DerivedResponse.class)) {
            parents.add( ((DerivedResponse) entity).getEpoch());
        }
        return parents;
    }
    
    protected static Node createNode(EntityWrapper key, Children c)
    {
        return createNode(key, BrowserUtilities.getNodeMap(), c);
    }
    
    protected static Node createNode(EntityWrapper key, Map<String, Node> treeMap, Children c)
    {
        String uri = key.getURI();
        if (uri != null && treeMap.containsKey(uri)) {
            //create a node that just proxies the existing node
            return new FilterNode(treeMap.get(uri));
        }
        
        //otherwise, create an AbstractNode representing this object
        AbstractNode n = new AbstractNode(c, Lookups.singleton(key));
        n.setDisplayName(key.getDisplayName());
        setIconForType(n, key.getType());
        if (uri != null) {//TODO: test this for nodes that don't have uris
            treeMap.put(key.getURI(), n);
        }
        return n;
    }
    
    protected static void setIconForType(AbstractNode n, Class entityClass) {
        if (entityClass.isAssignableFrom(Experiment.class)) {
            n.setIconBaseWithExtension("");

        } else if (entityClass.isAssignableFrom(EpochGroup.class)) {
            n.setIconBaseWithExtension("");
        }
    }
}
