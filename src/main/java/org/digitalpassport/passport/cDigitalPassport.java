package org.digitalpassport.passport;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.RandomAccessFile;
import java.nio.channels.FileChannel;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.digitalpassport.jdbc.cDatabaseHandler;

/**
 *
 * @author Philip M. Trenwith
 */
public class cDigitalPassport
{

  private cDatabaseHandler m_oDatabase = null;
  private Process m_oProcess = null;
  private boolean m_bTerminate = false;

  private static HashMap<Integer, cDigitalPassport> g_oOpenPassports = new HashMap();

  private String m_sPassportName = "";
  private String m_sPassportPath = "";

  private int m_oPASSPORT_ID = -1;
  private cProvenanceData m_oPASSPORT_Provenance = null;
  private File m_oPASSPORT_PAYLOAD = null;

  public int getID()
  {
    return m_oPASSPORT_ID;
  }

  public cProvenanceData getProvenance()
  {
    return m_oPASSPORT_Provenance;
  }

  public File getPayload()
  {
    return m_oPASSPORT_PAYLOAD;
  }

  public cDigitalPassport(File oFile, String sUsername)
  {
    m_oDatabase = cDatabaseHandler.instance();
    if (oFile.getName().endsWith("passport"))
    {
      try
      {
        // open the passport
        m_sPassportPath = oFile.getParent() + File.separator;
        m_sPassportName = oFile.getName();

        String sPath = oFile.getParent() + File.separator + oFile.getName().replace(".", "") + "_open";
        File oDir = new File(sPath);
        oDir.mkdirs();
        oDir.deleteOnExit();

        cExtractZip oExtract = new cExtractZip();
        oExtract.unzip(oFile.getAbsolutePath(), sPath);

        String sProvenance = sPath + File.separator + oFile.getName().replaceFirst(".passport", "_Provenance.xml");
        String sPayload = sPath + File.separator + oFile.getName().replaceFirst(".passport", "");

        String sID = m_oDatabase.getPassportID(sPayload);

        m_oPASSPORT_ID = Integer.parseInt(sID);
        m_oPASSPORT_PAYLOAD = new File(sPayload);
        m_oPASSPORT_Provenance = new cProvenanceData(m_oPASSPORT_PAYLOAD, sProvenance);

//        m_oPASSPORT_Provenance.generateLocationRecord(Main.bSERVER);
//        m_oPassportLocation = cIPLookup.lookupGeoLocationFromIP(Main.Database, 
//                cIPLookup.IPv4AddressToSingleNumber(cIPLookup.getIPAddressAssignedByISP()));
      }
      catch (IOException ex)
      {
        Logger.getLogger(cDigitalPassport.class.getName()).log(Level.SEVERE, null, ex);
      }
    }
    else
    {
      // create a new passport
      m_oDatabase.uploadPassport(oFile.getName(), sUsername);
      m_oPASSPORT_ID = Integer.parseInt(m_oDatabase.getPassportID(oFile.getName()));
      m_oPASSPORT_PAYLOAD = oFile;
      m_oPASSPORT_Provenance = new cProvenanceData(m_oPASSPORT_PAYLOAD);

//      m_oPASSPORT_Provenance.generateLocationRecord(Main.bSERVER);
//      m_oPassportLocation = cIPLookup.lookupGeoLocationFromIP(Main.Database, 
//                cIPLookup.IPv4AddressToSingleNumber(cIPLookup.getIPAddressAssignedByISP()));
    }
  }

  public static cDigitalPassport createPassport(File oPayload, String sUsername, boolean bDeletePayloadFile)
  {
    cDigitalPassport oPassport = null;
    try
    {
      if (oPayload != null)
      {
        Logger.getLogger(cDigitalPassport.class.getName()).log(Level.INFO,
            "Creating passport for payload: {0}", oPayload.getName());
        oPassport = new cDigitalPassport(oPayload, sUsername);
        if (!oPassport.compilePassport(bDeletePayloadFile))
        {
          oPassport = null;
        }
      }
    }
    catch (Exception ex)
    {
      Logger.getLogger(cDigitalPassport.class.getName()).log(Level.SEVERE,
          "Exception creating passport: {0}", ex.getMessage());
      ex.printStackTrace();
    }
    return oPassport;
  }

  public static cDigitalPassport openPassport(File oDigitalPassport, String sUsername, boolean bOpenPayload)
  {
    cDigitalPassport oPassport = null;
    try
    {
      Logger.getLogger(cDigitalPassport.class.getName()).log(Level.INFO,
          "Opening passport: {0}", oDigitalPassport.getName());
      oPassport = new cDigitalPassport(oDigitalPassport, sUsername);

      g_oOpenPassports.put(oPassport.getID(), oPassport);

      if (bOpenPayload)
      {
        oPassport.openPayload(oPassport.m_oPASSPORT_PAYLOAD.getAbsolutePath());
      }
    }
    catch (Exception ex)
    {
      Logger.getLogger(cDigitalPassport.class.getName()).log(Level.SEVERE,
          "Exception opening passport: {0}", ex.getMessage());
      ex.printStackTrace();
    }
    finally
    {
      if (oPassport != null)
      {
        if (!oPassport.compilePassport(true))
        {
          oPassport = null;
        }
      }
    }
    return oPassport;
  }

  private boolean compilePassport(boolean bDeletePayload)
  {
    boolean bSuccess = false;
    try
    {
      Logger.getLogger(cDigitalPassport.class.getName()).log(Level.INFO, "Compiling passport");
      m_sPassportPath = m_oPASSPORT_PAYLOAD.getParentFile().getAbsolutePath() + File.separator;
      m_sPassportName = m_oPASSPORT_PAYLOAD.getName() + ".passport";

      boolean commit = true;// Main.Database.updatePassport(this);

      if (commit)
      {
        cCreateZip zipFile = new cCreateZip(m_sPassportPath + File.separator + m_oPASSPORT_PAYLOAD.getName() + ".passport");

        File oIdFile = new File(new File(m_oPASSPORT_Provenance.m_sPRFile).getParent() + File.separator + m_oPASSPORT_ID + "");
        try
        {
          oIdFile.createNewFile();
        }
        catch (IOException ex)
        {
          Logger.getLogger(cDigitalPassport.class.getName()).log(Level.SEVERE, null, ex);
        }

        zipFile.addFile(m_oPASSPORT_ID + "", oIdFile.getAbsolutePath());
        zipFile.addFile(new File(m_oPASSPORT_Provenance.m_sPRFile).getName(), m_oPASSPORT_Provenance.m_sPRFile);
        zipFile.addFile(m_oPASSPORT_PAYLOAD.getName(), m_oPASSPORT_PAYLOAD.getAbsolutePath());
        zipFile.close();

        if (bDeletePayload)
        {
          m_oPASSPORT_PAYLOAD.delete();
        }
        new File(m_oPASSPORT_Provenance.m_sPRFile).delete();
        oIdFile.delete();
        m_oPASSPORT_PAYLOAD.getParentFile().delete();
        Logger.getLogger(cDigitalPassport.class.getName()).log(Level.INFO, "Passport Compiled");
        bSuccess = true;
      }
      else
      {
        new File(m_oPASSPORT_Provenance.m_sPRFile).delete();
        Logger.getLogger(cDigitalPassport.class.getName()).log(Level.INFO, "**** ERROR: Passport Compilation Failed!");
      }
    }
    catch (Exception ex)
    {
      Logger.getLogger(cDigitalPassport.class.getName()).log(Level.SEVERE,
          "Exception creating passport: {0}", ex.getMessage());
      ex.printStackTrace();
    }
    return bSuccess;
  }

  private void openPayload(String sPayload)
  {
    try
    {
      String sFILEPATH = sPayload;
      m_oProcess = Runtime.getRuntime().exec("rundll32 url.dll,FileProtocolHandler " + sFILEPATH);
      Logger.getLogger(cDigitalPassport.class.getName()).log(Level.INFO, "Waiting for process to complete...");
      Thread.sleep(1000);

      while (!m_bTerminate)
      {
        try
        {
          FileChannel channel = new RandomAccessFile(new File(sFILEPATH), "rw").getChannel();
          //FileLock lock = channel.lock();
          channel.close();
          break;
        }
        catch (Exception ex)
        {
          if (!ex.getMessage().contains("The process cannot access the file because it is being used by another process"))
          {
            Logger.getLogger(cDigitalPassport.class.getName()).log(Level.INFO, "Exception waiting: {0}", ex.getMessage());
            ex.printStackTrace();
            break;
          }
          Thread.sleep(1000);
        }
      }

      BufferedReader output = new BufferedReader(new InputStreamReader(m_oProcess.getInputStream()));
      BufferedReader error = new BufferedReader(new InputStreamReader(m_oProcess.getErrorStream()));
      String line = "";

      while ((line = output.readLine()) != null)
      {
        Logger.getLogger(cDigitalPassport.class.getName()).log(Level.INFO, line);
      }

      while ((line = error.readLine()) != null)
      {
        Logger.getLogger(cDigitalPassport.class.getName()).log(Level.INFO, line);
      }

      Logger.getLogger(cDigitalPassport.class.getName()).log(Level.INFO, "Process done!");

    }
    catch (Exception ex)
    {
      Logger.getLogger(cDigitalPassport.class.getName()).log(Level.INFO, "Exception: {0}", ex.getMessage());
      ex.printStackTrace();
    }
    finally
    {
      m_oPASSPORT_Provenance.generateModificationRecord();
    }
  }

  public static void terminateAll()
  {
    for (cDigitalPassport oPassport : g_oOpenPassports.values())
    {
      oPassport.terminate();
    }
  }

  public void terminate()
  {
    Logger.getLogger(cDigitalPassport.class.getName()).log(Level.INFO, "Closing Passport: {0}", m_oPASSPORT_PAYLOAD.getName());
    m_bTerminate = true;
    m_oProcess.destroy();
    try
    {
      m_oProcess.waitFor();
    }
    catch (InterruptedException ex)
    {
      Logger.getLogger(cDigitalPassport.class.getName()).log(Level.SEVERE, null, ex);
    }
  }

  public String getPassportName()
  {
    return m_sPassportName;
  }

  public String getPassportPath()
  {
    return m_sPassportPath;
  }
}
