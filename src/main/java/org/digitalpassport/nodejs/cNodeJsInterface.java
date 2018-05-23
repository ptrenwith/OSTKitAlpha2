package org.digitalpassport.nodejs;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Philip M. Trenwith
 */
public class cNodeJsInterface
{

  public static String hash(String sParameterString)
  {
    String sResponse = "";
    InputStream oInputStream = null;
    try
    {

      File oFile = new File("nodejs\\hash.js");
      ProcessBuilder pb = new ProcessBuilder("nodejs\\node.exe", oFile.getAbsolutePath(), sParameterString);

      pb = pb.redirectInput(ProcessBuilder.Redirect.PIPE);
      pb = pb.redirectErrorStream(true);
      pb = pb.redirectOutput(ProcessBuilder.Redirect.PIPE);

      Process oProcess = pb.start();
      BufferedReader in = new BufferedReader(new InputStreamReader(oProcess.getInputStream()));
      String line;
      while ((line = in.readLine()) != null)
      {
        sResponse += line;
      }
    }
    catch (FileNotFoundException ex)
    {
      Logger.getLogger(cNodeJsInterface.class.getName()).log(Level.SEVERE, null, ex);
    }
    catch (IOException ex)
    {
      Logger.getLogger(cNodeJsInterface.class.getName()).log(Level.SEVERE, null, ex);
    }
    finally
    {
      try
      {
        if (oInputStream != null)
        {
          oInputStream.close();
        }
      }
      catch (IOException ex)
      {
        Logger.getLogger(cNodeJsInterface.class.getName()).log(Level.SEVERE, null, ex);
      }
    }
    return sResponse;
  }
}
