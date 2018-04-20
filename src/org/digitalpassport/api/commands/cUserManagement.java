
package org.digitalpassport.api.commands;

import java.io.IOException;
import java.net.URLEncoder;
import java.util.TreeMap;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.codehaus.jackson.JsonFactory;
import org.codehaus.jackson.map.MappingJsonFactory;
import org.codehaus.jackson.map.ObjectMapper;
import static org.digitalpassport.api.IConstants.*;
import org.digitalpassport.api.cAPIClient;
import org.digitalpassport.deserialize.json.cResponse;
import org.digitalpassport.deserialize.json.users.lists.cListUsersJson;

/**
 *
 * @author Philip M. Trenwith
 */
public class cUserManagement
{
  private static final JsonFactory m_oJsonFactory = new MappingJsonFactory();

  public cResponse editUser(String sUUID, String sNewName)
  {
    cResponse oResponse = null;
    try
    {
      TreeMap oParameters = new TreeMap();
      oParameters.put(g_sPARAM_UUID, UUID.fromString(sUUID));
      oParameters.put(g_sPARAM_NAME, sNewName);
      
      String sResponse = cAPIClient.post(g_sPATH_USERS_EDIT, oParameters);
      ObjectMapper oMapper = new ObjectMapper(m_oJsonFactory);
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
      ObjectMapper oMapper = new ObjectMapper(m_oJsonFactory);
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
      ObjectMapper oMapper = new ObjectMapper(m_oJsonFactory);
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
