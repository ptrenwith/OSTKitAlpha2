
package org.digitalpassport.deserialize.json.users.lists;

import org.codehaus.jackson.annotate.JsonCreator;
import org.codehaus.jackson.annotate.JsonProperty;
import static org.digitalpassport.api.IConstants.*;

/**
 *
 * @author Philip M. Trenwith
 */
public class cListUsersJson
{
  private String result_type;
  private cEconomyUserJson[] economy_users;
  private cMetaJson meta;
  
  @JsonCreator
  public static cListUsersJson of(
          @JsonProperty(g_sPARAM_RESULT_TYPE) String result_type,
          @JsonProperty(g_sPARAM_ECONOMY_USERS) cEconomyUserJson[] economy_users,
          @JsonProperty(g_sPARAM_META) cMetaJson meta)
  {
    return new cListUsersJson(result_type, economy_users, meta);
  }

  @JsonProperty(g_sPARAM_RESULT_TYPE)
  public String getresult_type()
  {
    return result_type;
  }
  @JsonProperty(g_sPARAM_ECONOMY_USERS)
  public cEconomyUserJson[] geteconomy_users()
  {
    return economy_users;
  }
  @JsonProperty(g_sPARAM_META)
  public cMetaJson getmeta()
  {
    return meta;
  }

  public cListUsersJson(String result_type, cEconomyUserJson[] economy_users, cMetaJson meta)
  {
    this.result_type = result_type;
    this.economy_users = economy_users;
    this.meta = meta;
  }

  @Override
  public String toString()
  {
    String sUsers = "";
    String sMeta = (meta==null)?"null":meta.toString();
    if (economy_users != null)
    {
      for (cEconomyUserJson oUser: economy_users)
      {
        sUsers += oUser.toString() +"\n";
      }
    }
    else 
    {
      sUsers = "null";
    }
    return "cListUsersJson{" + g_sPARAM_RESULT_TYPE + "=" + result_type + 
            ", " + g_sPARAM_ECONOMY_USERS + "=" + sUsers + ", " + g_sPARAM_META + "=" + sMeta + '}';
  }
  
}
