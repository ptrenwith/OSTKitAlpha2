package org.digitalpassport.jdbc;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.table.DefaultTableModel;
import org.digitalpassport.api.commands.cTransactionManagement;
import org.digitalpassport.deserialize.json.cResponse;
import org.digitalpassport.deserialize.json.transactiontypes.cTransactionTypes;
import org.digitalpassport.passport.cDatabaseFile;
import org.digitalpassport.passport.cDigitalPassport;

/**
 *
 * @author PhilipT
 */
public class cDatabaseHandler
{
  private cTransactionManagement m_oTransactionManagement = new cTransactionManagement();
  private Connection m_oConnection = null;
  private static cDatabaseHandler g_oInstance = null;

  public static void main(String[] args)
  {
    cDatabaseHandler oDatabaseHandler = new cDatabaseHandler();
    oDatabaseHandler.init();
  }

  public static cDatabaseHandler instance()
  {
    if (g_oInstance == null)
    {
      synchronized (cDatabaseHandler.class)
      {
        g_oInstance = new cDatabaseHandler();
      }
    }
    g_oInstance.init();
    return g_oInstance;
  }

  private cDatabaseHandler()
  {
    init();
  }

  private void init()
  {
    try
    {
      if (m_oConnection == null || m_oConnection.isClosed())
      {
        System.out.println("Initializing Database Handler...");
        String url = "jdbc:mysql://46.101.80.197:3306/digitalpassport";//?rewriteBatchedStatements=true";
        String user = "philip";
        String password = "umooxiuf";
        Class.forName("com.mysql.jdbc.Driver").newInstance();
        m_oConnection = DriverManager.getConnection(url, user, password);
        m_oConnection.setAutoCommit(false);
        System.out.println("Database Handler initialized");
      }
    }
    catch (Exception ex)
    {
      Logger.getLogger(cDatabaseHandler.class.getName()).log(Level.SEVERE, null, ex);
    }
  }

  public String login(String sUsername, String sPasswordHash)
  {
    String sDisplayName = "";
    PreparedStatement oStatement = null;
    try
    {
      oStatement = m_oConnection.prepareStatement("SELECT * FROM dpt_users WHERE Username='" + sUsername + "' AND "
          + " Password='" + sPasswordHash + "';");
      ResultSet oResult = oStatement.executeQuery();
      if (oResult.next())
      {
        sDisplayName = oResult.getString("DisplayName");
      }
    }
    catch (SQLException ex)
    {
      Logger.getLogger(cDatabaseHandler.class.getName()).log(Level.SEVERE, null, ex);
    }
    finally
    {
      if (oStatement != null)
      {
        try
        {
          m_oConnection.commit();
          oStatement.close();
        }
        catch (SQLException ex)
        {
          Logger.getLogger(cDatabaseHandler.class.getName()).log(Level.SEVERE, null, ex);
        }
      }
    }
    return sDisplayName;
  }

  public boolean register(String sUsername, String sPasswordHash, String sDisplayName)
  {
    boolean bResult = false;
    PreparedStatement oStatement = null;
    try
    {
      oStatement = m_oConnection.prepareStatement("INSERT INTO dpt_users (Username, Password, DisplayName) VALUES ('"
          + sUsername + "', '" + sPasswordHash + "', '" + sDisplayName + "');");
      oStatement.execute();
      bResult = true;
    }
    catch (SQLException ex)
    {
      Logger.getLogger(cDatabaseHandler.class.getName()).log(Level.SEVERE, null, ex);
    }
    finally
    {
      if (oStatement != null)
      {
        try
        {
          m_oConnection.commit();
          oStatement.close();
        }
        catch (SQLException ex)
        {
          Logger.getLogger(cDatabaseHandler.class.getName()).log(Level.SEVERE, null, ex);
        }
      }
    }
    return bResult;
  }

  public boolean saveTransaction(String sTransactionUuid, String sFileId)
  {
    boolean bResult = false;
    PreparedStatement oStatement = null;
    try
    {
      oStatement = m_oConnection.prepareStatement("INSERT INTO transactions (transaction_uuid, FileId) values ('"
          + sTransactionUuid + "', '" + sFileId + "');");
      oStatement.execute();
      bResult = true;
    }
    catch (SQLException ex)
    {
      Logger.getLogger(cDatabaseHandler.class.getName()).log(Level.SEVERE, null, ex);
    }
    finally
    {
      if (oStatement != null)
      {
        try
        {
          m_oConnection.commit();
          oStatement.close();
        }
        catch (SQLException ex)
        {
          Logger.getLogger(cDatabaseHandler.class.getName()).log(Level.SEVERE, null, ex);
        }
      }
    }
    return bResult;
  }

  private ResultSet executeQuery(Statement oStatement, String sQuery)
  {
    ResultSet oResultSet = null;
    try
    {
      oResultSet = oStatement.executeQuery(sQuery);
    }
    catch (SQLException ex)
    {
      Logger.getLogger(cDatabaseHandler.class.getName()).log(Level.SEVERE, null, ex);
    }
    return oResultSet;
  }

  private boolean update(Statement oStatement, String sQuery)
  {
    boolean execute = false;
    try
    {
      oStatement.execute(sQuery);
      execute = true;
    }
    catch (SQLException ex)
    {
      Logger.getLogger(cDatabaseHandler.class.getName()).log(Level.SEVERE, null, ex);
    }
    return execute;
  }

  public ResultSet select(String sQuery)
  {
    ResultSet oResultSet = null;
    Statement oStatement = null;
    try
    {
      oStatement = m_oConnection.createStatement();
      oResultSet = oStatement.executeQuery(sQuery);
    }
    catch (SQLException ex)
    {
      Logger.getLogger(cDatabaseHandler.class.getName()).log(Level.SEVERE, null, ex);
    }
    return oResultSet;
  }

  public void terminate()
  {
    Logger.getLogger(cDatabaseHandler.class.getName()).log(Level.INFO, "Terminating Database Handler");
    if (m_oConnection != null)
    {
      try
      {
        m_oConnection.close();
      }
      catch (SQLException ex)
      {
        Logger.getLogger(cDatabaseHandler.class.getName()).log(Level.SEVERE, null, ex);
      }
    }
  }

  public boolean updatePassport(cDigitalPassport oPassport)
  {
    boolean bReturn = false;
    ResultSet result = null;
    Statement oStatement = null;
    try
    {
//      oStatement = m_oConnection.createStatement();
//      Logger.getLogger(cDatabaseHandler.class.getName()).log(Level.INFO,"Updating passport location to database");
//      String sID = oPassport.getID().toString();
//      result = executeQuery(oStatement, "SELECT * FROM passport WHERE ID = '" + sID + "'");
//      int iRow = 0;
//      while (result.next())
//      {
//        iRow++;
//        break;
//      }
//      if (iRow == 0)
//      {
//        bReturn = update(oStatement,"INSERT INTO passport VALUES ('" + 
//              oPassport.getID() + "', '" + 
//              oPassport.getPassportPath().replace("\\", "\\\\") + "', '" + 
//              oPassport.getPassportName() + "', '" + 
//              new GregorianCalendar().getTimeInMillis() + "', '" +
//              oPassport.getPassportLocation().sIPAddress + "', '" + 
//              oPassport.getPassportLocation().sCountry + "', '" + 
//              oPassport.getPassportLocation().sCity +  
//              "')");
//      }
//      else
//      {
//        bReturn = update(oStatement,"UPDATE passport SET " + 
//              "path='" + 
//              oPassport.getPassportPath().replace("\\", "\\\\") + "', name='" + 
//              oPassport.getPassportName() + "', timestamp='" + 
//              new GregorianCalendar().getTimeInMillis() + "', ip='" +
//              oPassport.getPassportLocation().sIPAddress + "', country='" + 
//              oPassport.getPassportLocation().sCountry + "', city='" + 
//              oPassport.getPassportLocation().sCity +  
//              "' where id='" + oPassport.getID() + "'");
//      }
//      m_oConnection.commit();
    }
    catch (Exception ex)
    {
      Logger.getLogger(cDatabaseHandler.class.getName()).log(Level.SEVERE,
          "Exception updating passport to database: " + ex.getMessage(), ex);
      ex.printStackTrace();
    }
    finally
    {
      if (result != null)
      {
        try
        {
          result.close();
        }
        catch (SQLException ex)
        {
          Logger.getLogger(cDatabaseHandler.class.getName()).log(Level.SEVERE, null, ex);
        }
      }

      if (oStatement != null)
      {
        try
        {
          oStatement.close();
        }
        catch (SQLException ex)
        {
          Logger.getLogger(cDatabaseHandler.class.getName()).log(Level.SEVERE, null, ex);
        }
      }
    }
    return bReturn;
  }

  public String getPassportID(String sPayload)
  {
    String sResult = "";
    PreparedStatement oStatement = null;
    try
    {
      oStatement = m_oConnection.prepareStatement("SELECT * FROM passports WHERE Filename='" + sPayload + "';");
      ResultSet executeQuery = oStatement.executeQuery();
      if (executeQuery.next())
      {
        sResult = executeQuery.getString("ID");
      }
    }
    catch (SQLException ex)
    {
      Logger.getLogger(cDatabaseHandler.class.getName()).log(Level.SEVERE, null, ex);
    }
    finally
    {
      if (oStatement != null)
      {
        try
        {
          m_oConnection.commit();
          oStatement.close();
        }
        catch (SQLException ex)
        {
          Logger.getLogger(cDatabaseHandler.class.getName()).log(Level.SEVERE, null, ex);
        }
      }
    }
    return sResult;
  }

  public boolean uploadPassport(String sFilename, String sOwner, String sDisplayName)
  {
    boolean bResult = false;
    PreparedStatement oStatement = null;
    try
    {
      
      
      String sFromUUID = cDatabaseHandler.instance().getUuid(sDisplayName);
      cTransactionTypes oTransaction = cTransactionManagement.m_oTransactions.get("upload");
      cResponse oResponse = m_oTransactionManagement.executeTransaction_sandbox(sFromUUID, "1ec0b428-1b27-4218-95ae-b116f14b0450", oTransaction);
      
      oStatement = m_oConnection.prepareStatement("INSERT INTO passports (Filename, Owner) VALUES ('"
          + sFilename + "', '" + sOwner + "');");
      oStatement.execute();
      
      bResult = true;
    }
    catch (SQLException ex)
    {
      Logger.getLogger(cDatabaseHandler.class.getName()).log(Level.SEVERE, null, ex);
    }
    finally
    {
      if (oStatement != null)
      {
        try
        {
          m_oConnection.commit();
          oStatement.close();
        }
        catch (SQLException ex)
        {
          Logger.getLogger(cDatabaseHandler.class.getName()).log(Level.SEVERE, null, ex);
        }
      }
    }
    return bResult;
  }

  public void setUUID(String sUsername, String sUuid)
  {
    PreparedStatement oStatement = null;
    try
    {
      oStatement = m_oConnection.prepareStatement("UPDATE dpt_users SET DptUUID='" + sUuid + "' WHERE DisplayName='" + sUsername + "';");
      oStatement.execute();
    }
    catch (SQLException ex)
    {
      Logger.getLogger(cDatabaseHandler.class.getName()).log(Level.SEVERE, null, ex);
    }
    finally
    {
      if (oStatement != null)
      {
        try
        {
          m_oConnection.commit();
          oStatement.close();
        }
        catch (SQLException ex)
        {
          Logger.getLogger(cDatabaseHandler.class.getName()).log(Level.SEVERE, null, ex);
        }
      }
    }
  }

  public String getUuid(String sDisplayName)
  {
    String sResult = "";
    PreparedStatement oStatement = null;
    try
    {
      oStatement = m_oConnection.prepareStatement("SELECT * FROM dpt_users WHERE DisplayName='" + sDisplayName + "';");
      ResultSet executeQuery = oStatement.executeQuery();
      if (executeQuery.next())
      {
        sResult = executeQuery.getString("DptUUID");
      }
    }
    catch (SQLException ex)
    {
      Logger.getLogger(cDatabaseHandler.class.getName()).log(Level.SEVERE, null, ex);
    }
    finally
    {
      if (oStatement != null)
      {
        try
        {
          m_oConnection.commit();
          oStatement.close();
        }
        catch (SQLException ex)
        {
          Logger.getLogger(cDatabaseHandler.class.getName()).log(Level.SEVERE, null, ex);
        }
      }
    }
    return sResult;
  }
  
  public String getUsername(String sDisplayName)
  {
    String sResult = "";
    PreparedStatement oStatement = null;
    try
    {
      oStatement = m_oConnection.prepareStatement("SELECT * FROM dpt_users WHERE DisplayName='" + sDisplayName + "';");
      ResultSet executeQuery = oStatement.executeQuery();
      if (executeQuery.next())
      {
        sResult = executeQuery.getString("Username");
      }
    }
    catch (SQLException ex)
    {
      Logger.getLogger(cDatabaseHandler.class.getName()).log(Level.SEVERE, null, ex);
    }
    finally
    {
      if (oStatement != null)
      {
        try
        {
          m_oConnection.commit();
          oStatement.close();
        }
        catch (SQLException ex)
        {
          Logger.getLogger(cDatabaseHandler.class.getName()).log(Level.SEVERE, null, ex);
        }
      }
    }
    return sResult;
  }

  public ArrayList<String> getTransactions()
  {
    ArrayList<String> lsFiles = new ArrayList();

    PreparedStatement oStatement = null;
    try
    {
      oStatement = m_oConnection.prepareStatement("SELECT * FROM transactions ;");
      ResultSet oResultSet = oStatement.executeQuery();
      while (oResultSet.next())
      {
        lsFiles.add(oResultSet.getString("transaction_uuid"));
      }
    }
    catch (SQLException ex)
    {
      Logger.getLogger(cDatabaseHandler.class.getName()).log(Level.SEVERE, null, ex);
    }
    finally
    {
      if (oStatement != null)
      {
        try
        {
          m_oConnection.commit();
          oStatement.close();
        }
        catch (SQLException ex)
        {
          Logger.getLogger(cDatabaseHandler.class.getName()).log(Level.SEVERE, null, ex);
        }
      }
    }

    return lsFiles;
  }
  
  public ArrayList<String> getTransactions(int iID)
  {
    ArrayList<String> lsFiles = new ArrayList();

    PreparedStatement oStatement = null;
    try
    {
      oStatement = m_oConnection.prepareStatement("SELECT * FROM transactions where FileId='" + iID + "';");
      ResultSet oResultSet = oStatement.executeQuery();
      while (oResultSet.next())
      {
        lsFiles.add(oResultSet.getString("transaction_uuid"));
      }
    }
    catch (SQLException ex)
    {
      Logger.getLogger(cDatabaseHandler.class.getName()).log(Level.SEVERE, null, ex);
    }
    finally
    {
      if (oStatement != null)
      {
        try
        {
          m_oConnection.commit();
          oStatement.close();
        }
        catch (SQLException ex)
        {
          Logger.getLogger(cDatabaseHandler.class.getName()).log(Level.SEVERE, null, ex);
        }
      }
    }

    return lsFiles;
  }

  public ArrayList<cDatabaseFile> getYourFiles(String sUsername, DefaultTableModel oYourModel)
  {
    ArrayList<cDatabaseFile> lsFiles = new ArrayList();

    PreparedStatement oStatement = null;
    try
    {
      oStatement = m_oConnection.prepareStatement("SELECT passports.ID, passports.Filename, passports.Owner, dpt_users.DisplayName "
              + "FROM passports inner join dpt_users on passports.owner=dpt_users.Username WHERE Owner='" + sUsername + "';");
      ResultSet oResultSet = oStatement.executeQuery();
      while (oResultSet.next())
      {
        cDatabaseFile oDatabase = new cDatabaseFile();
        oDatabase.m_sFileID = oResultSet.getString("ID");
        oDatabase.m_sFilename = oResultSet.getString("Filename");
        oDatabase.m_sOwner = oResultSet.getString("Owner");
        oDatabase.m_sDisplayName = oResultSet.getString("DisplayName");
        lsFiles.add(oDatabase);
        
        oYourModel.addRow(new Object[]
        {
          oDatabase.m_sFileID, oDatabase.m_sFilename, oDatabase.m_sDisplayName
        });
      }
    }
    catch (SQLException ex)
    {
      Logger.getLogger(cDatabaseHandler.class.getName()).log(Level.SEVERE, null, ex);
    }
    finally
    {
      if (oStatement != null)
      {
        try
        {
          m_oConnection.commit();
          oStatement.close();
        }
        catch (SQLException ex)
        {
          Logger.getLogger(cDatabaseHandler.class.getName()).log(Level.SEVERE, null, ex);
        }
      }
    }

    return lsFiles;
  }
  
  public String getFileIdFromName(String sFilename)
  {
    String sReturn = "";

    PreparedStatement oStatement = null;
    try
    {
      oStatement = m_oConnection.prepareStatement("SELECT passports.ID FROM passports WHERE Filename='" + sFilename + "';");
      ResultSet oResultSet = oStatement.executeQuery();
      while (oResultSet.next())
      {
        sReturn = oResultSet.getString("ID");
      }
    }
    catch (SQLException ex)
    {
      Logger.getLogger(cDatabaseHandler.class.getName()).log(Level.SEVERE, null, ex);
    }
    finally
    {
      if (oStatement != null)
      {
        try
        {
          m_oConnection.commit();
          oStatement.close();
        }
        catch (SQLException ex)
        {
          Logger.getLogger(cDatabaseHandler.class.getName()).log(Level.SEVERE, null, ex);
        }
      }
    }

    return sReturn;
  }
  
  public int countTransactions()
  {
    int iReturn = 0;
    PreparedStatement oStatement = null;
    try
    {
      oStatement = m_oConnection.prepareStatement("SELECT count(*) as c from transactions");
      ResultSet oResultSet = oStatement.executeQuery();
      oResultSet.next();

      iReturn = Integer.parseInt(oResultSet.getString("c"));
    }
    catch (SQLException ex)
    {
      Logger.getLogger(cDatabaseHandler.class.getName()).log(Level.SEVERE, null, ex);
    }
    finally
    {
      if (oStatement != null)
      {
        try
        {
          m_oConnection.commit();
          oStatement.close();
        }
        catch (SQLException ex)
        {
          Logger.getLogger(cDatabaseHandler.class.getName()).log(Level.SEVERE, null, ex);
        }
      }
    }
    
    return iReturn;
  }

  public ArrayList<cDatabaseFile> getOtherFiles(String sUsername)
  {
    ArrayList<cDatabaseFile> lsFiles = new ArrayList();

    PreparedStatement oStatement = null;
    try
    {
      oStatement = m_oConnection.prepareStatement("SELECT passports.ID, passports.Filename, passports.Owner, dpt_users.DisplayName "
              + "FROM passports inner join dpt_users on passports.owner=dpt_users.Username WHERE Owner!='" + sUsername + "';");
      ResultSet oResultSet = oStatement.executeQuery();
      while (oResultSet.next())
      {
        cDatabaseFile oDatabase = new cDatabaseFile();
        oDatabase.m_sFileID = oResultSet.getString("ID");
        oDatabase.m_sFilename = oResultSet.getString("Filename");
        oDatabase.m_sOwner = oResultSet.getString("Owner");
        oDatabase.m_sDisplayName = oResultSet.getString("DisplayName");
        lsFiles.add(oDatabase);
      }
    }
    catch (SQLException ex)
    {
      Logger.getLogger(cDatabaseHandler.class.getName()).log(Level.SEVERE, null, ex);
    }
    finally
    {
      if (oStatement != null)
      {
        try
        {
          m_oConnection.commit();
          oStatement.close();
        }
        catch (SQLException ex)
        {
          Logger.getLogger(cDatabaseHandler.class.getName()).log(Level.SEVERE, null, ex);
        }
      }
    }

    return lsFiles;
  }

  public boolean addUserIfNotExists(String sName, String sUuid)
  {
    boolean bResult = false;
    PreparedStatement oStatement = null;
    try
    {
      String sUsername = sName.toLowerCase().replaceAll(" ", "");
      oStatement = m_oConnection.prepareStatement("INSERT INTO dpt_users (Username, Password, DisplayName, DptUUID) VALUES ('"
          + sUsername + "', '03ac674216f3e15c761ee1a5e255f067953623c8b388b4459e13f978d7c846f4', '" + sName + "', '" + sUuid + "');");
      oStatement.execute();
      bResult = true;
    }
    catch (SQLException ex)
    {
    }
    finally
    {
      if (oStatement != null)
      {
        try
        {
          if (!m_oConnection.isClosed())
          {
            m_oConnection.commit();
          }
          oStatement.close();
        }
        catch (SQLException ex)
        {
          Logger.getLogger(cDatabaseHandler.class.getName()).log(Level.SEVERE, null, ex);
        }
      }
    }
    return bResult;
  }

  public boolean addShareFileIfNotExists(String sFileId, String sDisplayName)
  {
    boolean bResult = false;
    PreparedStatement oStatement = null;
    try
    {
      oStatement = m_oConnection.prepareStatement("SELECT * FROM shared WHERE FileId='" + sFileId + "' AND DisplayName='" + sDisplayName + "';");
      ResultSet executeQuery = oStatement.executeQuery();
      if (!executeQuery.next())
      {
        oStatement = m_oConnection.prepareStatement("INSERT INTO shared (FileId, DisplayName) VALUES ('" + sFileId + "', '" + sDisplayName + "');");
        oStatement.execute();
        bResult = true;
      }
    }
    catch (SQLException ex)
    {
      Logger.getLogger(cDatabaseHandler.class.getName()).log(Level.SEVERE, null, ex);
    }
    finally
    {
      if (oStatement != null)
      {
        try
        {
          m_oConnection.commit();
          oStatement.close();
        }
        catch (SQLException ex)
        {
          Logger.getLogger(cDatabaseHandler.class.getName()).log(Level.SEVERE, null, ex);
        }
      }
    }
    return bResult;
  }

  public ArrayList<cDatabaseFile> getSharedFiles(String sDisplayName, DefaultTableModel oYourModel)
  {
    ArrayList<cDatabaseFile> lsFiles = new ArrayList();

    PreparedStatement oStatement = null;
    try
    {
      oStatement = m_oConnection.prepareStatement("select shared.FileId, passports.Filename, passports.Owner, dpt_users.DisplayName "
              + "from shared inner join passports on passports.ID=shared.FileId \n"
              + "inner join dpt_users on dpt_users.Username=passports.Owner where shared.DisplayName='" + sDisplayName + "';");
      ResultSet oResultSet = oStatement.executeQuery();
      while (oResultSet.next())
      {
        cDatabaseFile oDatabase = new cDatabaseFile();
        oDatabase.m_sFileID = oResultSet.getString("FileId");
        oDatabase.m_sFilename = oResultSet.getString("Filename");
        oDatabase.m_sOwner = oDatabase.m_sDisplayName = oResultSet.getString("DisplayName") + " (shared)";
        
        lsFiles.add(oDatabase);
        oYourModel.addRow(new Object[]
        {
          oDatabase.m_sFileID, oDatabase.m_sFilename, oDatabase.m_sDisplayName
        });
      }
    }
    catch (SQLException ex)
    {
      Logger.getLogger(cDatabaseHandler.class.getName()).log(Level.SEVERE, null, ex);
    }
    finally
    {
      if (oStatement != null)
      {
        try
        {
          m_oConnection.commit();
          oStatement.close();
        }
        catch (SQLException ex)
        {
          Logger.getLogger(cDatabaseHandler.class.getName()).log(Level.SEVERE, null, ex);
        }
      }
    }

    return lsFiles;
  }

  public void updateTransaction(String sTransaction, String sID)
  {
    PreparedStatement oStatement = null;
    try
    {
      oStatement = m_oConnection.prepareStatement("update transactions set FileId='" + sID + "' where transaction_uuid='" + sTransaction + "';");
      oStatement.execute();
    }
    catch (SQLException ex)
    {
      Logger.getLogger(cDatabaseHandler.class.getName()).log(Level.SEVERE, null, ex);
    }
    finally
    {
      if (oStatement != null)
      {
        try
        {
          m_oConnection.commit();
          oStatement.close();
        }
        catch (SQLException ex)
        {
          Logger.getLogger(cDatabaseHandler.class.getName()).log(Level.SEVERE, null, ex);
        }
      }
    }
  }
}
