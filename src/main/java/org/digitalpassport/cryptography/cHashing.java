
package org.digitalpassport.cryptography;

import java.security.MessageDigest;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Philip M. Trenwith
 */
public class cHashing
{
  public static String hash(char[] cData)
  {
    String sData = "";
    for (char c: cData)
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
      return new String(digest, "UTF-8");
    }
    catch (Exception ex)
    {
      Logger.getLogger(cHashing.class.getName()).log(Level.SEVERE, null, ex);
    }
    return null;
  }
}
