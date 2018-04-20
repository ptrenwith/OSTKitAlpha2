
package org.digitalpassport.deserialize.json;

import org.codehaus.jackson.annotate.JsonCreator;
import org.codehaus.jackson.annotate.JsonIgnore;
import org.codehaus.jackson.annotate.JsonProperty;
import static org.digitalpassport.api.IConstants.*;
import org.digitalpassport.deserialize.json.users.lists.cListUsersJson;

/**
 *
 * @author Philip M. Trenwith
 */
public class cResponse
{
  private boolean success;
  @JsonIgnore private cListUsersJson data;
  @JsonIgnore private cError err;
  
  @JsonCreator
  public static cResponse of(@JsonProperty(g_sPARAM_SUCCESS) boolean success,
          @JsonProperty(g_sPARAM_DATA) cListUsersJson data,
          @JsonProperty(g_sPARAM_ERROR) cError err)
  {
    return new cResponse(success, data, err);
  }

  @JsonProperty(g_sPARAM_SUCCESS)
  public boolean getsuccess()
  {
    return success;
  }
  @JsonProperty(g_sPARAM_DATA)
  public cListUsersJson getdata()
  {
    return data;
  }
  @JsonProperty(g_sPARAM_ERROR)
  public cError geterr()
  {
    return err;
  }

  public cResponse(boolean success, cListUsersJson data, cError err)
  {
    this.success = success;
    this.data = data;
    this.err = err;
  }

  @Override
  public String toString()
  {
    String sData = (data!=null)?data.toString():"null";
    return "cResponse{" + g_sPARAM_SUCCESS + "=" + success + ", " + g_sPARAM_DATA + 
            "=" + sData + ", " + g_sPARAM_ERROR + "=" + err + '}';
  }
}
