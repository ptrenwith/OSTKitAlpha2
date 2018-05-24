
package org.digitalpassport;

import java.util.Random;
import org.digitalpassport.api.commands.cUserManagement;
import org.digitalpassport.deserialize.json.cResponse;
import org.digitalpassport.deserialize.json.users.lists.cUser;
import org.digitalpassport.jdbc.cDatabaseHandler;
import org.digitalpassport.ui.panels.cFileSharingPanel;
import org.digitalpassport.ui.panels.cTokenManagementPanel;

/**
 *
 * @author Philip M. Trenwith
 */
public class cAutomation
{
  public static void main(String[] args)
  {
    cUserManagement oUserManagement = new cUserManagement();
    cTokenManagementPanel oTokenManager = new cTokenManagementPanel();
    oTokenManager.loadTransactionsFromBlockchain();
    
    cResponse oResponse = oUserManagement.listUsers_sandbox("1", "name", "asc");
    
    if (oResponse.getsuccess())
    {
      cUser[] users = oResponse.getdata().getusers();
      int min = 1;
      int max = users.length-1;
      Random oRandom = new Random();
      for (cUser oUser: users)
      {
        String sDisplayName = oUser.getname();
        String sUsername = cDatabaseHandler.instance().getUsername(sDisplayName);
        
        for (int i=1; i<=10; i++)
        {
          cDatabaseHandler.instance().uploadPassport(sUsername + "_File_" + i, sUsername, sDisplayName);
          String sFileId = cDatabaseHandler.instance().getFileIdFromName(sUsername + "_File_" + i);
          for (int j=1; j<=10; j++)
          {
            int iUserIndex = oRandom.nextInt(max);
            cUser oOtherUser = users[iUserIndex];
            cFileSharingPanel.share(Integer.parseInt(sFileId), oUser.getId(), oOtherUser);
          }
        }
      }
    }
  }
}
