
package org.digitalpassport.deserialize.json.transactiontypes;

import org.codehaus.jackson.annotate.JsonCreator;
import org.codehaus.jackson.annotate.JsonIgnore;
import org.codehaus.jackson.annotate.JsonProperty;
import static org.digitalpassport.api.IConstants.*;

/**
 *
 * @author Philip M. Trenwith
 */
public class cTransaction
{
  private String id;
  private String client_id;
  private String name;
  private String kind;
  private String currency_type;
  private String currency_value;
  private String commission_percent;
  private String status;
  private long uts;
  
  @JsonIgnore private String transaction_uuid;
  @JsonIgnore private String transaction_hash;
  @JsonIgnore private String from_user_id;
  @JsonIgnore private String to_user_id;
  @JsonIgnore private String transaction_type_id;
  @JsonIgnore private String client_token_id;
  @JsonIgnore private String gas_price;
  @JsonIgnore private String gas_used;
  @JsonIgnore private String transaction_timestamp;
  @JsonIgnore private String transaction_fee;
  @JsonIgnore private String block_number;
  @JsonIgnore private String bt_transfer_value;
  @JsonIgnore private String bt_commission_amount;
  
  @JsonCreator
  public static cTransaction of(
          @JsonProperty(g_sPARAM_ID) String id,
          @JsonProperty(g_sPARAM_CLIENT_ID) String client_id,
          @JsonProperty(g_sPARAM_NAME) String name,
          @JsonProperty(g_sPARAM_KIND) String kind,
          @JsonProperty(g_sPARAM_CURRENCY_TYPE) String currency_type,
          @JsonProperty(g_sPARAM_CURRENCY_VALUE) String currency_value,
          @JsonProperty(g_sPARAM_COMMISSION_PERCENT) String commission_percent,
          @JsonProperty(g_sPARAM_STATUS) String status,
          @JsonProperty(g_sPARAM_TIMESTAMP) long uts,
          @JsonProperty(g_sPARAM_TRANSACTION_UUID) String transaction_uuid,
          @JsonProperty(g_sPARAM_TRANSACTION_HASH) String transaction_hash,
          @JsonProperty(g_sPARAM_FROM_USER_ID) String from_user_id,
          @JsonProperty(g_sPARAM_TO_USER_ID) String to_user_id,
          @JsonProperty(g_sPARAM_TRANSACTION_TYPE_ID) String transaction_type_id,
          @JsonProperty(g_sPARAM_CLIENT_TOKEN_ID) String client_token_id,
          @JsonProperty(g_sPARAM_GAS_PRICE) String gas_price,
          @JsonProperty(g_sPARAM_GAS_USED) String gas_used,
          @JsonProperty(g_sPARAM_TRANSACTION_TIMESTAMP) String transaction_timestamp,
          @JsonProperty(g_sPARAM_TRANSACTION_FEE) String transaction_fee,
          @JsonProperty(g_sPARAM_BLOCK_NUMBER) String block_number,
          @JsonProperty(g_sPARAM_BT_TRANSFER_VALUE) String bt_transfer_value,
          @JsonProperty(g_sPARAM_BT_COMMISSION_AMOUNT) String bt_commission_amount
          )
  {
    return new cTransaction(id, client_id, name, kind, currency_type, 
            currency_value, commission_percent, status, uts, transaction_uuid,
            transaction_hash, transaction_timestamp, transaction_fee, transaction_type_id, 
            from_user_id, to_user_id, block_number, client_token_id, gas_price, gas_used, 
            bt_transfer_value, bt_commission_amount);
  }
  
  @JsonProperty(g_sPARAM_ID)
  public String getid()
  {
    return id;
  }
  @JsonProperty(g_sPARAM_CLIENT_ID)
  public String getclient_id()
  {
    return client_id;
  }
  @JsonProperty(g_sPARAM_NAME)
  public String getname()
  {
    return name;
  }
  @JsonProperty(g_sPARAM_KIND)
  public String getkind()
  {
    return kind;
  }
  @JsonProperty(g_sPARAM_CURRENCY_TYPE)
  public String getcurrency_type()
  {
    return currency_type;
  }
  @JsonProperty(g_sPARAM_CURRENCY_VALUE)
  public String getcurrency_value()
  {
    return currency_value;
  }
  @JsonProperty(g_sPARAM_COMMISSION_PERCENT)
  public String getcommission_percent()
  {
    return commission_percent;
  }
  @JsonProperty(g_sPARAM_STATUS)
  public String getstatus()
  {
    return status;
  }
  @JsonProperty(g_sPARAM_TIMESTAMP)
  public long getuts()
  {
    return uts;
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
  public String getfrom_user_id()
  {
    return from_user_id;
  }
  @JsonProperty(g_sPARAM_TO_USER_ID)
  public String getto_user_id()
  {
    return to_user_id;
  }
  @JsonProperty(g_sPARAM_TRANSACTION_TYPE_ID)
  public String gettransaction_type_id()
  {
    return transaction_type_id;
  }
  @JsonProperty(g_sPARAM_CLIENT_TOKEN_ID)
  public String getclient_token_id()
  {
    return client_token_id;
  }
  @JsonProperty(g_sPARAM_GAS_PRICE)
  public String getgas_price()
  {
    return gas_price;
  }
  @JsonProperty(g_sPARAM_GAS_USED)
  public String getgas_used()
  {
    return gas_used;
  }
  @JsonProperty(g_sPARAM_TRANSACTION_TIMESTAMP)
  public String gettransaction_timestamp()
  {
    return transaction_timestamp;
  }
  @JsonProperty(g_sPARAM_TRANSACTION_FEE)
  public String gettransaction_fee()
  {
    return transaction_fee;
  }
  @JsonProperty(g_sPARAM_BLOCK_NUMBER)
  public String getblock_number()
  {
    return block_number;
  }
  @JsonProperty(g_sPARAM_BT_TRANSFER_VALUE)
  public String getbt_transfer_value()
  {
    return bt_transfer_value;
  }
  @JsonProperty(g_sPARAM_BT_COMMISSION_AMOUNT)
  public String getbt_commission_amount()
  {
    return bt_commission_amount;
  }
  
  public cTransaction(String id, String client_id, String name, String kind,
          String currency_type, String currency_value, String commission_percent,
          String status, long uts, String transaction_uuid, String transaction_hash,
          String transaction_timestamp, String transaction_fee, 
          String transaction_type_id, String from_user_id, String to_user_id, 
          String block_number, String client_token_id, String gas_price, 
          String gas_used, String bt_transfer_value, String bt_commission_amount)
  {
    this.id = id;
    this.client_id = client_id;
    this.name = name;
    this.kind = kind;
    this.currency_type = currency_type;
    this.currency_value = currency_value;
    this.commission_percent = commission_percent;
    this.status = status;
    this.uts = uts;
    this.transaction_uuid = transaction_uuid;
    this.transaction_hash = transaction_hash;
    this.transaction_timestamp = transaction_timestamp;
    this.transaction_fee = transaction_fee;
    this.transaction_type_id = transaction_type_id;
    this.from_user_id = from_user_id;
    this.to_user_id = to_user_id;
    this.block_number = block_number;
    this.client_token_id = client_token_id;
    this.gas_price = gas_price;
    this.gas_used = gas_used;
    this.bt_transfer_value = bt_transfer_value;
    this.bt_commission_amount = bt_commission_amount;
  }

  @Override
  public String toString()
  {
    return "cTransaction{" + "id=" + id + ", client_id=" + client_id + 
            ", name=" + name + ", kind=" + kind + ", currency_type=" + currency_type + 
            ", currency_value=" + currency_value + ", commission_percent=" + commission_percent + 
            ", status=" + status + ", uts=" + uts + ", transaction_uuid=" + transaction_uuid + 
            ", transaction_hash=" + transaction_hash + ", from_user_id=" + from_user_id + 
            ", to_user_id=" + to_user_id + ", transaction_type_id=" + transaction_type_id + 
            ", client_token_id=" + client_token_id + ", gas_price=" + gas_price + 
            ", gas_used=" + gas_used + ", transaction_timestamp=" + transaction_timestamp + 
            ", transaction_fee=" + transaction_fee + ", block_number=" + block_number + 
            ", bt_transfer_value=" + bt_transfer_value + ", bt_commission_amount=" + bt_commission_amount + '}';
  }
}
