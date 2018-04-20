
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
  
  @JsonCreator
  public static cErrorData of(@JsonProperty(g_sPARAM_NAME) String name)
  {
    return new cErrorData(name);
  }

  @JsonProperty(g_sPARAM_NAME)
  public String getname()
  {
    return name;
  }

  public cErrorData(String name)
  {
    this.name = name;
  }

  @Override
  public String toString()
  {
    return "cErrorData{" + g_sPARAM_NAME + "=" + name + '}';
  }
}
