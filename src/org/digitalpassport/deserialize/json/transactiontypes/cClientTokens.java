
package org.digitalpassport.deserialize.json.transactiontypes;

import org.codehaus.jackson.annotate.JsonCreator;
import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.codehaus.jackson.annotate.JsonProperty;
import static org.digitalpassport.api.IConstants.*;

/**
 *
 * @author Philip M. Trenwith
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class cClientTokens
{
  private String id;
  private String client_id;
  private String name;
  private String symbol;
  private String symbol_icon;
  private String conversion_factor;
  private String token_erc20_address;
  private String airdrop_contract_addr;
  private String simple_stake_contract_addr;

  @JsonCreator
  public static cClientTokens of(
          @JsonProperty(g_sPARAM_ID) String id,
          @JsonProperty(g_sPARAM_CLIENT_ID) String client_id,
          @JsonProperty(g_sPARAM_NAME) String name,
          @JsonProperty(g_sPARAM_SYMBOL) String symbol,
          @JsonProperty(g_sPARAM_SYMBOL_ICON) String symbol_icon,
          @JsonProperty(g_sPARAM_CONV_FACTOR) String conversion_factor,
          @JsonProperty(g_sPARAM_TOKEN_ERC20_ADDR) String token_erc20_address,
          @JsonProperty(g_sPARAM_AIRDROP_CONTRACT_ADDR) String airdrop_contract_addr,
          @JsonProperty(g_sPARAM_SIMPLE_STAKE_CONTRACT_ADDR) String simple_stake_contract_addr)
  {
    return new cClientTokens(id, client_id, name, symbol, symbol_icon, 
            conversion_factor, token_erc20_address, airdrop_contract_addr, 
            simple_stake_contract_addr);
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
  @JsonProperty(g_sPARAM_SYMBOL)
  public String getsymbol()
  {
    return symbol;
  }
  @JsonProperty(g_sPARAM_SYMBOL_ICON)
  public String getsymbol_icon()
  {
    return symbol_icon;
  }
  @JsonProperty(g_sPARAM_CONV_FACTOR)
  public String getconversion_factor()
  {
    return conversion_factor;
  }
  @JsonProperty(g_sPARAM_TOKEN_ERC20_ADDR)
  public String gettoken_erc20_address()
  {
    return token_erc20_address;
  }
  @JsonProperty(g_sPARAM_AIRDROP_CONTRACT_ADDR)
  public String getairdrop_contract_addr()
  {
    return airdrop_contract_addr;
  }
  @JsonProperty(g_sPARAM_SIMPLE_STAKE_CONTRACT_ADDR)
  public String getsimple_stake_contract_addr()
  {
    return simple_stake_contract_addr;
  }

  public cClientTokens(String id, String client_id, String name, String symbol, 
          String symbol_icon, String conversion_factor, String token_erc20_address, 
          String airdrop_contract_addr, String simple_stake_contract_addr)
  {
    this.id = id;
    this.client_id = client_id;
    this.name = name;
    this.symbol = symbol;
    this.symbol_icon = symbol_icon;
    this.conversion_factor = conversion_factor;
    this.token_erc20_address = token_erc20_address;
    this.airdrop_contract_addr = airdrop_contract_addr;
    this.simple_stake_contract_addr = simple_stake_contract_addr;
  }

  @Override
  public String toString()
  {
    return "cClientTokens{" + "id=" + id + ", client_id=" + client_id + ", name=" + name + 
            ", symbol=" + symbol + ", symbol_icon=" + symbol_icon + 
            ", conversion_factor=" + conversion_factor + 
            ", token_erc20_address=" + token_erc20_address + 
            ", airdrop_contract_addr=" + airdrop_contract_addr + 
            ", simple_stake_contract_addr=" + simple_stake_contract_addr + '}';
  }
  
  
}
