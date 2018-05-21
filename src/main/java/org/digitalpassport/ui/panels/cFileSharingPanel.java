package org.digitalpassport.ui.panels;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import static java.awt.event.MouseEvent.BUTTON3;
import java.awt.event.MouseListener;
import java.io.File;
import java.util.ArrayList;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.table.DefaultTableModel;
import org.digitalpassport.api.commands.cTransactionManagement;
import org.digitalpassport.api.commands.eCurrencyType;
import org.digitalpassport.api.commands.eTransactionKind;
import org.digitalpassport.cryptography.cHashing;
import org.digitalpassport.deserialize.json.transactiontypes.cTransactionTypes;
import org.digitalpassport.jdbc.cDatabaseHandler;
import org.digitalpassport.passport.cDatabaseFile;
import org.digitalpassport.ui.cRegisterFrame;
import org.digitalpassport.ui.cSelectUser;

/**
 *
 * @author Philip M. Trenwith
 */
public class cFileSharingPanel extends javax.swing.JPanel
{
  private cSelectUser m_oSelectUser = new cSelectUser();
  private cTransactionManagement m_oTransactionManagement = null;
  private cRegisterFrame m_oRegistrationFrame = null;
  private boolean m_bLogin = false;
  private String m_sUsername = "";
  
  private JPopupMenu m_oPopupMenu = new JPopupMenu("Popup");
  private JMenuItem m_oShareMenuItem = new JMenuItem("Share");
  private JMenuItem m_oLikeMenuItem = new JMenuItem("Like");
  private JMenuItem m_oAccessMenuItem = new JMenuItem("Access");
  private JMenuItem m_oHistoryMenuItem = new JMenuItem("History");

  class PopupTriggerListener implements MouseListener
  {
    public PopupTriggerListener()
    {
    }

    @Override
    public void mouseClicked(MouseEvent ev)
    {
      if (ev.getButton() == BUTTON3)
      {
        m_oPopupMenu.show(ev.getComponent(), ev.getX(), ev.getY());
      }
    }

    @Override
    public void mousePressed(MouseEvent ev)
    {
    }

    @Override
    public void mouseReleased(MouseEvent ev)
    {
    }

    @Override
    public void mouseEntered(MouseEvent e)
    {
    }

    @Override
    public void mouseExited(MouseEvent e)
    {
    }
  }

  /**
   * Creates new form cFileSharingPanel
   */
  public cFileSharingPanel()
  {
    initComponents();

    m_oSelectUser = new cSelectUser();
    m_oSelectUser.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
    btnLogin.setActionCommand("login");
    m_oTransactionManagement = new cTransactionManagement();
    m_oRegistrationFrame = new cRegisterFrame(this, m_oTransactionManagement);

    m_oShareMenuItem.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        int iRow = tblFiles.getSelectedRow();
        int iID = Integer.parseInt(tblFiles.getValueAt(iRow, 0)+"");
        String sOriginal = "share";
        String sTransaction = sOriginal + " " + iID; 
        if (!cTransactionManagement.m_oTransactions.containsKey(sTransaction))
        {
          System.out.println("Share with: " + sTransaction);
        }
        else
        {
          cTransactionTypes oTransaction = cTransactionManagement.m_oTransactions.get(sOriginal);
          System.out.println("Create Transaction: " + sTransaction);
          m_oTransactionManagement.createTransaction_sandbox(sTransaction, eTransactionKind.valueOf(oTransaction.getkind()), 
              eCurrencyType.valueOf(oTransaction.getcurrency_type()), Float.parseFloat(oTransaction.getcurrency_value()), 
              Float.parseFloat(oTransaction.getcommission_percent()));
        }
        m_oSelectUser.setTitle("Share");
        m_oSelectUser.setBounds(getX() + getWidth()/2, getY() + getHeight()/2, 
                  m_oSelectUser.getWidth(), m_oSelectUser.getHeight());
        m_oSelectUser.setUsersExcluding(txtUsername.getText());
        
        m_oSelectUser.setVisible(true);
      }
    });
    m_oLikeMenuItem.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        int iRow = tblFiles.getSelectedRow();
        int iID = Integer.parseInt(tblFiles.getValueAt(iRow, 0)+"");
        String sOriginal = "like";
        String sTransaction = sOriginal + " " + iID; 
        if (cTransactionManagement.m_oTransactions.containsKey(sTransaction))
        {
          System.out.println("Share with: " + sTransaction);
        }
        else
        {
          cTransactionTypes oTransaction = cTransactionManagement.m_oTransactions.get(sOriginal);
          System.out.println("Create Transaction: " + sTransaction);
          m_oTransactionManagement.createTransaction(sTransaction, eTransactionKind.valueOf(oTransaction.getkind()), 
              eCurrencyType.valueOf(oTransaction.getcurrency_type()), Float.parseFloat(oTransaction.getcurrency_value()), 
              Float.parseFloat(oTransaction.getcommission_percent()));
        }
        m_oSelectUser.setTitle("Like");
        m_oSelectUser.setBounds(getX() + getWidth()/2, getY() + getHeight()/2, 
                  m_oSelectUser.getWidth(), m_oSelectUser.getHeight());
        m_oSelectUser.setUsersExcluding(txtUsername.getText());
        
        m_oSelectUser.setVisible(true);
      }
    });
    m_oAccessMenuItem.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        int iRow = tblFiles.getSelectedRow();
        int iID = Integer.parseInt(tblFiles.getValueAt(iRow, 0)+"");
        String sOriginal = "access";
        String sTransaction = sOriginal + " " + iID; 
        if (cTransactionManagement.m_oTransactions.containsKey(sTransaction))
        {
          System.out.println("Access item: " + sTransaction);
        }
        else
        {
          cTransactionTypes oTransaction = cTransactionManagement.m_oTransactions.get(sOriginal);
          System.out.println("Create Transaction: " + sTransaction);
          m_oTransactionManagement.createTransaction(sTransaction, eTransactionKind.valueOf(oTransaction.getkind()), 
              eCurrencyType.valueOf(oTransaction.getcurrency_type()), Float.parseFloat(oTransaction.getcurrency_value()), 
              Float.parseFloat(oTransaction.getcommission_percent()));
        }
        m_oSelectUser.setTitle("Access");
        m_oSelectUser.setBounds(getX() + getWidth()/2, getY() + getHeight()/2, 
                  m_oSelectUser.getWidth(), m_oSelectUser.getHeight());
        m_oSelectUser.setUsersExcluding(txtUsername.getText());
        
        m_oSelectUser.setVisible(true);
      }
    });
    m_oHistoryMenuItem.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        int iRow = tblFiles.getSelectedRow();
        int iID = Integer.parseInt(tblFiles.getValueAt(iRow, 0)+"");
        String sOriginal = "history";
        String sTransaction = sOriginal + " " + iID; 
//        if (cTransactionManagement.m_oTransactions.containsKey(sTransaction))
//        {
//          System.out.println("History of: " + sTransaction);
//        }
//        else
//        {
//          cTransactionTypes oTransaction = cTransactionManagement.m_oTransactions.get(sOriginal);
//          System.out.println("Create Transaction: " + sTransaction);
//          m_oTransactionManagement.createTransaction(sTransaction, eTransactionKind.valueOf(oTransaction.getkind()), 
//              eCurrencyType.valueOf(oTransaction.getcurrency_type()), Float.parseFloat(oTransaction.getcurrency_value()), 
//              Float.parseFloat(oTransaction.getcommission_percent()));
//        }
        m_oSelectUser.setTitle("History");
        m_oSelectUser.setBounds(getX() + getWidth()/2, getY() + getHeight()/2, 
                  m_oSelectUser.getWidth(), m_oSelectUser.getHeight());
        m_oSelectUser.setUsersExcluding(txtUsername.getText());
        
        m_oSelectUser.setVisible(true);
      }
    });
    
    m_oPopupMenu.add(m_oShareMenuItem);
    //m_oPopupMenu.add(m_oLikeMenuItem);
    //m_oPopupMenu.add(m_oAccessMenuItem);
    m_oPopupMenu.add(m_oHistoryMenuItem);
    tblFiles.addMouseListener(new PopupTriggerListener());
  }

  public void setUsername(String sUsername)
  {
    m_bLogin = true;
    m_sUsername = sUsername;
    txtUsername.setText(sUsername);
    setLogin();
  }

  /**
   * This method is called from within the constructor to initialize the form. WARNING: Do NOT modify this code. The
   * content of this method is always regenerated by the Form Editor.
   */
  @SuppressWarnings("unchecked")
  // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
  private void initComponents()
  {

    btnUploadFile = new javax.swing.JButton();
    jScrollPane1 = new javax.swing.JScrollPane();
    tblFiles = new javax.swing.JTable();
    btnLogin = new javax.swing.JButton();
    lblPassword = new javax.swing.JLabel();
    txtUsername = new javax.swing.JTextField();
    lblUsername = new javax.swing.JLabel();
    btnRegister = new javax.swing.JButton();
    txtPassword = new javax.swing.JPasswordField();

    btnUploadFile.setText("Upload");
    btnUploadFile.setEnabled(false);
    btnUploadFile.addActionListener(new java.awt.event.ActionListener()
    {
      public void actionPerformed(java.awt.event.ActionEvent evt)
      {
        btnUploadFileActionPerformed(evt);
      }
    });

    tblFiles.setModel(new javax.swing.table.DefaultTableModel(
      new Object [][]
      {

      },
      new String []
      {
        "ID", "Filename", "Owner"
      }
    )
    {
      Class[] types = new Class []
      {
        java.lang.String.class, java.lang.String.class, java.lang.String.class
      };
      boolean[] canEdit = new boolean []
      {
        false, false, false
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
    tblFiles.setEnabled(false);
    jScrollPane1.setViewportView(tblFiles);

    btnLogin.setText("Login");
    btnLogin.addActionListener(new java.awt.event.ActionListener()
    {
      public void actionPerformed(java.awt.event.ActionEvent evt)
      {
        btnLoginActionPerformed(evt);
      }
    });

    lblPassword.setText("Password:");

    txtUsername.setText("philipt");

    lblUsername.setText("Username:");

    btnRegister.setText("Register");
    btnRegister.addActionListener(new java.awt.event.ActionListener()
    {
      public void actionPerformed(java.awt.event.ActionEvent evt)
      {
        btnRegisterActionPerformed(evt);
      }
    });

    txtPassword.setText("1234");
    txtPassword.setToolTipText("");

    javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
    this.setLayout(layout);
    layout.setHorizontalGroup(
      layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
      .addGroup(layout.createSequentialGroup()
        .addContainerGap()
        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
          .addComponent(jScrollPane1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 953, Short.MAX_VALUE)
          .addGroup(layout.createSequentialGroup()
            .addComponent(btnUploadFile)
            .addGap(0, 0, Short.MAX_VALUE))
          .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
            .addGap(0, 0, Short.MAX_VALUE)
            .addComponent(lblUsername, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE)
            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
            .addComponent(txtUsername, javax.swing.GroupLayout.PREFERRED_SIZE, 130, javax.swing.GroupLayout.PREFERRED_SIZE)
            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
            .addComponent(lblPassword, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE)
            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
            .addComponent(txtPassword, javax.swing.GroupLayout.PREFERRED_SIZE, 130, javax.swing.GroupLayout.PREFERRED_SIZE)
            .addGap(8, 8, 8)
            .addComponent(btnLogin)
            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
            .addComponent(btnRegister)))
        .addContainerGap())
    );
    layout.setVerticalGroup(
      layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
      .addGroup(layout.createSequentialGroup()
        .addGap(10, 10, 10)
        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
          .addComponent(btnLogin)
          .addComponent(lblPassword)
          .addComponent(txtUsername, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
          .addComponent(lblUsername)
          .addComponent(btnRegister)
          .addComponent(txtPassword, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
        .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 395, Short.MAX_VALUE)
        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
        .addComponent(btnUploadFile)
        .addContainerGap())
    );
  }// </editor-fold>//GEN-END:initComponents

  private void btnUploadFileActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_btnUploadFileActionPerformed
  {//GEN-HEADEREND:event_btnUploadFileActionPerformed
    JFileChooser fileChooser = new JFileChooser();
    int result = fileChooser.showOpenDialog(tblFiles);
    if (result == JFileChooser.APPROVE_OPTION)
    {
      File oFile = fileChooser.getSelectedFile();
      cDatabaseHandler.instance().uploadPassport(oFile.getName(), m_sUsername);
      loadFiles();
    }
  }//GEN-LAST:event_btnUploadFileActionPerformed

  private void btnRegisterActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_btnRegisterActionPerformed
  {//GEN-HEADEREND:event_btnRegisterActionPerformed
    m_oRegistrationFrame.setBounds(getX() + getWidth() / 2, getY() + getHeight() / 2, m_oRegistrationFrame.getWidth(), m_oRegistrationFrame.getHeight());
    m_oRegistrationFrame.setVisible(true);
  }//GEN-LAST:event_btnRegisterActionPerformed

  private void btnLoginActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_btnLoginActionPerformed
  {//GEN-HEADEREND:event_btnLoginActionPerformed
    if (btnLogin.getActionCommand().equals("login"))
    {
      m_sUsername = txtUsername.getText();
      char[] cPassword = txtPassword.getPassword();
      String sPasswordHash = cHashing.hash(cPassword);
      cDatabaseHandler oDatabase = cDatabaseHandler.instance();
      String sDisplayName = oDatabase.login(m_sUsername, sPasswordHash);
      if (!sDisplayName.isEmpty())
      {
        m_bLogin = true;
        setLogin();
      }
    }
    else if (btnLogin.getActionCommand().equals("logout"))
    {
      m_bLogin = false;
      setLogin();
    }
  }//GEN-LAST:event_btnLoginActionPerformed

  private void setLogin()
  {
    if (m_bLogin)
    {
      btnLogin.setText("Logout");
      btnLogin.setActionCommand("logout");
    }
    else
    {
      btnLogin.setText("Login");
      btnLogin.setActionCommand("login");
    }

    lblPassword.setEnabled(!m_bLogin);
    txtPassword.setEnabled(!m_bLogin);
    lblUsername.setEnabled(!m_bLogin);
    txtUsername.setEnabled(!m_bLogin);
    btnRegister.setEnabled(!m_bLogin);
    tblFiles.setEnabled(m_bLogin);
    btnUploadFile.setEnabled(m_bLogin);

    loadFiles();
  }

  private void loadFiles()
  {
    new Thread(new Runnable()
    {
      @Override
      public void run()
      {
        cDatabaseHandler oDatabase = cDatabaseHandler.instance();
        ArrayList<cDatabaseFile> lsFiles = new ArrayList();
        if (m_bLogin)
        {
          lsFiles = oDatabase.getFilesForOwner(m_sUsername);
        }
        int rowCount = tblFiles.getRowCount();
        DefaultTableModel model = (DefaultTableModel) tblFiles.getModel();
        for (int i = 0; i < rowCount; i++)
        {
          model.removeRow(0);
        }

        for (cDatabaseFile oFile : lsFiles)
        {
          // Append a row 
          model.addRow(new Object[]
          {
            oFile.m_sFileID, oFile.m_sFilename, oFile.m_sOwner
          });
        }
      }
    }).start();
  }

  // Variables declaration - do not modify//GEN-BEGIN:variables
  private javax.swing.JButton btnLogin;
  private javax.swing.JButton btnRegister;
  private javax.swing.JButton btnUploadFile;
  private javax.swing.JScrollPane jScrollPane1;
  private javax.swing.JLabel lblPassword;
  private javax.swing.JLabel lblUsername;
  private javax.swing.JTable tblFiles;
  private javax.swing.JPasswordField txtPassword;
  private javax.swing.JTextField txtUsername;
  // End of variables declaration//GEN-END:variables

}
