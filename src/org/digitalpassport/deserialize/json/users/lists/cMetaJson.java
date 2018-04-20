
package org.digitalpassport.deserialize.json.users.lists;

import org.codehaus.jackson.annotate.JsonCreator;
import org.codehaus.jackson.annotate.JsonProperty;
import static org.digitalpassport.api.IConstants.*;

/**
 *
 * @author Philip M. Trenwith
 */
public class cMetaJson
{
  private cNextPagePayloadJson oNextPagePayload;
  
  @JsonCreator
  public static cMetaJson of(
          @JsonProperty(g_sPARAM_NEXT_PAGE) cNextPagePayloadJson oNextPagePayload)
  {
    return new cMetaJson(oNextPagePayload);
  }
  @JsonProperty(g_sPARAM_NEXT_PAGE)
  public cNextPagePayloadJson getoNextPagePayload()
  {
    return oNextPagePayload;
  }

  @Override
  public String toString()
  {
    return "cMetaJson{" + g_sPARAM_NEXT_PAGE + "=" + oNextPagePayload.toString() + '}';
  }
  
  public cMetaJson(cNextPagePayloadJson oNextPagePayload)
  {
    this.oNextPagePayload = oNextPagePayload;
  }
}
