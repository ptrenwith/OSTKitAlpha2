
package org.digitalpassport.deserialize.json.users.lists;

import org.codehaus.jackson.annotate.JsonCreator;
import org.codehaus.jackson.annotate.JsonIgnore;
import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.codehaus.jackson.annotate.JsonProperty;
import static org.digitalpassport.api.IConstants.g_sPARAM_ID;
import static org.digitalpassport.api.IConstants.g_sPARAM_NAME;

/**
 *
 * @author Philip M. Trenwith
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class cUser
{
  @JsonIgnore
  private String id;
  @JsonIgnore
  private String[][] addresses;
  @JsonIgnore
  private String name;
  @JsonIgnore
  private String airdropped_tokens;
  @JsonIgnore
  private String token_balance;

  @JsonCreator
  public static cUser of(
      @JsonProperty(g_sPARAM_ID) String id,
      @JsonProperty("addresses") String[][] addresses,
      @JsonProperty(g_sPARAM_NAME) String name,
      @JsonProperty("airdropped_tokens") String airdropped_tokens,
      @JsonProperty("token_balance") String token_balance)
  {
    return new cUser(id, addresses, name, airdropped_tokens, token_balance);
  }

  @JsonProperty(g_sPARAM_ID)
  public String getId()
  {
    return id;
  }

  @JsonProperty("addresses")
  public String[][] getaddresses()
  {
    return addresses;
  }

  @JsonProperty(g_sPARAM_NAME)
  public String getname()
  {
    return name;
  }

  @JsonProperty("airdropped_tokens")
  public String getairdropped_tokens()
  {
    return airdropped_tokens;
  }

  @JsonProperty("token_balance")
  public String gettoken_balance()
  {
    return token_balance;
  }

  public cUser(String id, String[][] addresses, String name, String airdropped_tokens,
      String token_balance)
  {
    this.id = id;
    this.addresses = addresses;
    this.name = name;
    this.airdropped_tokens = airdropped_tokens;
    this.token_balance = token_balance;
  }

  @Override
  public String toString()
  {
    return "cEconomyUser{" + "id=" + id + ", addresses=" + addresses + ", name=" + name
        + ", airdropped_tokens=" + airdropped_tokens + ", token_balance="
        + token_balance + '}';
  }
}
