package org.digitalpassport.api.commands;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.TreeMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.codehaus.jackson.JsonFactory;
import org.codehaus.jackson.map.DeserializationConfig;
import org.codehaus.jackson.map.MappingJsonFactory;
import org.codehaus.jackson.map.ObjectMapper;
import static org.digitalpassport.api.IConstants.*;
import org.digitalpassport.api.cAPIClient;
import org.digitalpassport.deserialize.json.cResponse;
import org.digitalpassport.deserialize.json.transactiontypes.cClientTokens;
import org.digitalpassport.deserialize.json.transactiontypes.cTransactionTypes;
import org.digitalpassport.deserialize.json.users.lists.cEconomyUser;

/**
 *
 * @author Philip M. Trenwith
 */
public class cTransactionManagement
{

  private static final JsonFactory m_oJsonFactory = new MappingJsonFactory();
  private static final ObjectMapper oMapper = new ObjectMapper(m_oJsonFactory);
  public static final HashMap<String, cTransactionTypes> m_oTransactions = new HashMap();

  public cTransactionManagement()
  {
    oMapper.configure(DeserializationConfig.Feature.ACCEPT_SINGLE_VALUE_AS_ARRAY, true);
  }

  public cResponse createTransaction(String sName, eTransactionKind eKind, eCurrencyType eType,
      float value, float percent)
  {
    cResponse oResponse = null;
    try
    {
      TreeMap oParameters = new TreeMap();
      oParameters.put(g_sPARAM_NAME, sName);
      oParameters.put(g_sPARAM_KIND, eKind);
      oParameters.put(g_sPARAM_CURRENCY_TYPE, eType);
      oParameters.put(g_sPARAM_CURRENCY_VALUE, value);
      oParameters.put(g_sPARAM_COMMISSION_PERCENT, percent);

      String sResponse = cAPIClient.post(g_sPATH_TRANSACTION_TYPES_CREATE, oParameters);
      System.out.println(sResponse);
      oResponse = oMapper.readValue(sResponse, cResponse.class);
      System.out.println(oResponse.toString());
    }
    catch (IOException ex)
    {
      Logger.getLogger(cUserManagement.class.getName()).log(Level.SEVERE, null, ex);
    }
    return oResponse;
  }

  public cResponse createTransaction_sandbox(String sName, eTransactionKind eKind, eCurrencyType eType,
      float value, float percent)
  {
    cResponse oResponse = null;
    try
    {
      String sValue = "" + value;
      String sPercent = "" + percent;

      TreeMap oParameters = new TreeMap();
      oParameters.put("name", sName);
      oParameters.put("kind", eKind.name());
      oParameters.put("currency", eType.name());
      oParameters.put("arbitrary_amount", false);
      oParameters.put("amount", sValue);
      oParameters.put("arbitrary_commission", false);
      oParameters.put("commission_percent", sPercent);

      String sResponse = cAPIClient.post_sandbox("/actions", oParameters);
      System.out.println(sResponse);
      oResponse = oMapper.readValue(sResponse, cResponse.class);
      System.out.println(oResponse.toString());
    }
    catch (IOException ex)
    {
      Logger.getLogger(cUserManagement.class.getName()).log(Level.SEVERE, null, ex);
    }
    return oResponse;
  }

  public cResponse editTransaction_sandbox(String client_transaction_id,
      String sName, eTransactionKind eKind, eCurrencyType eType,
      float value, float percent)
  {
    cResponse oResponse = null;
    try
    {
      String sValue = "" + value;
      String sPercent = "" + percent;

      TreeMap oParameters = new TreeMap();
      oParameters.put("name", sName);
      oParameters.put("kind", eKind.name());
      oParameters.put("currency", eType.name());
      oParameters.put("arbitrary_amount", false);
      oParameters.put("amount", sValue);
      oParameters.put("arbitrary_commission", false);
      oParameters.put("commission_percent", sPercent);

      String sResponse = cAPIClient.post_sandbox("/actions/" + client_transaction_id, oParameters);
      System.out.println(sResponse);
      oResponse = oMapper.readValue(sResponse, cResponse.class);
      System.out.println(oResponse.toString());
    }
    catch (IOException ex)
    {
      Logger.getLogger(cUserManagement.class.getName()).log(Level.SEVERE, null, ex);
    }
    return oResponse;
  }

  public cResponse listTransactions()
  {
    cResponse oResponse = null;
    try
    {
      TreeMap oParameters = new TreeMap();
      String sResponse = cAPIClient.get(g_sPATH_TRANSACTION_TYPES_LIST, oParameters);
      System.out.println(sResponse);
      oResponse = oMapper.readValue(sResponse, cResponse.class);
      System.out.println(oResponse.toString());
    }
    catch (IOException ex)
    {
      Logger.getLogger(cUserManagement.class.getName()).log(Level.SEVERE, null, ex);
    }
    return oResponse;
  }

  public cResponse executeTransaction(String from_uuid,
      String to_uuid, String transaction_kind)
  {
    cResponse oResponse = null;
    try
    {
      TreeMap oParameters = new TreeMap();
      oParameters.put(g_sPARAM_TRANSACTION_FROM_UUID, from_uuid);
      oParameters.put(g_sPARAM_TRANSACTION_TO_UUID, to_uuid);
      oParameters.put(g_sPARAM_TRANSACTION_KIND, transaction_kind);

      String sResponse = cAPIClient.post(g_sPATH_TRANSACTION_TYPES_EXECUTE, oParameters);
      System.out.println(sResponse);
      oResponse = oMapper.readValue(sResponse, cResponse.class);
      System.out.println(oResponse.toString());
    }
    catch (IOException ex)
    {
      Logger.getLogger(cUserManagement.class.getName()).log(Level.SEVERE, null, ex);
    }
    return oResponse;
  }

  public cResponse executeTransaction_sandbox(String from_uuid,
      String to_uuid, String sActionId)
  {
    cResponse oResponse = null;
    try
    {
      TreeMap oParameters = new TreeMap();
      oParameters.put("from_user_id", from_uuid);
      oParameters.put("to_user_id", to_uuid);
      oParameters.put("action_id", sActionId);

      String sResponse = cAPIClient.post_sandbox("/transactions", oParameters);
      System.out.println(sResponse);
      oResponse = oMapper.readValue(sResponse, cResponse.class);
      System.out.println(oResponse.toString());
    }
    catch (IOException ex)
    {
      Logger.getLogger(cUserManagement.class.getName()).log(Level.SEVERE, null, ex);
    }
    return oResponse;
  }

  public cResponse getTransactionStatus(String sTransactionId)
  {
    cResponse oResponse = null;
    try
    {
      TreeMap oParameters = new TreeMap();
      oParameters.put(g_sPARAM_TRANSACTION_UUIDS, sTransactionId);

      String sResponse = cAPIClient.post(g_sPATH_TRANSACTION_TYPES_STATUS, oParameters);
      System.out.println(sResponse);
      cClientTokens oClientTokens = null;
      {
        int indexOf = sResponse.indexOf("client_tokens\":");
        if (indexOf > 0)
        {
          int iStartBracket1 = sResponse.indexOf("{", indexOf + "client_tokens:".length() + 2);
          int iBracket1 = sResponse.indexOf("}", indexOf + "client_tokens:".length() + 2);
          int iBracket2 = sResponse.indexOf("}", iBracket1 + 1);
          String client_tokens = sResponse.substring(iStartBracket1, iBracket2);
          String sOld_client_tokens = sResponse.substring(indexOf, iStartBracket1);
          client_tokens = client_tokens.replace(sOld_client_tokens, "client_tokens\": [{\n");
          oClientTokens = oMapper.readValue(client_tokens, cClientTokens.class);
        }
      }

      cTransactionTypes oTransactionTypes = null;
      {
        int indexOf = sResponse.indexOf("transaction_types\":{");
        int iStartBracket1 = sResponse.indexOf("{", indexOf + "transaction_types:{".length() + 1);
        int iBracket1 = sResponse.indexOf("}", indexOf + "transaction_types:{".length() + 1);
        int iBracket2 = sResponse.indexOf("}", iBracket1 + 1);
        String transaction_types = sResponse.substring(iStartBracket1, iBracket2);
        String sOld_transaction_types = sResponse.substring(indexOf, iStartBracket1);
        transaction_types = transaction_types.replace(sOld_transaction_types, "transaction_types\": [{\n");
        oTransactionTypes = oMapper.readValue(transaction_types, cTransactionTypes.class);
      }

      cEconomyUser oEconomyUser = null;
      cEconomyUser oEconomyUser2 = null;
      {
        int indexOf = sResponse.indexOf("economy_users\":{");
        int iStartBracket1 = sResponse.indexOf("{", indexOf + "economy_users:{".length() + 1);
        int iBracket1 = sResponse.indexOf("}", indexOf + "economy_users:{".length() + 1);
        String economy_users = "" + sResponse.substring(iStartBracket1, iBracket1) + "}";
        oEconomyUser = oMapper.readValue(economy_users, cEconomyUser.class);

        indexOf = iBracket1;
        iStartBracket1 = sResponse.indexOf("{", indexOf + "economy_users:{".length() + 1);
        iBracket1 = sResponse.indexOf("}", indexOf + "economy_users:{".length() + 1);
        economy_users = "" + sResponse.substring(iStartBracket1, iBracket1) + "}";
        oEconomyUser2 = oMapper.readValue(economy_users, cEconomyUser.class);
      }
      ArrayList lsUsers = new ArrayList();
      lsUsers.add(oEconomyUser);
      lsUsers.add(oEconomyUser2);

      oResponse = oMapper.readValue(sResponse, cResponse.class);
      oResponse.getdata().setClientToken(oClientTokens);
      oResponse.getdata().setTransactionType(oTransactionTypes);
      oResponse.getdata().setEconomyUsers(lsUsers);
      System.out.println(oResponse.toString());
    }
    catch (IOException ex)
    {
      Logger.getLogger(cUserManagement.class.getName()).log(Level.SEVERE, null, ex);
    }
    return oResponse;
  }
}
