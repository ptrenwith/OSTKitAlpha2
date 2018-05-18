
package org.digitalpassport.ui;

import java.sql.ResultSet;
import java.util.Arrays;
import org.digitalpassport.api.commands.cTransactionManagement;
import org.digitalpassport.api.commands.cUserManagement;
import org.digitalpassport.cryptography.cHashing;
import org.digitalpassport.deserialize.json.cError;
import org.digitalpassport.deserialize.json.cErrorData;
import org.digitalpassport.deserialize.json.cResponse;
import org.digitalpassport.jdbc.cDatabaseHandler;
import static org.digitalpassport.ui.panels.cTokenManagementPanel.showError;
import static org.digitalpassport.ui.panels.cTokenManagementPanel.showInfo;

/**
 *
 * @author PHilip M. Trenwith
 */
public class cRegisterFrame extends javax.swing.JFrame
{
  private cUserManagement m_oUserManagement = new cUserManagement();
  private cTransactionManagement m_oTransactionManagement = new cTransactionManagement();
  
  /**
   * Creates new form cRegisterFrame
   */
  public cRegisterFrame()
  {
    initComponents();
    
    lblUsernameInUse.setVisible(false);
    lblPasswordNotMatch.setVisible(false);
  }

  /**
   * This method is called from within the constructor to initialize the form.
   * WARNING: Do NOT modify this code. The content of this method is always
   * regenerated by the Form Editor.
   */
  @SuppressWarnings("unchecked")
  // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
  private void initComponents()
  {

    lblName = new javax.swing.JLabel();
    txtName = new javax.swing.JTextField();
    lblSurname = new javax.swing.JLabel();
    txtSurname = new javax.swing.JTextField();
    lblUsernameInUse = new javax.swing.JLabel();
    lblPasswordNotMatch = new javax.swing.JLabel();
    btnRegister = new javax.swing.JButton();
    btnCancel = new javax.swing.JButton();
    txtUsername = new javax.swing.JTextField();
    txtPassword1 = new javax.swing.JPasswordField();
    txtPassword2 = new javax.swing.JPasswordField();
    lblPassword2 = new javax.swing.JLabel();
    lblPassword = new javax.swing.JLabel();
    jLabel1 = new javax.swing.JLabel();
    chbAirdropDPTAfterRegistration = new javax.swing.JCheckBox();

    setTitle("Register");
    setName("RegisterFrame"); // NOI18N
    setResizable(false);

    lblName.setText("Name:");

    txtName.setToolTipText("");

    lblSurname.setText("Surname:");

    lblUsernameInUse.setForeground(new java.awt.Color(255, 0, 0));
    lblUsernameInUse.setText("Username in use");
    lblUsernameInUse.setToolTipText("");

    lblPasswordNotMatch.setForeground(new java.awt.Color(255, 0, 0));
    lblPasswordNotMatch.setText("Passwords does not match");

    btnRegister.setText("Register");
    btnRegister.addActionListener(new java.awt.event.ActionListener()
    {
      public void actionPerformed(java.awt.event.ActionEvent evt)
      {
        btnRegisterActionPerformed(evt);
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

    lblPassword2.setText("Confirm Password:");

    lblPassword.setText("Password:");

    jLabel1.setText("Username:");

    chbAirdropDPTAfterRegistration.setText("Airdrop 10 DPT after registration");

    javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
    getContentPane().setLayout(layout);
    layout.setHorizontalGroup(
      layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
      .addGroup(layout.createSequentialGroup()
        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
          .addGroup(layout.createSequentialGroup()
            .addContainerGap()
            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
              .addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
                .addComponent(lblPassword2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(txtPassword2, javax.swing.GroupLayout.DEFAULT_SIZE, 140, Short.MAX_VALUE))
              .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                  .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addComponent(lblPassword, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel1, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                  .addComponent(lblName))
                .addGap(52, 52, 52)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                  .addComponent(txtPassword1, javax.swing.GroupLayout.DEFAULT_SIZE, 140, Short.MAX_VALUE)
                  .addComponent(txtName)
                  .addComponent(txtUsername))))
            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
              .addComponent(lblUsernameInUse, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
              .addGroup(layout.createSequentialGroup()
                .addComponent(lblSurname)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(txtSurname, javax.swing.GroupLayout.PREFERRED_SIZE, 140, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
              .addComponent(lblPasswordNotMatch, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
          .addGroup(layout.createSequentialGroup()
            .addGap(33, 33, 33)
            .addComponent(chbAirdropDPTAfterRegistration, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
            .addComponent(btnRegister)
            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
            .addComponent(btnCancel)))
        .addContainerGap())
    );
    layout.setVerticalGroup(
      layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
      .addGroup(layout.createSequentialGroup()
        .addContainerGap()
        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
          .addComponent(lblName)
          .addComponent(lblSurname)
          .addComponent(txtName, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
          .addComponent(txtSurname, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
          .addComponent(txtUsername, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
          .addComponent(jLabel1)
          .addComponent(lblUsernameInUse))
        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
          .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
            .addComponent(lblPassword)
            .addComponent(lblPasswordNotMatch))
          .addComponent(txtPassword1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 19, javax.swing.GroupLayout.PREFERRED_SIZE))
        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
          .addComponent(txtPassword2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
          .addComponent(lblPassword2))
        .addGap(18, 18, 18)
        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
          .addComponent(btnCancel)
          .addComponent(btnRegister)
          .addComponent(chbAirdropDPTAfterRegistration))
        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
    );

    pack();
  }// </editor-fold>//GEN-END:initComponents

  private void btnCancelActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_btnCancelActionPerformed
  {//GEN-HEADEREND:event_btnCancelActionPerformed
    close();
  }//GEN-LAST:event_btnCancelActionPerformed

  private void btnRegisterActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_btnRegisterActionPerformed
  {//GEN-HEADEREND:event_btnRegisterActionPerformed
    new Thread(()->{register();}).start();
  }//GEN-LAST:event_btnRegisterActionPerformed

  public void register()
  {
    boolean bRegistered = false;
    
    lblUsernameInUse.setVisible(false);
    lblPasswordNotMatch.setVisible(false);
    
    cDatabaseHandler oDatabase = null;
    try
    {
      if (!Arrays.equals(txtPassword1.getPassword(), txtPassword2.getPassword()))
      {
        lblPasswordNotMatch.setVisible(true);
      }
      else
      {
        oDatabase = new cDatabaseHandler();
        ResultSet oUserResultSet = oDatabase.select("select * from dpt_users where Username='" + txtUsername.getText() + "'");
        if (oUserResultSet.next()) 
        {
          lblUsernameInUse.setVisible(true);
        }
        else
        {
          String sUsername = txtUsername.getText();
          String sPasswordHash = cHashing.hash(txtPassword1.getPassword());
          String sDisplayName = txtName.getText() + " " + txtSurname.getText();
          bRegistered = oDatabase.register(sUsername, sPasswordHash, sDisplayName);
          
          if (bRegistered)
          {
            // create the user in the DPT economy
            cResponse oResponse = m_oUserManagement.createUser(sDisplayName);
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
              showInfo("User Registered :-)");
              // if so selected airdrop ten tokens to the user
              if (chbAirdropDPTAfterRegistration.isSelected())
              {
                m_oUserManagement.sendAirdropTo(sUsername, 10);
              }
            }
          }
          else
          {
            showError(null, "Failed to register user");
          }
        }
      }
    }
    catch (Exception ex)
    {
      System.err.println("Error registering: " + ex.getMessage());
      ex.printStackTrace();
    }
    finally
    {
      if (oDatabase != null)
      {
        oDatabase.terminate();
      }
    }
  }      
  
  public void close()
  {
    txtName.setText("");
    txtSurname.setText("");
    txtUsername.setText("");
    txtPassword1.setText("");
    txtPassword2.setText("");
    
    lblUsernameInUse.setVisible(false);
    lblPasswordNotMatch.setVisible(false);
    
    this.setVisible(false);
  }

  // Variables declaration - do not modify//GEN-BEGIN:variables
  private javax.swing.JButton btnCancel;
  private javax.swing.JButton btnRegister;
  private javax.swing.JCheckBox chbAirdropDPTAfterRegistration;
  private javax.swing.JLabel jLabel1;
  private javax.swing.JLabel lblName;
  private javax.swing.JLabel lblPassword;
  private javax.swing.JLabel lblPassword2;
  private javax.swing.JLabel lblPasswordNotMatch;
  private javax.swing.JLabel lblSurname;
  private javax.swing.JLabel lblUsernameInUse;
  private javax.swing.JTextField txtName;
  private javax.swing.JPasswordField txtPassword1;
  private javax.swing.JPasswordField txtPassword2;
  private javax.swing.JTextField txtSurname;
  private javax.swing.JTextField txtUsername;
  // End of variables declaration//GEN-END:variables
}
