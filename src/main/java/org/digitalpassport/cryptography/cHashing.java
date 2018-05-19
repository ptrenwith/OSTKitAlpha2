package org.digitalpassport.cryptography;

import java.security.MessageDigest;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.binary.Hex;
/**
 *
 * @author Philip M. Trenwith
 */
public class cHashing
{

  public static String hash(char[] cData)
  {
    String sData = "";
    for (char c : cData)
    {
      sData += c;
    }
    byte[] yData = sData.getBytes();
    MessageDigest md;
    byte[] digest;
    try
    {
      md = MessageDigest.getInstance("SHA-256");
      md.update(yData);
      digest = md.digest();
      return ByteToString( digest );
    }
    catch (Exception ex)
    {
      Logger.getLogger(cHashing.class.getName()).log(Level.SEVERE, null, ex);
    }
    return null;
  }

  public static String ByteToString(byte[] bytes)
  {
    return Hex.encodeHexString( bytes );
  }
  
  public static byte[] StringToByte(String string)
  {
    byte[] decodeHex = null;
    try
    {
      char[] oData = string.toCharArray();
      decodeHex = Hex.decodeHex(oData);
    }
    catch (DecoderException ex)
    {
      Logger.getLogger(cHashing.class.getName()).log(Level.SEVERE, null, ex);
    }
    return decodeHex;
  }
  
  public static void main(String[] args)
  {
    String s = "1234";
    byte[] StringToByte = StringToByte(s);
    String s2 = ByteToString(StringToByte);
    System.out.println(s + ":" + s2);
  }
}
