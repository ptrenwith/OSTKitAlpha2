package org.digitalpassport.deserialize.json.transactiontypes;

import org.codehaus.jackson.annotate.JsonCreator;
import org.codehaus.jackson.annotate.JsonProperty;
import static org.digitalpassport.api.IConstants.*;

/**
 *
 * @author Philip M. Trenwith
 */
public class cPricePoints
{

  private cToken OST;

  @JsonCreator
  public static cPricePoints of(@JsonProperty(g_sPARAM_OST) cToken OST)
  {
    return new cPricePoints(OST);
  }

  @JsonProperty(g_sPARAM_OST)
  public cToken getOST()
  {
    return OST;
  }

  public cPricePoints(cToken OST)
  {
    this.OST = OST;
  }

  @Override
  public String toString()
  {
    return "cPricePoints{" + "OST=" + OST + '}';
  }
}
