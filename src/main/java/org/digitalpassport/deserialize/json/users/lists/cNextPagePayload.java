package org.digitalpassport.deserialize.json.users.lists;

import org.codehaus.jackson.annotate.JsonCreator;
import org.codehaus.jackson.annotate.JsonProperty;
import static org.digitalpassport.api.IConstants.*;

/**
 *
 * @author Philip M. Trenwith
 */
public class cNextPagePayload
{

  private String order_by;
  private String order;
  private String filter;
  private String page_no;
  private int limit;

  @JsonCreator
  public static cNextPagePayload of(@JsonProperty(g_sPARAM_ORDER_BY) String order_by,
      @JsonProperty(g_sPARAM_ORDER) String order, @JsonProperty(g_sPARAM_FILTER) String filter,
      @JsonProperty(g_sPARAM_PAGE_NO) String page_no,
      @JsonProperty(g_sPARAM_LIMIT) int limit)
  {
    return new cNextPagePayload(order_by, order, filter, page_no, limit);
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

  @JsonProperty(g_sPARAM_LIMIT)
  public int getLimit()
  {
    return limit;
  }

  @Override
  public String toString()
  {
    return "cNextPagePayload{" + g_sPARAM_ORDER_BY + "=" + order_by + ", " + g_sPARAM_ORDER + "=" + order
        + ", " + g_sPARAM_FILTER + "=" + filter + ", " + g_sPARAM_PAGE_NO + "=" + page_no + ", "
        + g_sPARAM_LIMIT + "=" + limit + '}';
  }

  public cNextPagePayload(String order_by, String order, String filter, String page_no, int limit)
  {
    this.order_by = order_by;
    this.order = order;
    this.filter = filter;
    this.page_no = page_no;
    this.limit = limit;
  }
}
