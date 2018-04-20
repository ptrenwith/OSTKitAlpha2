
package org.digitalpassport.deserialize.json.users.lists;

import org.codehaus.jackson.annotate.JsonCreator;
import org.codehaus.jackson.annotate.JsonProperty;
import static org.digitalpassport.api.IConstants.*;

/**
 *
 * @author Philip M. Trenwith
 */
public class cNextPagePayloadJson
{
  private String order_by;
  private String order;
  private String filter;
  private String page_no;
  
  @JsonCreator
  public static cNextPagePayloadJson of(@JsonProperty(g_sPARAM_ORDER_BY) String order_by,
          @JsonProperty(g_sPARAM_ORDER) String order, @JsonProperty(g_sPARAM_FILTER) String filter,
          @JsonProperty(g_sPARAM_PAGE_NO) String page_no)
  {
    return new cNextPagePayloadJson(order_by, order, filter, page_no);
  }

  @JsonProperty(g_sPARAM_ORDER_BY)
  public String getOrder_by()
  {
    return order_by;
  }
  @JsonProperty(g_sPARAM_ORDER)
  public String getOrder()
  {
    return order;
  }
  @JsonProperty(g_sPARAM_FILTER)
  public String getFilter()
  {
    return filter;
  }
  @JsonProperty(g_sPARAM_PAGE_NO)
  public String getPage_no()
  {
    return page_no;
  }

  @Override
  public String toString()
  {
    return "cNextPagePayloadJson{" + g_sPARAM_ORDER_BY + "=" + order_by + ", " + g_sPARAM_ORDER + "=" + order + 
            ", " + g_sPARAM_FILTER + "=" + filter + ", " + g_sPARAM_PAGE_NO + "=" + page_no + '}';
  }

  public cNextPagePayloadJson(String order_by, String order, String filter, String page_no)
  {
    this.order_by = order_by;
    this.order = order;
    this.filter = filter;
    this.page_no = page_no;
  }
}
