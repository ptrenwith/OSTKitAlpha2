/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.digitalpassport.deserialize.json;

import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.codehaus.jackson.JsonFactory;
import org.codehaus.jackson.map.DeserializationConfig;
import org.codehaus.jackson.map.MappingJsonFactory;
import org.codehaus.jackson.map.ObjectMapper;
import org.digitalpassport.deserialize.json.transactiontypes.cClientTokens;
import org.digitalpassport.deserialize.json.transactiontypes.cTransactionTypes;
import org.digitalpassport.deserialize.json.users.lists.cEconomyUser;
import org.junit.After;
import org.junit.AfterClass;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

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
  public void testCreateUserSuccess()
  {
    try
    {
      String sResponse = "{\n" +
      "  \"success\": true,\n" +
      "  \"data\": {\n" +
      "    \"result_type\": \"economy_users\",\n" +
      "    \"economy_users\": [\n" +
      "      {\n" +
      "        \"id\": \"574b456d-5da6-4353-ad7c-9b70893e757b\",\n" +
      "        \"uuid\": \"574b456d-5da6-4353-ad7c-9b70893e757b\",\n" +
      "        \"name\": \"NAME\",\n" +
      "        \"total_airdropped_tokens\": 0,\n" +
      "        \"token_balance\": 0\n" +
      "      }\n" +
      "    ],\n" +
      "    \"meta\": {\n" +
      "      \"next_page_payload\": {}\n" +
      "    }\n" +
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
  public void testCreateUserFailure1()
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
  public void testCreateUserFailure2()
  {
    try
    {
      String sResponse = "{\n" +
      "  \"success\": false,\n" +
      "  \"err\": {\n" +
      "    \"code\": \"companyRestFulApi(s_a_g_1:rJndQJkYG)\",\n" +
      "    \"msg\": \"invalid params\",\n" +
      "    \"error_data\": [\n" +
      "      {\n" +
      "        \"name\": \"Only letters, numbers and spaces allowed. (Max 20 characters)\"\n" +
      "      }\n" +
      "    ]\n" +
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
  public void testEditUserSuccess()
  {
    try
    {
      String sResponse = "{\n" +
      "  \"success\": true,\n" +
      "  \"data\": {\n" +
      "    \"result_type\": \"economy_users\",\n" +
      "    \"economy_users\": [\n" +
      "      {\n" +
      "        \"id\": \"2f5f6388-fb0e-4812-929f-f37e5ebbfd50\",\n" +
      "        \"uuid\": \"2f5f6388-fb0e-4812-929f-f37e5ebbfd50\",\n" +
      "        \"name\": \"NAME\",\n" +
      "        \"total_airdropped_tokens\": \"0\",\n" +
      "        \"token_balance\": \"0\"\n" +
      "      }\n" +
      "    ],\n" +
      "    \"meta\": {\n" +
      "      \"next_page_payload\": {}\n" +
      "    }\n" +
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
  public void testEditUserFailure()
  {
    try
    {
      String sResponse = "{\n" +
      "  \"success\": false,\n" +
      "  \"err\": {\n" +
      "    \"code\": \"companyRestFulApi(s_cu_eu_2.1:rJOpl4JtG)\",\n" +
      "    \"msg\": \"User not found\",\n" +
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
  public void testUserListSuccess()
  {
    try
    {
      String sResponse = "{\n" +
      "  \"success\": true,\n" +
      "  \"data\": {\n" +
      "    \"result_type\": \"economy_users\",\n" +
      "    \"economy_users\": [\n" +
      "      {\n" +
      "        \"id\": \"c1e5da9b-787d-4897-aa58-742f2756c71d\",\n" +
      "        \"name\": \"User 1\",\n" +
      "        \"uuid\": \"c1e5da9b-787d-4897-aa58-742f2756c71d\",\n" +
      "        \"total_airdropped_tokens\": \"15\",\n" +
      "        \"token_balance\": \"15\"\n" +
      "      },\n" +
      "      {\n" +
      "        \"id\": \"461c10ea-2b6c-42e8-9fea-b997995cdf8b\",\n" +
      "        \"name\": \"User 25\",\n" +
      "        \"uuid\": \"461c10ea-2b6c-42e8-9fea-b997995cdf8b\",\n" +
      "        \"total_airdropped_tokens\": \"15\",\n" +
      "        \"token_balance\": \"15\"\n" +
      "      }\n" +
      "    ],\n" +
      "    \"meta\": {\n" +
      "      \"next_page_payload\": {\n" +
      "        \"order_by\": \"creation_time\",\n" +
      "        \"order\": \"asc\",\n" +
      "        \"filter\": \"all\",\n" +
      "        \"page_no\": 2\n" +
      "      }\n" +
      "    }\n" +
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
  public void testAirdropDropSuccess()
  {
    try
    {
      String sResponse = "{\n" +
      " \"success\": true,\n" +
      " \"data\": {\n" +
      "   \"airdrop_uuid\": \"cbc20092-7326-4517-b851-ec211e3ced7d\"\n" +
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
  
  @Test
  public void testAirdropDropFailure()
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
  
  @Test
  public void testAirdropStatusSuccess()
  {
    try
    {
      String sResponse = "{\n" +
      " \"success\": true,\n" +
      " \"data\": {\n" +
      "   \"airdrop_uuid\": \"5412c48e-2bec-4224-9305-56be99174f54\",\n" +
      "   \"current_status\": \"complete\",\n" +
      "   \"steps_complete\": [\n" +
      "     \"users_identified\",\n" +
      "     \"tokens_transfered\",\n" +
      "     \"contract_approved\",\n" +
      "     \"allocation_done\"\n" +
      "   ]\n" +
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
  
  @Test
  public void testAirdropStatusFailure()
  {
    try
    {
      String sResponse = "{\n" +
          "\"success\": false,\n" +
          " \"err\": {\n" +
          "   \"code\": \"companyRestFulApi(s_am_gas_1:SJy641uFG)\",\n" +
          "   \"msg\": \"Invalid Airdrop Request Id.\",\n" +
          "   \"display_text\": \"\",\n" +
          "   \"display_heading\": \"\",\n" +
          "   \"error_data\": {\n" +
          "   }\n" +
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
  
  @Test
  public void testCreateTransactionSuccess()
  {
    try
    {
      String sResponse = "{\n" +
        "  \"success\": true,\n" +
        "  \"data\": {\n" +
        "    \"result_type\": \"transactions\",\n" +
        "    \"transactions\": [\n" +
        "      {\n" +
        "        \"id\": 10170,\n" +
        "        \"client_id\": 20373,\n" +
        "        \"name\": \"Upvote\",\n" +
        "        \"kind\": \"user_to_user\",\n" +
        "        \"currency_type\": \"USD\",\n" +
        "        \"currency_value\": \"0.2\",\n" +
        "        \"commission_percent\": \"0.1\",\n" +
        "        \"status\": \"active\",\n" +
        "        \"uts\": 1520179969832\n" +
        "      }\n" +
        "    ]\n" +
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
  public void testCreateTransactionFailure()
  {
    try
    {
      String sResponse = "{\n" +
        "  \"success\": false,\n" +
        "  \"err\": {\n" +
        "    \"code\": \"companyRestFulApi(s_a_g_1:rJndQJkYG)\",\n" +
        "    \"msg\": \"invalid params\",\n" +
        "    \"error_data\": [\n" +
        "      {\n" +
        "        \"name\": \"Transaction-types name \\\"Upvote\\\" already present.\"\n" +
        "      }\n" +
        "    ]\n" +
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
  public void testEditTransaction()
  {
    try
    {
      String sResponse = "{\n" +
        "  \"success\": true,\n" +
        "  \"data\": {\n" +
        "    \"result_type\": \"transactions\",\n" +
        "    \"transactions\": [\n" +
        "      {\n" +
        "        \"id\": \"20198\",\n" +
        "        \"client_id\": 1018,\n" +
        "        \"name\": \"Reward\",\n" +
        "        \"kind\": \"company_to_user\",\n" +
        "        \"currency_type\": \"BT\",\n" +
        "        \"currency_value\": \"0.1\",\n" +
        "        \"commission_percent\": \"0\",\n" +
        "        \"uts\": 1520876285325\n" +
        "      }\n" +
        "    ]\n" +
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
  public void testTransactionList()
  {
    try
    {
      String sResponse = "{\n" +
        "  \"success\": true,\n" +
        "  \"data\": {\n" +
        "    \"client_id\": 1018,\n" +
        "    \"result_type\": \"transaction_types\",\n" +
        "    \"transaction_types\": [\n" +
        "      {\n" +
        "        \"id\": \"20216\",\n" +
        "        \"client_transaction_id\": \"20216\",\n" +
        "        \"name\": \"Upvote\",\n" +
        "        \"kind\": \"user_to_user\",\n" +
        "        \"currency_type\": \"USD\",\n" +
        "        \"currency_value\": \"0.20000\",\n" +
        "        \"commission_percent\": \"0.1\",\n" +
        "        \"status\": \"active\"\n" +
        "      },\n" +
        "      {\n" +
        "        \"id\": \"20221\",\n" +
        "        \"client_transaction_id\": \"20221\",\n" +
        "        \"name\": \"Download\",\n" +
        "        \"kind\": \"user_to_company\",\n" +
        "        \"currency_type\": \"USD\",\n" +
        "        \"currency_value\": \"0.10000\",\n" +
        "        \"commission_percent\": \"0\",\n" +
        "        \"status\": \"active\"\n" +
        "      }\n" +
        "    ],\n" +
        "    \"meta\": {\n" +
        "      \"next_page_payload\": {}\n" +
        "    },\n" +
        "    \"price_points\": {\n" +
        "      \"OST\": {\n" +
        "        \"USD\": \"0.197007\"\n" +
        "      }\n" +
        "    },\n" +
        "    \"client_tokens\": {\n" +
        "      \"client_id\": 1018,\n" +
        "      \"name\": \"ACME\",\n" +
        "      \"symbol\": \"ACM\",\n" +
        "      \"symbol_icon\": \"token_icon_6\",\n" +
        "      \"conversion_factor\": \"0.21326\",\n" +
        "      \"token_erc20_address\": \"0xEa1c45D934d287fec813C74021A5d692278bE5e9\",\n" +
        "      \"airdrop_contract_addr\": \"0xaA5460105E39184B5e43a925bf8Da17EED64BE68\",\n" +
        "      \"simple_stake_contract_addr\": \"0xf892f80567A97C54b2852316c0F2cA5eb186a0AD\"\n" +
        "    }\n" +
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
  public void testTransactionTypeExecute()
  {
    try
    {
      String sResponse = "{\n" +
        "  \"success\": true,\n" +
        "  \"data\": {\n" +
        "    \"transaction_uuid\": \"49cc3411-7ab3-4478-8fac-beeab09e3ed2\",\n" +
        "    \"transaction_hash\": null,\n" +
        "    \"from_uuid\": \"1b5039ea-323f-416c-9007-7fe2d068d42d\",\n" +
        "    \"to_uuid\": \"286d2cb9-421b-495d-8a82-034d8e2c96e2\",\n" +
        "    \"transaction_kind\": \"Download\"\n" +
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
  public void testTransactionTypeStatus()
  {
    try
    {
      String sResponse = "{\n" +
        "  \"success\": true,\n" +
        "  \"data\": {\n" +
        "    \"client_tokens\": {\n" +
        "      \"30117\": {\n" +
        "        \"id\": \"30117\",\n" +
        "        \"client_id\": 1124,\n" +
        "        \"name\": \"hkedgrd 3\",\n" +
        "        \"symbol\": \"ghpi\",\n" +
        "        \"symbol_icon\": \"token_icon_4\",\n" +
        "        \"conversion_factor\": \"0.03085\",\n" +
        "        \"airdrop_contract_addr\": \"0x3afd9f2273af535c513c2a35f56aF1Fe65E1dBaA\",\n" +
        "        \"uts\": 1520182157543\n" +
        "      }\n" +
        "    },\n" +
        "    \"transaction_types\": {\n" +
        "      \"20334\": {\n" +
        "        \"id\": \"20334\",\n" +
        "        \"name\": \"Reward\",\n" +
        "        \"kind\": \"company_to_user\",\n" +
        "        \"currency_type\": \"BT\",\n" +
        "        \"currency_value\": \"5\",\n" +
        "        \"commission_percent\": \"0.00\",\n" +
        "        \"status\": \"active\",\n" +
        "        \"uts\": 1520182157546\n" +
        "      }\n" +
        "    },\n" +
        "    \"economy_users\": {\n" +
        "      \"ae5b9aa6-a45d-439a-bb22-027df78727a1\": {\n" +
        "        \"id\": \"ae5b9aa6-a45d-439a-bb22-027df78727a1\",\n" +
        "        \"uuid\": \"ae5b9aa6-a45d-439a-bb22-027df78727a1\",\n" +
        "        \"name\": \"\",\n" +
        "        \"kind\": \"reserve\",\n" +
        "        \"uts\": 1520182157551\n" +
        "      },\n" +
        "      \"91af390d-843d-44eb-b554-5ad01f874eba\": {\n" +
        "        \"id\": \"91af390d-843d-44eb-b554-5ad01f874eba\",\n" +
        "        \"uuid\": \"91af390d-843d-44eb-b554-5ad01f874eba\",\n" +
        "        \"name\": \"User 4\",\n" +
        "        \"kind\": \"user\",\n" +
        "        \"uts\": 1520182157551\n" +
        "      }\n" +
        "    },\n" +
        "    \"result_type\": \"transactions\",\n" +
        "    \"transactions\": [\n" +
        "      {\n" +
        "        \"id\": \"4bc71630-c131-4b8d-814a-33184d1e6fe1\",\n" +
        "        \"transaction_uuid\": \"4bc71630-c131-4b8d-814a-33184d1e6fe1\",\n" +
        "        \"from_user_id\": \"ae5b9aa6-a45d-439a-bb22-027df78727a1\",\n" +
        "        \"to_user_id\": \"91af390d-843d-44eb-b554-5ad01f874eba\",\n" +
        "        \"transaction_type_id\": \"20334\",\n" +
        "        \"client_token_id\": 30117,\n" +
        "        \"transaction_hash\": \"0xe945362504b20eab78b51fdc9e699686eabf3089d40ea57fe552d147ab11f1ba\",\n" +
        "        \"status\": \"complete\",\n" +
        "        \"gas_price\": \"0x12A05F200\",\n" +
        "        \"transaction_timestamp\": 1520165780,\n" +
        "        \"uts\": 1520182157540,\n" +
        "        \"gas_used\": \"99515\",\n" +
        "        \"transaction_fee\": \"0.000497575\",\n" +
        "        \"block_number\": 213100,\n" +
        "        \"bt_transfer_value\": \"5\",\n" +
        "        \"bt_commission_amount\": \"0\"\n" +
        "      }\n" +
        "    ]\n" +
        "  }\n" +
        "}";
      
      cClientTokens oClientTokens = null;
      {
        int indexOf = sResponse.indexOf("client_tokens\": {\n");
        int iStartBracket1 = sResponse.indexOf("{", indexOf + "client_tokens: {\n".length());
        int iBracket1 = sResponse.indexOf("}", indexOf + "client_tokens: {\n".length());
        int iBracket2 = sResponse.indexOf("}", iBracket1+1);
        String client_tokens = sResponse.substring(iStartBracket1, iBracket2);
        String sOld_client_tokens = sResponse.substring(indexOf, iStartBracket1);
        client_tokens = client_tokens.replace(sOld_client_tokens, "client_tokens\": [{\n");
        oClientTokens = oMapper.readValue(client_tokens, cClientTokens.class);
      }
      
      cTransactionTypes oTransactionTypes = null;
      {
        int indexOf = sResponse.indexOf("transaction_types\": {\n");
        int iStartBracket1 = sResponse.indexOf("{", indexOf + "transaction_types: {\n".length());
        int iBracket1 = sResponse.indexOf("}", indexOf + "transaction_types: {\n".length());
        int iBracket2 = sResponse.indexOf("}", iBracket1+1);
        String transaction_types = sResponse.substring(iStartBracket1, iBracket2);
        String sOld_transaction_types = sResponse.substring(indexOf, iStartBracket1);
        transaction_types = transaction_types.replace(sOld_transaction_types, "transaction_types\": [{\n");
        oTransactionTypes = oMapper.readValue(transaction_types, cTransactionTypes.class);
      }
      
      cEconomyUser oEconomyUser = null;
      cEconomyUser oEconomyUser2 = null;
      {
        int indexOf = sResponse.indexOf("economy_users\": {\n");
        int iStartBracket1 = sResponse.indexOf("{", indexOf + "economy_users: {\n".length());
        int iBracket1 = sResponse.indexOf("}", indexOf + "economy_users: {\n".length());
        String economy_users = "" + sResponse.substring(iStartBracket1, iBracket1) + "}";
        oEconomyUser = oMapper.readValue(economy_users, cEconomyUser.class);
        System.out.println(oEconomyUser.toString());

        indexOf = iBracket1;
        iStartBracket1 = sResponse.indexOf("{", indexOf + "economy_users: {\n".length());
        iBracket1 = sResponse.indexOf("}", indexOf + "economy_users: {\n".length());
        economy_users = "" + sResponse.substring(iStartBracket1, iBracket1) + "}";
        oEconomyUser2 = oMapper.readValue(economy_users, cEconomyUser.class);
        System.out.println(oEconomyUser2.toString());
      }
      ArrayList lsUsers = new ArrayList();
      lsUsers.add(oEconomyUser);
      lsUsers.add(oEconomyUser2);
              
      cResponse oResponse = oMapper.readValue(sResponse, cResponse.class);
      oResponse.getdata().setClientToken(oClientTokens);
      oResponse.getdata().setTransactionType(oTransactionTypes);
      oResponse.getdata().setEconomyUsers(lsUsers);
      assertEquals(1, 1);
    }
    catch (IOException ex)
    {
      Logger.getLogger(cErrorTest.class.getName()).log(Level.SEVERE, null, ex);
      fail("fail.");
    }
  }
}
