
package org.digitalpassport.deserialize.json;

import org.codehaus.jackson.annotate.JsonCreator;
import org.codehaus.jackson.annotate.JsonIgnore;
import org.codehaus.jackson.annotate.JsonProperty;
import static org.digitalpassport.api.IConstants.*;

/**
 *
 * @author Philip M. Trenwith
 */
public class cError
{
  private String code;
  @JsonIgnore private String msg;
  @JsonIgnore private cErrorData[] error_data;
  @JsonIgnore private String display_text;
  @JsonIgnore private String display_heading;
  
  @JsonCreator
  public static cError of(@JsonProperty(g_sPARAM_CODE) String code,
          @JsonProperty(g_sPARAM_MSG) String msg)//,
          //@JsonProperty(g_sPARAM_ERROR_DATA) cErrorData[] error_data)
  {
    return new cError(code, msg, null);
  }
  
  @JsonProperty(g_sPARAM_CODE)
  public String getcode()
  {
    return code;
  }
  @JsonProperty(g_sPARAM_MSG)
  public String getmsg()
  {
    return msg;
  }
  @JsonProperty(g_sPARAM_ERROR_DATA)
  public cErrorData[] geterror_data()
  {
    return error_data;
  }

  public cError(String code, String msg, cErrorData[] error_data)
  {
    this.code = code;
    this.msg = msg;
    this.error_data = error_data;
  }

  @Override
  public String toString()
  {
    return "cError{" + g_sPARAM_CODE + "=" + code + ", " + g_sPARAM_MSG + "=" + msg + ", " + g_sPARAM_ERROR_DATA + "=" + error_data + '}';
  }
}
