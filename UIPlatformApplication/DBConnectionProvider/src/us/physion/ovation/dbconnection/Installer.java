/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package us.physion.ovation.dbconnection;

import org.openide.modules.ModuleInstall;
import org.openide.util.Lookup;
import us.physion.ovation.interfaces.ConnectionProvider;

public class Installer extends ModuleInstall {

    @Override
    public void restored() {
        new DBConnectionProvider();
    }
}
