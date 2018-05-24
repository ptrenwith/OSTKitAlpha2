package org.digitalpassport.deserialize.json.transactiontypes;

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
public class cTransactionTypes
{

  private String id;
  private String name;
  private String kind;
  @JsonIgnore  private String currency_type;
  @JsonIgnore private String currency;
  private String currency_value;
  @JsonIgnore private String amount;
  private String commission_percent;
  private String status;
  @JsonIgnore
  private String client_transaction_id;
  @JsonIgnore
  private long uts;

  @JsonCreator
  public static cTransactionTypes of(
      @JsonProperty(g_sPARAM_ID) String id,
      @JsonProperty(g_sPARAM_NAME) String name,
      @JsonProperty(g_sPARAM_KIND) String kind,
      @JsonProperty("currency_type") String currency_type1,
      @JsonProperty(g_sPARAM_CURRENCY_TYPE) String currency_type2,
      @JsonProperty(g_sPARAM_CURRENCY_VALUE) String currency_value,
      @JsonProperty(g_sPARAM_AMOUNT) String amount,
      @JsonProperty(g_sPARAM_COMMISSION_PERCENT) String commission_percent,
      @JsonProperty(g_sPARAM_STATUS) String status,
      @JsonProperty(g_sPARAM_CLIENT_TRANSACTION_ID) String client_transaction_id,
      @JsonProperty(g_sPARAM_TIMESTAMP) long uts)
  {
    String currency_type = (currency_type1 == null || currency_type1.isEmpty()) ? currency_type2 : currency_type1;
    return new cTransactionTypes(id, name, kind, currency_type, currency_value, amount,
        commission_percent, status, client_transaction_id, uts);
  }

  @JsonProperty(g_sPARAM_ID)
  public String getid()
  {
    return id;
  }

  @JsonProperty(g_sPARAM_CLIENT_TRANSACTION_ID)
  public String getclient_transaction_id()
  {
    return client_transaction_id;
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
  
  @JsonProperty(g_sPARAM_AMOUNT)
  public String getamount()
  {
    return amount;
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

  public cTransactionTypes(String id, String name, String kind,
      String currency_type, String currency_value, String amount, String commission_percent,
      String status, String client_transaction_id, long uts)
  {
    this.id = id;
    this.name = name;
    this.kind = kind;
    this.currency_type = currency_type;
    this.currency_value = currency_value;
    this.amount = amount;
    this.commission_percent = commission_percent;
    this.status = status;
    this.client_transaction_id = client_transaction_id;
    this.uts = uts;
  }

  @Override
  public String toString()
  {
    return "cTransactionTypes{" + "id=" + id + ", name=" + name + ", kind=" + kind
        + ", currency_type=" + currency_type + ", currency_value=" + currency_value
        + ", commission_percent=" + commission_percent + ", status=" + status
        + ", client_transaction_id=" + client_transaction_id + ", uts=" + uts + '}';
  }
}
