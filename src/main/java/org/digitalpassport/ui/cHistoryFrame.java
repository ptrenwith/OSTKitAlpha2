package org.digitalpassport.ui;

import java.awt.Color;
import java.awt.Component;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Vector;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.table.DefaultTableModel;
import org.digitalpassport.api.commands.cTransactionManagement;
import org.digitalpassport.deserialize.json.cError;
import org.digitalpassport.deserialize.json.cErrorData;
import org.digitalpassport.deserialize.json.cResponse;
import org.digitalpassport.deserialize.json.transactiontypes.cTransaction;
import org.digitalpassport.deserialize.json.transactiontypes.cTransactionTypes;
import org.digitalpassport.deserialize.json.users.lists.cEconomyUser;
import org.digitalpassport.jdbc.cDatabaseHandler;
import static org.digitalpassport.ui.panels.cTokenManagementPanel.showError;

/**
 *
 * @author Philip M. Trenwith
 */
public class cHistoryFrame extends javax.swing.JFrame
{

  private SimpleDateFormat m_oSimpleDateFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss.SSSZ");
  private cDatabaseHandler m_oDatabase = null;
  private cTransactionManagement m_oTransactionManagement = null;

  /**
   * Creates new form cHistoryFrame
   */
  public cHistoryFrame()
  {
    initComponents();
    lblStatus.setText("Initializing...");
    m_oDatabase = cDatabaseHandler.instance();
    m_oTransactionManagement = new cTransactionManagement();

    addWindowListener(new WindowAdapter()
    {
      @Override
      public void windowClosing(WindowEvent e)
      {

        e.getWindow().dispose();
        DefaultTableModel oTransactionModel = (DefaultTableModel) tblFileHistory.getModel();
        int iRowNumber = oTransactionModel.getRowCount();
        for (int i = 0; i < iRowNumber; i++)
        {
          oTransactionModel.removeRow(0);
        }
      }
    });
  }

  /**
   * This method is called from within the constructor to initialize the form. WARNING: Do NOT modify this code. The
   * content of this method is always regenerated by the Form Editor.
   */
  @SuppressWarnings("unchecked")
  // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
  private void initComponents()
  {

    jScrollPane7 = new javax.swing.JScrollPane();
    tblFileHistory = new javax.swing.JTable();
    lblFile = new javax.swing.JLabel();
    lblStatus = new javax.swing.JLabel();

    setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

    tblFileHistory.setModel(new javax.swing.table.DefaultTableModel(
      new Object [][]
      {

      },
      new String []
      {
        "Name", "From", "To", "Value", "Currency", "Kind", "Commission Percent", "Time", "Block", "Tx Hash", "Gas Used"
      }
    )
    {
      Class[] types = new Class []
      {
        java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.Object.class, java.lang.String.class, java.lang.String.class
      };
      boolean[] canEdit = new boolean []
      {
        false, false, false, false, false, false, false, false, false, false, false
      };

      public Class getColumnClass(int columnIndex)
      {
        return types [columnIndex];
      }

      public boolean isCellEditable(int rowIndex, int columnIndex)
      {
        return canEdit [columnIndex];
      }
    });
    tblFileHistory.getTableHeader().setReorderingAllowed(false);
    jScrollPane7.setViewportView(tblFileHistory);

    lblFile.setText("File:");

    javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
    getContentPane().setLayout(layout);
    layout.setHorizontalGroup(
      layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
      .addGroup(layout.createSequentialGroup()
        .addContainerGap()
        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
          .addComponent(jScrollPane7, javax.swing.GroupLayout.DEFAULT_SIZE, 865, Short.MAX_VALUE)
          .addGroup(layout.createSequentialGroup()
            .addComponent(lblFile, javax.swing.GroupLayout.PREFERRED_SIZE, 534, javax.swing.GroupLayout.PREFERRED_SIZE)
            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
            .addComponent(lblStatus, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
        .addContainerGap())
    );
    layout.setVerticalGroup(
      layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
      .addGroup(layout.createSequentialGroup()
        .addContainerGap()
        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
          .addComponent(lblFile, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
          .addComponent(lblStatus, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
        .addComponent(jScrollPane7, javax.swing.GroupLayout.DEFAULT_SIZE, 451, Short.MAX_VALUE)
        .addContainerGap())
    );

    pack();
  }// </editor-fold>//GEN-END:initComponents

  // Variables declaration - do not modify//GEN-BEGIN:variables
  private javax.swing.JScrollPane jScrollPane7;
  private javax.swing.JLabel lblFile;
  private javax.swing.JLabel lblStatus;
  private javax.swing.JTable tblFileHistory;
  // End of variables declaration//GEN-END:variables

  public void setFile(String sFile)
  {
    lblFile.setText("File: " + sFile);
  }

  public void getHistoryOfFile(int iID)
  {
    new Thread(() ->
    {
      DefaultTableModel oTransactionModel = (DefaultTableModel) tblFileHistory.getModel();
      int iRowNumber = oTransactionModel.getRowCount();
      for (int i = 0; i < iRowNumber; i++)
      {
        oTransactionModel.removeRow(0);
      }
//      {
//        String sOriginal = "history";
//        String sTransaction = sOriginal + " " + iID; 
//        if (cTransactionManagement.m_oTransactions.containsKey(sTransaction))
//        {
//          System.out.println("Transaction already exists! : " + sTransaction);
//        }
//        else
//        {
//          cTransactionTypes oTransaction = cTransactionManagement.m_oTransactions.get(sOriginal);
//          System.out.println("Create Transaction: " + sTransaction);
//          m_oTransactionManagement.createTransaction(sTransaction, eTransactionKind.valueOf(oTransaction.getkind()), 
//              eCurrencyType.valueOf(oTransaction.getcurrency_type()), Float.parseFloat(oTransaction.getcurrency_value()), 
//              Float.parseFloat(oTransaction.getcommission_percent()));
//        }
//      }

      ArrayList<String> lsTransactions = m_oDatabase.getTransactions();

      for (int i = 0; i < lsTransactions.size(); i++)
      {
        String sTransaction = lsTransactions.get(i);
        lblStatus.setText("Lookup transaction: " + (i + 1) + " of " + lsTransactions.size());
        cResponse oResponse = m_oTransactionManagement.getTransactionStatus(sTransaction);
        if (oResponse != null && !oResponse.getsuccess())
        {
          String sErrorMsg = "";
          cError oError = oResponse.geterr();
          if (oError != null)
          {
            cErrorData[] error_data = oError.geterror_data();
            if (error_data != null)
            {
              for (cErrorData oErr : error_data)
              {
                sErrorMsg += oErr.getname() + "\n";
              }
            }
            showError(oResponse.geterr(), "Failed to create user: " + sErrorMsg);
          }
          else
          {
            showError(oResponse.geterr(), "Failed to create user");
          }
        }
        else
        {
          cTransactionTypes transaction_type = oResponse.getdata().gettransaction_types()[0];
          String[] lsTransactionName = transaction_type.getname().split(" ");

//          String sID = lsTransactionName[1].trim();
//          
//          cDatabaseHandler.instance().updateTransaction(sTransaction, sID);
          
          if (lsTransactionName.length > 1 && lsTransactionName[1].equals("" + iID))
          {
            cEconomyUser fromUser = oResponse.getdata().geteconomy_users()[0];
            cEconomyUser toUser = oResponse.getdata().geteconomy_users()[1];

            Vector<Object> vRow = new Vector<Object>();
            iRowNumber = oTransactionModel.getRowCount();
            oTransactionModel.addRow(vRow);

            //          String sName = transaction_type.getname();
            //          String[] sValues = sName.split(" ");
            //          String sOperation = sValues[0];
            //          String sFileId = sValues[1];
            //          if (sOperation.equalsIgnoreCase("share"))
            //          {
            //            if (cDatabaseHandler.instance().addShareFileIfNotExists(sFileId, toUser.getName()))
            //            {
            //              System.out.println("Added non-existing file share: '" + toUser.getName() + "' - " + sFileId);
            //            }
            //          }
            //          else if (sOperation.equalsIgnoreCase("access"))
            //          {
            //            if (cDatabaseHandler.instance().addShareFileIfNotExists(sFileId, fromUser.getName()))
            //            {
            //              System.out.println("Added non-existing file share: '" + fromUser.getName() + "' - " + sFileId);
            //            }
            //          }
            oTransactionModel.setValueAt(transaction_type.getname(), iRowNumber, getTransactionHistoryTableColumnIndexByHeading("Name"));
            oTransactionModel.setValueAt(transaction_type.getkind(), iRowNumber, getTransactionHistoryTableColumnIndexByHeading("Kind"));
            oTransactionModel.setValueAt(transaction_type.getcurrency_type(), iRowNumber, getTransactionHistoryTableColumnIndexByHeading("Currency"));
            oTransactionModel.setValueAt(transaction_type.getcurrency_value(), iRowNumber, getTransactionHistoryTableColumnIndexByHeading("Value"));
            oTransactionModel.setValueAt(transaction_type.getcommission_percent(), iRowNumber, getTransactionHistoryTableColumnIndexByHeading("Commission Percent"));
            oTransactionModel.setValueAt(fromUser.getName(), iRowNumber, getTransactionHistoryTableColumnIndexByHeading("From"));
            oTransactionModel.setValueAt(toUser.getName(), iRowNumber, getTransactionHistoryTableColumnIndexByHeading("To"));

            cTransaction transaction = oResponse.getdata().gettransaction();
            if (transaction != null)
            {
              String sDate = m_oSimpleDateFormat.format(new Date(transaction.gettransaction_timestamp()));
              oTransactionModel.setValueAt(sDate, iRowNumber, getTransactionHistoryTableColumnIndexByHeading("Time"));
              oTransactionModel.setValueAt(transaction.getblock_number(), iRowNumber, getTransactionHistoryTableColumnIndexByHeading("Block"));
              oTransactionModel.setValueAt(transaction.gettransaction_hash(), iRowNumber, getTransactionHistoryTableColumnIndexByHeading("Tx Hash"));
              oTransactionModel.setValueAt(transaction.gettransaction_fee(), iRowNumber, getTransactionHistoryTableColumnIndexByHeading("Tx Fee"));
            }
          }
        }
      }
      lblStatus.setText("");
    }).start();
  }
  
  public void getHistoryOfFileFromDatabase(int iID)
  {
    new Thread(() ->
    {
      DefaultTableModel oTransactionModel = (DefaultTableModel) tblFileHistory.getModel();
      int iRowNumber = oTransactionModel.getRowCount();
      for (int i = 0; i < iRowNumber; i++)
      {
        oTransactionModel.removeRow(0);
      }
      
      ArrayList<String> lsTransactions = m_oDatabase.getTransactions(iID);
      int iTotalTransactions = m_oDatabase.countTransactions();

      for (int i = 0; i < lsTransactions.size(); i++)
      {
        String sTransaction = lsTransactions.get(i);
        lblStatus.setText("Lookup transaction: " + (i + 1) + " of " + lsTransactions.size() + "  (Total Transactions: " + iTotalTransactions + ")");
        cResponse oResponse = m_oTransactionManagement.getTransactionStatus(sTransaction);
        if (oResponse != null && !oResponse.getsuccess())
        {
          String sErrorMsg = "";
          cError oError = oResponse.geterr();
          if (oError != null)
          {
            cErrorData[] error_data = oError.geterror_data();
            if (error_data != null)
            {
              for (cErrorData oErr : error_data)
              {
                sErrorMsg += oErr.getname() + "\n";
              }
            }
            showError(oResponse.geterr(), "Failed to create user: " + sErrorMsg);
          }
          else
          {
            showError(oResponse.geterr(), "Failed to create user");
          }
        }
        else
        {
          cTransactionTypes transaction_type = oResponse.getdata().gettransaction_types()[0];
          String[] lsTransactionName = transaction_type.getname().split(" ");

          if (lsTransactionName.length > 1 && lsTransactionName[1].equals("" + iID))
          {
            cEconomyUser fromUser = oResponse.getdata().geteconomy_users()[0];
            cEconomyUser toUser = oResponse.getdata().geteconomy_users()[1];

            Vector<Object> vRow = new Vector<Object>();
            iRowNumber = oTransactionModel.getRowCount();
            oTransactionModel.addRow(vRow);

            oTransactionModel.setValueAt(transaction_type.getname(), iRowNumber, getTransactionHistoryTableColumnIndexByHeading("Name"));
            oTransactionModel.setValueAt(transaction_type.getkind(), iRowNumber, getTransactionHistoryTableColumnIndexByHeading("Kind"));
            oTransactionModel.setValueAt(transaction_type.getcurrency_type(), iRowNumber, getTransactionHistoryTableColumnIndexByHeading("Currency"));
            oTransactionModel.setValueAt(transaction_type.getcurrency_value(), iRowNumber, getTransactionHistoryTableColumnIndexByHeading("Value"));
            oTransactionModel.setValueAt(transaction_type.getcommission_percent(), iRowNumber, getTransactionHistoryTableColumnIndexByHeading("Commission Percent"));
            oTransactionModel.setValueAt(fromUser.getName(), iRowNumber, getTransactionHistoryTableColumnIndexByHeading("From"));
            oTransactionModel.setValueAt(toUser.getName(), iRowNumber, getTransactionHistoryTableColumnIndexByHeading("To"));

            cTransaction transaction = oResponse.getdata().gettransaction();
            if (transaction != null)
            {
              String sDate = m_oSimpleDateFormat.format(new Date(transaction.gettransaction_timestamp()));
              oTransactionModel.setValueAt(sDate, iRowNumber, getTransactionHistoryTableColumnIndexByHeading("Time"));
              oTransactionModel.setValueAt(transaction.getblock_number(), iRowNumber, getTransactionHistoryTableColumnIndexByHeading("Block"));
              oTransactionModel.setValueAt(transaction.gettransaction_hash(), iRowNumber, getTransactionHistoryTableColumnIndexByHeading("Tx Hash"));
              oTransactionModel.setValueAt(transaction.gettransaction_fee(), iRowNumber, getTransactionHistoryTableColumnIndexByHeading("Tx Fee"));
            }
          }
        }
      }
      lblStatus.setText("");
    }).start();
  }

  public int getTransactionHistoryTableColumnIndexByHeading(String _sColumnHeading)
  {
    DefaultTableModel oTransactionModel = (DefaultTableModel) tblFileHistory.getModel();
    for (int iCol = 0; iCol < oTransactionModel.getColumnCount(); iCol++)
    {
      if (oTransactionModel.getColumnName(iCol).equals(_sColumnHeading))
      {
        return iCol;
      }
    }
    return -1;
  }
}
