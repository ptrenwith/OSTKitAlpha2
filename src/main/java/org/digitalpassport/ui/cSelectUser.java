
package org.digitalpassport.ui;

import java.util.Iterator;
import org.digitalpassport.api.commands.cTransactionManagement;
import org.digitalpassport.api.commands.cUserManagement;
import org.digitalpassport.deserialize.json.cResponse;
import org.digitalpassport.deserialize.json.transactiontypes.cTransactionTypes;
import org.digitalpassport.jdbc.cDatabaseHandler;
import org.digitalpassport.ui.panels.cTokenManagementPanel;

/**
 *
 * @author Philip
 */
public class cSelectUser extends javax.swing.JFrame
{
  private cTransactionManagement m_oTransactionManagement = null;
  private String m_sTransactionName = "";
  private String m_sFromUUID = "";
  private String m_sToUUID = "";
  
  public cSelectUser(cTransactionManagement oTransactionManagement)
  {
    m_oTransactionManagement = oTransactionManagement;
    initComponents();
  }

  /**
   * This method is called from within the constructor to initialize the form. WARNING: Do NOT modify this code. The
   * content of this method is always regenerated by the Form Editor.
   */
  @SuppressWarnings("unchecked")
  // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
  private void initComponents()
  {

    lblQuestion = new javax.swing.JLabel();
    cmbUsers = new javax.swing.JComboBox<>();
    btnOK = new javax.swing.JButton();
    btnCancel = new javax.swing.JButton();

    setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

    lblQuestion.setText("With Whom?");

    btnOK.setText("OK");
    btnOK.addActionListener(new java.awt.event.ActionListener()
    {
      public void actionPerformed(java.awt.event.ActionEvent evt)
      {
        btnOKActionPerformed(evt);
      }
    });

    btnCancel.setText("Cancel");
    btnCancel.addActionListener(new java.awt.event.ActionListener()
    {
      public void actionPerformed(java.awt.event.ActionEvent evt)
      {
        btnCancelActionPerformed(evt);
      }
    });

    javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
    getContentPane().setLayout(layout);
    layout.setHorizontalGroup(
      layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
      .addGroup(layout.createSequentialGroup()
        .addContainerGap()
        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
          .addComponent(lblQuestion, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
          .addComponent(cmbUsers, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
          .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
            .addGap(0, 379, Short.MAX_VALUE)
            .addComponent(btnCancel)
            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
            .addComponent(btnOK, javax.swing.GroupLayout.PREFERRED_SIZE, 65, javax.swing.GroupLayout.PREFERRED_SIZE)))
        .addContainerGap())
    );
    layout.setVerticalGroup(
      layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
      .addGroup(layout.createSequentialGroup()
        .addContainerGap()
        .addComponent(lblQuestion)
        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
        .addComponent(cmbUsers, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
        .addGap(17, 17, 17)
        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
          .addComponent(btnOK)
          .addComponent(btnCancel))
        .addContainerGap())
    );

    pack();
  }// </editor-fold>//GEN-END:initComponents

  private void btnCancelActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_btnCancelActionPerformed
  {//GEN-HEADEREND:event_btnCancelActionPerformed
    setVisible(false);
  }//GEN-LAST:event_btnCancelActionPerformed

  private void btnOKActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_btnOKActionPerformed
  {//GEN-HEADEREND:event_btnOKActionPerformed
    String sUser = cmbUsers.getSelectedItem()+"";//sUsernameFromUsers + " (" + cUserManagement.m_oUsers.get(sUsernameFromUsers) + ")";
    int iIndex = sUser.indexOf("(");
    m_sToUUID = sUser.substring(iIndex+1, sUser.length()-1);
    cTransactionTypes oTransaction = cTransactionManagement.m_oTransactions.get(m_sTransactionName);
    String sTransactionId = oTransaction.getid();
    cResponse oResponse = m_oTransactionManagement.executeTransaction_sandbox(m_sFromUUID, m_sToUUID, sTransactionId);
    if (oResponse.geterr() != null && !oResponse.getsuccess())
    {
      cTokenManagementPanel.showError(oResponse.geterr(), "Transaction failed to execute!");
    }
    else
    {
      cTokenManagementPanel.showInfo("Transaction complete!");
      String transaction_uuid = oResponse.getdata().gettransaction().getid();
      cDatabaseHandler.instance().saveTransaction(transaction_uuid);
    }
    setVisible(false);
  }//GEN-LAST:event_btnOKActionPerformed

  // Variables declaration - do not modify//GEN-BEGIN:variables
  private javax.swing.JButton btnCancel;
  private javax.swing.JButton btnOK;
  public static javax.swing.JComboBox<String> cmbUsers;
  private javax.swing.JLabel lblQuestion;
  // End of variables declaration//GEN-END:variables

  public void setUsersExcluding(String sUsername)
  {
    cmbUsers.removeAllItems();
    Iterator<String> oIterator = cUserManagement.m_oUsers.keySet().iterator();
    while (oIterator.hasNext())
    {
      String sUsernameFromUsers = oIterator.next();
      if (!sUsernameFromUsers.equalsIgnoreCase(sUsername))
      {
        String sUser = sUsernameFromUsers + " (" + cUserManagement.m_oUsers.get(sUsernameFromUsers) + ")";
        cmbUsers.addItem(sUser);
      }
      else
      {
        m_sFromUUID = cUserManagement.m_oUsers.get(sUsernameFromUsers);
      }
    }
  }
  
  public void setTransactionName(String sName)
  {
    m_sTransactionName = sName;
  }
}
