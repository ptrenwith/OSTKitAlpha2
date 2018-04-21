/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.digitalpassport.deserialize.json;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.codehaus.jackson.JsonFactory;
import org.codehaus.jackson.map.DeserializationConfig;
import org.codehaus.jackson.map.MappingJsonFactory;
import org.codehaus.jackson.map.ObjectMapper;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Philip
 */
public class cErrorTest
{
  private static final JsonFactory m_oJsonFactory = new MappingJsonFactory();
  private static final ObjectMapper oMapper = new ObjectMapper(m_oJsonFactory);
  
  public cErrorTest()
  {
    oMapper.configure(DeserializationConfig.Feature.ACCEPT_SINGLE_VALUE_AS_ARRAY, true);
  }
  
  @BeforeClass
  public static void setUpClass()
  {
  }
  
  @AfterClass
  public static void tearDownClass()
  {
  }
  
  @Before
  public void setUp()
  {
  }
  
  @After
  public void tearDown()
  {
  }

  @Test
  public void testUnauthorized()
  {
    try
    {
      String sResponse = "{\n" +
"  \"success\": false,\n" +
"  \"err\": {\n" +
"    \"code\": \"companyRestFulApi(401:HJg2HK0A_f)\",\n" +
"    \"msg\": \"Unauthorized\",\n" +
"    \"error_data\": {}\n" +
"  }\n" +
"}";
      
      cResponse oResponse = oMapper.readValue(sResponse, cResponse.class);
      System.out.println(oResponse.toString());
      assertEquals(1, 1);
    }
    catch (IOException ex)
    {
      Logger.getLogger(cErrorTest.class.getName()).log(Level.SEVERE, null, ex);
      fail("fail.");
    }
  }
  
  @Test
  public void testAirdropDrop()
  {
    try
    {
      String sResponse = "{\n" +
          " \"success\": false,\n" +
          " \"err\": {\n" +
          "   \"code\": \"companyRestFulApi(s_am_sa_7:HypBvRPFM)\",\n" +
          "   \"msg\": \"Insufficient funds to airdrop users\",\n" +
          "   \"display_text\": \"\",\n" +
          "   \"display_heading\": \"\",\n" +
          "   \"error_data\": [\n" +
          "     {\n" +
          "       \"amount\": \"Available token amount is insufficient. Please mint more tokens or reduce the amount to complete the process.\"\n" +
          "     }\n" +
          "   ]\n" +
          " },\n" +
          " \"data\": {\n" +
          " }\n" +
          "}";
      
      cResponse oResponse = oMapper.readValue(sResponse, cResponse.class);
      System.out.println(oResponse.toString());
      assertEquals(1, 1);
    }
    catch (IOException ex)
    {
      Logger.getLogger(cErrorTest.class.getName()).log(Level.SEVERE, null, ex);
      fail("fail.");
    }
    
  }
  
}
