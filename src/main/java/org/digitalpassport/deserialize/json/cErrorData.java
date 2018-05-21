
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
  @JsonIgnore private String parameter;
  @JsonIgnore private String msg;
  
  @JsonCreator
  public static cErrorData of(@JsonProperty(g_sPARAM_NAME) String name,
      @JsonProperty(g_sPARAM_AMOUNT) String amount,
      @JsonProperty(g_sPARAM_PARAMETER) String parameter,
      @JsonProperty(g_sPARAM_MSG) String msg)
  {
    return new cErrorData(name, amount, parameter, msg);
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
  @JsonProperty(g_sPARAM_PARAMETER)
  public String getparameter()
  {
    return parameter;
  }
  @JsonProperty(g_sPARAM_MSG)
  public String getmsg()
  {
    return msg;
  }

  public cErrorData(String name, String amount, String parameter, String msg)
  {
    this.name = name;
    this.amount = amount;
    this.parameter = parameter;
    this.msg = msg;
  }

  @Override
  public String toString()
  {
    return "cErrorData{" + g_sPARAM_NAME + "=" + name + ", " + 
        g_sPARAM_AMOUNT + "=" + amount + ", " + g_sPARAM_PARAMETER + "=" + parameter + 
            ", " + g_sPARAM_MSG + "=" + msg + '}';
  }
}
