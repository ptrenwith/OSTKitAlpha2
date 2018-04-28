
package org.digitalpassport.deserialize.json.transactiontypes;

import java.util.Objects;
import org.codehaus.jackson.annotate.JsonCreator;
import org.codehaus.jackson.annotate.JsonProperty;
import static org.digitalpassport.api.IConstants.*;

/**
 *
 * @author Philip M. Trenwith
 */
public class cToken
{
  private String USD;
  
  @JsonCreator
  public static cToken of(@JsonProperty(g_sPARAM_USD) String USD)
  {
    return new cToken(USD);
  }
  
  @JsonProperty(g_sPARAM_CODE)
  public String getUSD()
  {
    return USD;
  }
  
  public cToken(String USD)
  {
    this.USD = USD;
  }

  @Override
  public String toString()
  {
    return "cToken{" + "USD=" + USD + '}';
  }
}
