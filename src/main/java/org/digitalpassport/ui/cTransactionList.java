package org.digitalpassport.ui;

import java.io.Serializable;
import java.util.ArrayList;

/**
 *
 * @author Philip M. Trenwith
 */
public class cTransactionList implements Serializable
{

  public ArrayList<String> lsTransactionUUIDs = new ArrayList();

  public ArrayList<String> getTransactionUUIDs()
  {
    return lsTransactionUUIDs;
  }

  public void setTransactionUUIDs(ArrayList<String> lsTransactionUUIDs)
  {
    this.lsTransactionUUIDs = lsTransactionUUIDs;
  }

  public void addTransactionUuid(String sTransaction_uuid)
  {
    lsTransactionUUIDs.add(sTransaction_uuid);
  }
}
