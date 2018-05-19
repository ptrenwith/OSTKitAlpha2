package org.digitalpassport.jdbc;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.digitalpassport.passport.cDigitalPassport;

/**
 *
 * @author PhilipT
 */
public class cDatabaseHandler 
{
  private Connection m_oConnection = null;
 
  public static void main(String[] args)
  {
    cDatabaseHandler oDatabaseHandler = new cDatabaseHandler();
    oDatabaseHandler.init();
  }
  
  public cDatabaseHandler()
  {
    init();
  }
  
  private void init()
  {
    try
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
    catch (Exception ex)
    {
      Logger.getLogger(cDatabaseHandler.class.getName()).log(Level.SEVERE, null, ex);
    }
  }

  public boolean register(String sUsername, String sPasswordHash, String sDisplayName)
  {
    boolean bResult = false;
    PreparedStatement oStatement = null;
    try
    {
      oStatement = m_oConnection.prepareStatement("INSERT INTO dpt_users (Username, Password, DisplayName) VALUES ('" + 
          sUsername + "', '" + sPasswordHash + "', '" + sDisplayName + "');");
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
    throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
  }

  public int uploadPassport(String name, String string)
  {
    throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
  }
}
