
package org.digitalpassport.deserialize.json.users.lists;

import org.codehaus.jackson.annotate.JsonCreator;
import org.codehaus.jackson.annotate.JsonProperty;
import static org.digitalpassport.api.IConstants.*;

/**
 *
 * @author Philip M. Trenwith
 */
public class cMeta
{
  private cNextPagePayload oNextPagePayload;
  
  @JsonCreator
  public static cMeta of(
          @JsonProperty(g_sPARAM_NEXT_PAGE) cNextPagePayload oNextPagePayload)
  {
    return new cMeta(oNextPagePayload);
  }
  @JsonProperty(g_sPARAM_NEXT_PAGE)
  public cNextPagePayload getoNextPagePayload()
  {
    return oNextPagePayload;
  }

  @Override
  public String toString()
  {
    return "cMeta{" + g_sPARAM_NEXT_PAGE + "=" + oNextPagePayload.toString() + '}';
  }
  
  public cMeta(cNextPagePayload oNextPagePayload)
  {
    this.oNextPagePayload = oNextPagePayload;
  }
}
