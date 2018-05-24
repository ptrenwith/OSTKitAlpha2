package org.digitalpassport.deserialize.json;

import java.util.List;
import org.codehaus.jackson.annotate.JsonCreator;
import org.codehaus.jackson.annotate.JsonIgnore;
import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.codehaus.jackson.annotate.JsonProperty;
import static org.digitalpassport.api.IConstants.*;
import org.digitalpassport.deserialize.json.transactiontypes.cClientTokens;
import org.digitalpassport.deserialize.json.transactiontypes.cPricePoints;
import org.digitalpassport.deserialize.json.transactiontypes.cTransaction;
import org.digitalpassport.deserialize.json.transactiontypes.cTransactionTypes;
import org.digitalpassport.deserialize.json.users.lists.cEconomyUser;
import org.digitalpassport.deserialize.json.users.lists.cMeta;
import org.digitalpassport.deserialize.json.users.lists.cUser;

/**
 *
 * @author Philip M. Trenwith
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class cData
{

  // parameters of airdrop 
  @JsonIgnore
  private String airdrop_uuid;
  @JsonIgnore
  private String current_status;
  @JsonIgnore
  private String[] steps_complete;

  // parameters for transactions
  @JsonIgnore
  private cTransaction transaction;
  @JsonIgnore
  private cTransactionTypes[] actions;
  @JsonIgnore
  private cTransactionTypes action;
  @JsonIgnore
  private cUser[] users;
  @JsonIgnore
  private cPricePoints price_points;
  @JsonIgnore
  private cClientTokens client_tokens;
  @JsonIgnore
  private int client_id;
  @JsonIgnore
  private String transaction_uuid;
  @JsonIgnore
  private String transaction_hash;
  @JsonIgnore
  private String from_uuid;
  @JsonIgnore
  private String to_uuid;
  @JsonIgnore
  private String transaction_kind;

  // paramters of list users
  private String result_type;
  private cEconomyUser[] economy_users;
  private cMeta meta;

  @JsonCreator
  public static cData of(@JsonProperty(g_sPARAM_CLIENT_ID) int client_id,
      @JsonProperty(g_sPARAM_AIRDROP_UUID) String airdrop_uuid,
      @JsonProperty(g_sPARAM_CURRENT_STATUS) String current_status,
      @JsonProperty(g_sPARAM_STEPS_COMPLETED) String[] steps_complete,
      @JsonProperty(g_sPARAM_RESULT_TYPE) String result_type,
      @JsonProperty(g_sPARAM_TRANSACTION) cTransaction transaction,
      @JsonProperty("actions") cTransactionTypes[] actions,
      @JsonProperty(g_sPARAM_TRANSACTION_UUID) String transaction_uuid,
      @JsonProperty(g_sPARAM_TRANSACTION_HASH) String transaction_hash,
      @JsonProperty(g_sPARAM_TRANSACTION_FROM_UUID) String from_uuid,
      @JsonProperty(g_sPARAM_TRANSACTION_TO_UUID) String to_uuid,
      @JsonProperty(g_sPARAM_TRANSACTION_KIND) String transaction_kind,
      @JsonProperty(g_sPARAM_PRICE_POINTS) cPricePoints price_points,
      @JsonProperty(g_sPARAM_CLIENT_TOKENS) cClientTokens client_tokens,
      @JsonProperty(g_sPARAM_ECONOMY_USERS) cEconomyUser[] economy_users,
      @JsonProperty(g_sPARAM_META) cMeta meta,
      @JsonProperty(g_sPARAM_ACTION) cTransactionTypes action,
      @JsonProperty("users") cUser[] users)
  {
    return new cData(client_id, airdrop_uuid, current_status, steps_complete,
        result_type, transaction, actions, transaction_uuid,
        transaction_hash, from_uuid, to_uuid, transaction_kind,
        price_points, client_tokens, economy_users, meta, action, users);
  }

  @JsonProperty(g_sPARAM_CLIENT_ID)
  public int getclient_id()
  {
    return client_id;
  }

  @JsonProperty(g_sPARAM_AIRDROP_UUID)
  public String getairdrop_uuid()
  {
    return airdrop_uuid;
  }

  @JsonProperty(g_sPARAM_CURRENT_STATUS)
  public String getcurrent_status()
  {
    return current_status;
  }

  @JsonProperty(g_sPARAM_STEPS_COMPLETED)
  public String[] getsteps_complete()
  {
    return steps_complete;
  }

  @JsonProperty(g_sPARAM_RESULT_TYPE)
  public String getresult_type()
  {
    return result_type;
  }

  @JsonProperty(g_sPARAM_TRANSACTION)
  public cTransaction gettransaction()
  {
    return transaction;
  }

  @JsonProperty("actions")
  public cTransactionTypes[] gettransaction_types()
  {
    return actions;
  }
  
  @JsonProperty(g_sPARAM_ACTION)
  public cTransactionTypes getaction()
  {
    return action;
  }

  @JsonProperty(g_sPARAM_TRANSACTION_UUID)
  public String gettransaction_uuid()
  {
    return transaction_uuid;
  }

  @JsonProperty(g_sPARAM_TRANSACTION_HASH)
  public String gettransaction_hash()
  {
    return transaction_hash;
  }

  @JsonProperty(g_sPARAM_TRANSACTION_FROM_UUID)
  public String getfrom_uuid()
  {
    return from_uuid;
  }

  @JsonProperty(g_sPARAM_TRANSACTION_TO_UUID)
  public String getto_uuid()
  {
    return to_uuid;
  }

  @JsonProperty(g_sPARAM_TRANSACTION_KIND)
  public String gettransaction_kind()
  {
    return transaction_kind;
  }

  @JsonProperty(g_sPARAM_PRICE_POINTS)
  public cPricePoints getprice_points()
  {
    return price_points;
  }

  @JsonProperty(g_sPARAM_CLIENT_TOKENS)
  public cClientTokens getclient_tokens()
  {
    return client_tokens;
  }

  @JsonProperty(g_sPARAM_ECONOMY_USERS)
  public cEconomyUser[] geteconomy_users()
  {
    return economy_users;
  }

  @JsonProperty(g_sPARAM_META)
  public cMeta getmeta()
  {
    return meta;
  }
  
  @JsonProperty("users")
  public cUser[] getusers()
  {
    return users;
  }

  public cData(int client_id, String airdrop_uuid, String current_status,
      String[] steps_complete, String result_type, cTransaction transaction,
      cTransactionTypes[] actions, String transaction_uuid,
      String transaction_hash, String from_uuid, String to_uuid,
      String transaction_kind, cPricePoints price_points, cClientTokens client_tokens,
      cEconomyUser[] economy_users, cMeta meta, cTransactionTypes action, cUser[] users)
  {
    this.client_id = client_id;
    this.airdrop_uuid = airdrop_uuid;
    this.current_status = current_status;
    this.steps_complete = steps_complete;
    this.result_type = result_type;
    this.transaction = transaction;
    this.actions = actions;
    this.transaction_uuid = transaction_uuid;
    this.transaction_hash = transaction_hash;
    this.from_uuid = from_uuid;
    this.to_uuid = to_uuid;
    this.transaction_kind = transaction_kind;
    this.client_tokens = client_tokens;
    this.economy_users = economy_users;
    this.price_points = price_points;
    this.meta = meta;
    this.action = action;
    this.users = users;
  }

  @Override
  public String toString()
  {
    String sUsers = "";
    String sMeta = (meta == null) ? "null" : meta.toString();
    if (economy_users != null)
    {
      for (cEconomyUser oUser : economy_users)
      {
        sUsers += oUser.toString() + "\n";
      }
    }
    else
    {
      sUsers = "null";
    }

    return "cData{" + "airdrop_uuid=" + airdrop_uuid + ", current_status=" + current_status
        + ", steps_complete=" + steps_complete + ", transaction=" + transaction
        + ", actions=" + actions + ", price_points=" + price_points
        + ", client_tokens=" + client_tokens + ", client_id=" + client_id
        + ", transaction_uuid=" + transaction_uuid + ", transaction_hash=" + transaction_hash
        + ", from_uuid=" + from_uuid + ", to_uuid=" + to_uuid + ", transaction_kind=" + transaction_kind
        + ", result_type=" + result_type + ", economy_users=" + sUsers + ", meta=" + sMeta + '}';
  }

  public void setEconomyUsers(List<cEconomyUser> lsUsers)
  {
    economy_users = new cEconomyUser[lsUsers.size()];
    economy_users = lsUsers.toArray(economy_users);
  }

  public void setClientToken(cClientTokens oClientTokens)
  {
    client_tokens = oClientTokens;
  }

  public void setTransactionType(cTransactionTypes oTransactionTypes)
  {
    actions = new cTransactionTypes[1];
    actions[0] = oTransactionTypes;
  }
}
