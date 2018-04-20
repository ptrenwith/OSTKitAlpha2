
package org.digitalpassport.deserialize.json.users.lists;

import org.codehaus.jackson.annotate.JsonCreator;
import org.codehaus.jackson.annotate.JsonProperty;
import static org.digitalpassport.api.IConstants.*;

/**
 *
 * @author Philip M. Trenwith
 */
public class cEconomyUserJson
{
  private String id;
  private String name;
  private String uuid;
  private String total_airdropped_tokens;
  private String token_balance;
  
  @JsonCreator
  public static cEconomyUserJson of(@JsonProperty(g_sPARAM_ID) String id,
          @JsonProperty(g_sPARAM_NAME) String name, @JsonProperty(g_sPARAM_UUID) String uuid,
          @JsonProperty(g_sPARAM_TOKENS_AIRDROPPED) String total_airdropped_tokens,
          @JsonProperty(g_sPARAM_TOKEN_BALANCE) String token_balance)
  {
    return new cEconomyUserJson(id, name, uuid, total_airdropped_tokens, token_balance);
  }
  
  @JsonProperty(g_sPARAM_ID)
  public String getId()
  {
    return id;
  }
  @JsonProperty(g_sPARAM_NAME)
  public String getName()
  {
    return name;
  }
  @JsonProperty(g_sPARAM_UUID)
  public String getUuid()
  {
    return uuid;
  }
  @JsonProperty(g_sPARAM_TOKENS_AIRDROPPED)
  public String getTotal_airdropped_tokens()
  {
    return total_airdropped_tokens;
  }
  @JsonProperty(g_sPARAM_TOKEN_BALANCE)
  public String getToken_balance()
  {
    return token_balance;
  }

  @Override
  public String toString()
  {
    return "cEconomyUserJson{" + g_sPARAM_ID + "=" + id + ", " + g_sPARAM_NAME + "=" + name + ", " + g_sPARAM_UUID + "=" + uuid + 
            ", " + g_sPARAM_TOKENS_AIRDROPPED + "=" + total_airdropped_tokens + ", " + g_sPARAM_TOKEN_BALANCE + "=" + token_balance + '}';
  }  

  public cEconomyUserJson(String id, String name, String uuid, String total_airdropped_tokens, String token_balance)
  {
    this.id = id;
    this.name = name;
    this.uuid = uuid;
    this.total_airdropped_tokens = total_airdropped_tokens;
    this.token_balance = token_balance;
  }

}
