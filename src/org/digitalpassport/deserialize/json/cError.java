
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
  private String msg;
  @JsonIgnore private String display_text;
  @JsonIgnore private String display_heading;
  private cErrorData[] error_data;
  
  @JsonCreator
  public static cError of(@JsonProperty(g_sPARAM_CODE) String code,
          @JsonProperty(g_sPARAM_MSG) String msg,
          @JsonProperty(g_sPARAM_DISPLAY_TEXT) String display_text,
          @JsonProperty(g_sPARAM_DISPLAY_HEADING) String display_heading,
          @JsonProperty(g_sPARAM_ERROR_DATA) cErrorData[] error_data)
  {
    return new cError(code, msg, display_heading, display_text, error_data);
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
  @JsonProperty(g_sPARAM_DISPLAY_TEXT)
  public String getdisplay_text()
  {
    return display_text;
  }
  @JsonProperty(g_sPARAM_DISPLAY_HEADING)
  public String getdisplay_heading()
  {
    return display_heading;
  }
  @JsonProperty(g_sPARAM_ERROR_DATA)
  public cErrorData[] geterror_data()
  {
    return error_data;
  }

  public cError(String code, String msg, String display_heading, String display_text, cErrorData[] error_data)
  {
    this.code = code;
    this.msg = msg;
    this.display_heading = display_heading;
    this.display_text = display_text;
    this.error_data = error_data;
  }

  @Override
  public String toString()
  {
    return "cError{" + "code=" + code + ", msg=" + msg + ", display_text=" + 
            display_text + ", display_heading=" + display_heading + ", error_data=" + error_data + '}';
  }
}
