
package org.digitalpassport.ui;

import java.io.Serializable;
import java.util.ArrayList;

/**
 *
 * @author Philip M. Trenwith
 */
public class cAirdropList implements Serializable
{
  public ArrayList<String> lsAirdropUUIDs = new ArrayList();

  public ArrayList<String> getlsAirdropUUIDs()
  {
    return lsAirdropUUIDs;
  }

  public void setlsAirdropUUIDs(ArrayList<String> lsAirdropUUIDs)
  {
    this.lsAirdropUUIDs = lsAirdropUUIDs;
  }

  public void addAirdropUuid(String sAirdrop_uuid)
  {
    lsAirdropUUIDs.add(sAirdrop_uuid);
  }
}
