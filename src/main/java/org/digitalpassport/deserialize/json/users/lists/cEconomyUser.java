package org.digitalpassport.deserialize.json.users.lists;

import org.codehaus.jackson.annotate.JsonCreator;
import org.codehaus.jackson.annotate.JsonIgnore;
import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.codehaus.jackson.annotate.JsonProperty;
import static org.digitalpassport.api.IConstants.*;

/**
 *
 * @author Philip M. Trenwith
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class cEconomyUser
{

  private String id;
  private String uuid;
  private String name;
  @JsonIgnore
  private String kind;
  @JsonIgnore
  private String total_airdropped_tokens;
  @JsonIgnore
  private String token_balance;
  @JsonIgnore
  private long uts;

  @JsonCreator
  public static cEconomyUser of(
      @JsonProperty(g_sPARAM_ID) String id,
      @JsonProperty(g_sPARAM_UUID) String uuid,
      @JsonProperty(g_sPARAM_NAME) String name,
      @JsonProperty(g_sPARAM_KIND) String kind,
      @JsonProperty(g_sPARAM_TOKENS_AIRDROPPED) String total_airdropped_tokens,
      @JsonProperty(g_sPARAM_TOKEN_BALANCE) String token_balance,
      @JsonProperty(g_sPARAM_TIMESTAMP) long uts)
  {
    return new cEconomyUser(id, uuid, name, kind, total_airdropped_tokens, token_balance, uts);
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

  @JsonProperty(g_sPARAM_KIND)
  public String getkind()
  {
    return kind;
  }

  @JsonProperty(g_sPARAM_TIMESTAMP)
  public long getuts()
  {
    return uts;
  }

  public cEconomyUser(String id, String uuid, String name, String kind,
      String total_airdropped_tokens, String token_balance, long uts)
  {
    this.id = id;
    this.uuid = uuid;
    this.name = name;
    this.kind = kind;
    this.total_airdropped_tokens = total_airdropped_tokens;
    this.token_balance = token_balance;
    this.uts = uts;
  }

  @Override
  public String toString()
  {
    return "cEconomyUser{" + "id=" + id + ", uuid=" + uuid + ", name=" + name
        + ", kind=" + kind + ", total_airdropped_tokens="
        + total_airdropped_tokens + ", token_balance=" + token_balance
        + ", uts=" + uts + '}';
  }

}
