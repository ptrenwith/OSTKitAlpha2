
package org.digitalpassport.api.commands;

import java.io.IOException;
import java.util.TreeMap;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.codehaus.jackson.JsonFactory;
import org.codehaus.jackson.map.DeserializationConfig.Feature;
import org.codehaus.jackson.map.MappingJsonFactory;
import org.codehaus.jackson.map.ObjectMapper;
import static org.digitalpassport.api.IConstants.*;
import org.digitalpassport.api.cAPIClient;
import org.digitalpassport.deserialize.json.cResponse;

/**
 *
 * @author Philip M. Trenwith
 */
public class cUserManagement
{
  private static final JsonFactory m_oJsonFactory = new MappingJsonFactory();
  private static final ObjectMapper oMapper = new ObjectMapper(m_oJsonFactory);
  
  public cUserManagement()
  {
    oMapper.configure(Feature.ACCEPT_SINGLE_VALUE_AS_ARRAY, true);
  }
  
  public cResponse editUser(String sUUID, String sNewName)
  {
    cResponse oResponse = null;
    try
    {
      TreeMap oParameters = new TreeMap();
      oParameters.put(g_sPARAM_UUID, UUID.fromString(sUUID));
      oParameters.put(g_sPARAM_NAME, sNewName);
      
      String sResponse = cAPIClient.post(g_sPATH_USERS_EDIT, oParameters);
      
      oResponse = oMapper.readValue(sResponse, cResponse.class);
      System.out.println(oResponse.toString());
    }
    catch (IOException ex)
    {
      Logger.getLogger(cUserManagement.class.getName()).log(Level.SEVERE, null, ex);
    }
    return oResponse;
  }
  
  public cResponse listUsers(String sPage, String sFilter, String sOrderBy, String sOrder)
  {
    cResponse oResponse = null;
    try
    {
      TreeMap oParameters = new TreeMap();
      oParameters.put(g_sPARAM_PAGE_NO, sPage);
      oParameters.put(g_sPARAM_FILTER, sFilter);
      oParameters.put(g_sPARAM_ORDER_BY, sOrderBy);
      oParameters.put(g_sPARAM_ORDER, sOrder);
      
      String sResponse = cAPIClient.get(g_sPATH_USERS_LIST, oParameters);
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

  public cResponse createUser(String sName)
  {
    cResponse oResponse = null;
    try
    {
      TreeMap oParameters = new TreeMap();
      oParameters.put(g_sPARAM_NAME, sName);
      
      String sResponse = cAPIClient.post(g_sPATH_USERS_CREATE, oParameters);
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

  public cResponse sendAirdropTo(String sAirdropTo, int iAmount)
  {
    cResponse oResponse = null;
    try
    {
      TreeMap oParameters = new TreeMap();
      oParameters.put(g_sPARAM_LIST_TYPE, sAirdropTo);
      oParameters.put(g_sPARAM_AMOUNT, iAmount);
      
      String sResponse = cAPIClient.post(g_sPATH_USERS_AIRDROP_DROP, oParameters);
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
  
  public cResponse sendAirdropTo_sandbox(String sAirdropTo, int iAmount)
  {
    cResponse oResponse = null;
    try
    {
      TreeMap oParameters = new TreeMap();
      oParameters.put("user_ids", sAirdropTo);
      oParameters.put(g_sPARAM_AMOUNT, iAmount);
      
      String sResponse = cAPIClient.post_sandbox("/airdrops", oParameters);
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
  
  public cResponse airdropStatus(String sAirdropUUID)
  {
    cResponse oResponse = null;
    try
    {
      TreeMap oParameters = new TreeMap();
      oParameters.put(g_sPARAM_AIRDROP_UUID, sAirdropUUID);
      
      String sResponse = cAPIClient.post(g_sPATH_USERS_AIRDROP_STATUS, oParameters);
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
}
