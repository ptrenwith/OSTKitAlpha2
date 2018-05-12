
package org.digitalpassport.deserialize.json;

import org.codehaus.jackson.annotate.JsonCreator;
import org.codehaus.jackson.annotate.JsonIgnore;
import org.codehaus.jackson.annotate.JsonProperty;
import static org.digitalpassport.api.IConstants.*;

/**
 *
 * @author Philip M. Trenwith
 */
public class cErrorData
{
  @JsonIgnore private String name;
  @JsonIgnore private String amount;
  
  @JsonCreator
  public static cErrorData of(@JsonProperty(g_sPARAM_NAME) String name,
      @JsonProperty(g_sPARAM_AMOUNT) String amount)
  {
    return new cErrorData(name, amount);
  }

  @JsonProperty(g_sPARAM_NAME)
  public String getname()
  {
    return name;
  }
  @JsonProperty(g_sPARAM_AMOUNT)
  public String getamount()
  {
    return amount;
  }

  public cErrorData(String name, String amount)
  {
    this.name = name;
    this.amount = amount;
  }

  @Override
  public String toString()
  {
    return "cErrorData{" + g_sPARAM_NAME + "=" + name + ", " + 
        g_sPARAM_AMOUNT + "=" + amount +'}';
  }
}
