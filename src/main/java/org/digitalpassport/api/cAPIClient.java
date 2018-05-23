package org.digitalpassport.api;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientHandlerException;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.UniformInterfaceException;
import com.sun.jersey.api.client.WebResource;
import java.io.IOException;
import java.util.Date;
import java.util.Iterator;
import java.util.Set;
import java.util.TreeMap;
import javax.ws.rs.core.MediaType;
import org.codehaus.jackson.JsonFactory;
import org.codehaus.jackson.map.MappingJsonFactory;
import org.codehaus.jackson.map.ObjectMapper;
import org.digitalpassport.nodejs.cNodeJsInterface;
import static org.digitalpassport.api.IConstants.*;

/**
 *
 * @author Philip M. Trenwith
 */
public class cAPIClient
{

  public static String get(String sEndpoint, TreeMap oParameters)
  {
    return api_call(sEndpoint, oParameters, false);
  }

  public static String post(String sEndpoint, TreeMap oParameters)
  {
    return api_call(sEndpoint, oParameters, true);
  }

  private static String api_call(String sEndpoint, TreeMap oParameters, boolean POST)
  {
    String sResponse = "";
    try
    {
      oParameters.put(g_sPARAM_API_KEY, API_KEY);
      oParameters.put(g_sPARAM_REQ_TIMESTAMP, getTimestamp_sec());

      if (oParameters.containsKey(g_sPARAM_NAME))
      {
        String sName = (oParameters.get(g_sPARAM_NAME) + "").replaceAll(" ", "+");
        oParameters.put(g_sPARAM_NAME, sName);
      }

      String sStringToHash = sEndpoint + "?" + TreeMapToString(oParameters);
      System.out.println("StringToHash: " + sStringToHash);
      String sSignature = cNodeJsInterface.hash(sStringToHash);

      if (oParameters.containsKey(g_sPARAM_NAME))
      {
        String sName = (oParameters.get(g_sPARAM_NAME) + "").replaceAll("\\+", " ");
        oParameters.put(g_sPARAM_NAME, sName);
        System.out.println("Name: " + sName);
      }

      oParameters.put(g_sPARAM_SIGNATURE, sSignature);
      Client oClient = Client.create();
      ClientResponse oResponse;

      if (POST)
      {
        JsonFactory oJsonFactory = new MappingJsonFactory();
        WebResource oWebResource = oClient.resource(m_sURL + sEndpoint);
        ObjectMapper oMapper = new ObjectMapper(oJsonFactory);
        String sParameters = oMapper.writeValueAsString(oParameters);

        System.out.println("POST URL: " + m_sURL + sEndpoint);
        System.out.println("POST PARAMETERS: " + sParameters);
        sResponse = oWebResource.accept(MediaType.APPLICATION_JSON)
            .entity(sParameters, MediaType.APPLICATION_JSON)
            .post(String.class);
        System.out.println("POST RESPONSE: " + sResponse);
      }
      else
      {
        String sURL = m_sURL + sEndpoint + "?" + TreeMapToString(oParameters);
        System.out.println("GET URL: " + sURL);

        WebResource webResource = oClient.resource(sURL);
        oResponse = webResource.accept("application/json").get(ClientResponse.class);
        if (oResponse.getStatus() != 200)
        {
          System.err.println("Failed : HTTP error code : " + oResponse.getStatus() + " " + oResponse.toString());
        }

        sResponse = oResponse.getEntity(String.class);
      }
    }
    catch (ClientHandlerException | IOException ex)
    {
      System.err.println("Exception: " + ex.getMessage());
      ex.printStackTrace();
    }
    catch (UniformInterfaceException uie)
    {
      sResponse = uie.getResponse().getEntity(String.class);
    }
    return sResponse;
  }

  public static long getTimestamp_sec()
  {
    Date date = new Date(System.currentTimeMillis());
    long lEpochSec = date.getTime() / 1000l;
    return lEpochSec;
  }

  private static String TreeMapToString(TreeMap oParameters)
  {
    String sParameters = "";
    Set oParamKeys = oParameters.keySet();
    Iterator oIterator = oParamKeys.iterator();
    boolean bFirst = true;
    while (oIterator.hasNext())
    {
      String sKey = oIterator.next() + "";
      if (bFirst)
      {
        bFirst = false;
        sParameters += sKey + "=" + oParameters.get(sKey);
      }
      else
      {
        sParameters += "&" + sKey + "=" + oParameters.get(sKey);
      }
    }
    return sParameters;
  }

  public static String post_sandbox(String sEndpoint, TreeMap oParameters)
  {
    return api_call_sandbox(sEndpoint, oParameters, true);
  }

  private static String api_call_sandbox(String sEndpoint, TreeMap oParameters, boolean POST)
  {
    String sResponse = "";
    try
    {
      oParameters.put(g_sPARAM_API_KEY, API_KEY);
      oParameters.put(g_sPARAM_REQ_TIMESTAMP, getTimestamp_sec());

      if (oParameters.containsKey(g_sPARAM_NAME))
      {
        String sName = (oParameters.get(g_sPARAM_NAME) + "").replaceAll(" ", "+");
        oParameters.put(g_sPARAM_NAME, sName);
      }

      String sStringToHash = sEndpoint + "?" + TreeMapToString(oParameters);
      System.out.println("StringToHash: " + sStringToHash);
      String sSignature = cNodeJsInterface.hash(sStringToHash);

      if (oParameters.containsKey(g_sPARAM_NAME))
      {
        String sName = (oParameters.get(g_sPARAM_NAME) + "").replaceAll("\\+", " ");
        oParameters.put(g_sPARAM_NAME, sName);
        System.out.println("Name: " + sName);
      }

      oParameters.put(g_sPARAM_SIGNATURE, sSignature);
      Client oClient = Client.create();
      ClientResponse oResponse;

      if (POST)
      {
        JsonFactory oJsonFactory = new MappingJsonFactory();
        WebResource oWebResource = oClient.resource(m_sSANDBOX_URL + sEndpoint);
        ObjectMapper oMapper = new ObjectMapper(oJsonFactory);
        String sParameters = oMapper.writeValueAsString(oParameters);

        System.out.println("POST URL: " + m_sSANDBOX_URL + sEndpoint);
        System.out.println("POST PARAMETERS: " + sParameters);
        sResponse = oWebResource.accept(MediaType.APPLICATION_JSON)
            .entity(sParameters, MediaType.APPLICATION_JSON)
            .post(String.class);
        System.out.println("POST RESPONSE: " + sResponse);
      }
      else
      {
        String sURL = m_sURL + sEndpoint + "?" + TreeMapToString(oParameters);
        System.out.println("GET URL: " + sURL);

        WebResource webResource = oClient.resource(sURL);
        oResponse = webResource.accept("application/json").get(ClientResponse.class);
        if (oResponse.getStatus() != 200)
        {
          System.err.println("Failed : HTTP error code : " + oResponse.getStatus() + " " + oResponse.toString());
        }

        sResponse = oResponse.getEntity(String.class);
      }
    }
    catch (ClientHandlerException | IOException ex)
    {
      System.err.println("Exception: " + ex.getMessage());
      ex.printStackTrace();
    }
    catch (UniformInterfaceException uie)
    {
      sResponse = uie.getResponse().getEntity(String.class);
    }
    return sResponse;
  }
}
