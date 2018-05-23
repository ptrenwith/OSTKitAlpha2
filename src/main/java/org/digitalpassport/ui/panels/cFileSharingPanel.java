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
import org.digitalpassport.deserialize.json.cResponse;
import org.digitalpassport.deserialize.json.transactiontypes.cTransactionTypes;
import org.digitalpassport.jdbc.cDatabaseHandler;
import org.digitalpassport.passport.cDatabaseFile;
import org.digitalpassport.ui.cHistoryFrame;
import org.digitalpassport.ui.cRegisterFrame;
import org.digitalpassport.ui.cSelectUser;

/**
 *
 * @author Philip M. Trenwith
 */
public class cFileSharingPanel extends javax.swing.JPanel
{
  private cHistoryFrame m_oHistoryFrame = new cHistoryFrame();
  private cSelectUser m_oSelectUser = null;
  private cTransactionManagement m_oTransactionManagement = null;
  private cRegisterFrame m_oRegistrationFrame = null;
  private boolean m_bLogin = false;
  private String m_sUsername = "";
  private String m_sDisplayName = "";

  private JPopupMenu m_oYourPopupMenu = new JPopupMenu("YourPopup");
  private JPopupMenu m_oOtherPopupMenu = new JPopupMenu("OtherPopup");
  private JMenuItem m_oShareMenuItem = new JMenuItem("Share");
  private JMenuItem m_oLikeMenuItem = new JMenuItem("Like");
  private JMenuItem m_oAccessMenuItem = new JMenuItem("Access");
  private JMenuItem m_oHistoryMenuItem = new JMenuItem("History");

  class cYourPopupTriggerListener implements MouseListener
  {
    public cYourPopupTriggerListener()
    {
    }

    @Override
    public void mouseClicked(MouseEvent ev)
    {
      if (ev.getButton() == BUTTON3)
      {
        m_oYourPopupMenu.show(ev.getComponent(), ev.getX(), ev.getY());
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

  class cOtherPopupTriggerListener implements MouseListener
  {
    public cOtherPopupTriggerListener()
    {
    }

    @Override
    public void mouseClicked(MouseEvent ev)
    {
      if (ev.getButton() == BUTTON3)
      {
        m_oOtherPopupMenu.show(ev.getComponent(), ev.getX(), ev.getY());
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

    m_oHistoryFrame.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
    
    btnLogin.setActionCommand("login");
    m_oTransactionManagement = new cTransactionManagement();
    m_oRegistrationFrame = new cRegisterFrame(this, m_oTransactionManagement);
    m_oSelectUser = new cSelectUser(m_oTransactionManagement);
    m_oSelectUser.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);

    m_oShareMenuItem.addActionListener(new ActionListener()
    {
      public void actionPerformed(ActionEvent e)
      {
        int iRow = tblYourFiles.getSelectedRow();
        int iID = Integer.parseInt(tblYourFiles.getValueAt(iRow, 0) + "");
        String sOriginal = "share";
        String sTransaction = sOriginal + " " + iID;
        if (!cTransactionManagement.m_oTransactions.containsKey(sTransaction))
        {
          System.out.println("Transaction already exists! : " + sTransaction);
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
        m_oSelectUser.setParameters(sOriginal, m_sDisplayName);
        m_oSelectUser.setBounds(getX() + getWidth() / 2, getY() + getHeight() / 2,
                m_oSelectUser.getWidth(), m_oSelectUser.getHeight());
        m_oSelectUser.setUsersExcluding(m_sDisplayName);
        m_oSelectUser.setTransactionName(sTransaction);
        m_oSelectUser.setVisible(true);
      }
    });
    
    m_oHistoryMenuItem.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        int iRow = tblYourFiles.getSelectedRow();
        int iID = Integer.parseInt(tblYourFiles.getValueAt(iRow, 0)+"");
        String sFile = tblYourFiles.getValueAt(iRow, 1)+"";
        
        m_oHistoryFrame.setTitle("History");
        m_oHistoryFrame.setBounds(getX()+50, getY()+150,  getWidth(), getHeight());
        m_oHistoryFrame.setFile(sFile);
        m_oHistoryFrame.getHistoryOfFile(iID);
        m_oHistoryFrame.setVisible(true);
      }
    });

    m_oLikeMenuItem.addActionListener(new ActionListener()
    {
      public void actionPerformed(ActionEvent e)
      {
        int iRow = tblYourFiles.getSelectedRow();
        int iID = Integer.parseInt(tblYourFiles.getValueAt(iRow, 0) + "");
        String sOwner = tblYourFiles.getValueAt(iRow, 2)+"";
        sOwner = sOwner.replaceAll("(shared)", "").trim();
        
        String sOriginal = "like";
        String sTransaction = sOriginal + " " + iID;
        if (cTransactionManagement.m_oTransactions.containsKey(sTransaction))
        {
          System.out.println("Transaction already exists! : " + sTransaction);
        }
        else
        {
          cTransactionTypes oTransaction = cTransactionManagement.m_oTransactions.get(sOriginal);
          System.out.println("Create Transaction: " + sTransaction);
          m_oTransactionManagement.createTransaction(sTransaction, eTransactionKind.valueOf(oTransaction.getkind()),
                  eCurrencyType.valueOf(oTransaction.getcurrency_type()), Float.parseFloat(oTransaction.getcurrency_value()),
                  Float.parseFloat(oTransaction.getcommission_percent()));
        }
        String sFromUUID = cDatabaseHandler.instance().getUuid(m_sDisplayName);
        String sToUUID = cDatabaseHandler.instance().getUuid(sOwner);
        cResponse oResponse = m_oTransactionManagement.executeTransaction_sandbox(sFromUUID, sToUUID, sTransaction);
      }
    });

    m_oAccessMenuItem.addActionListener(new ActionListener()
    {
      public void actionPerformed(ActionEvent e)
      {
        int iRow = tblYourFiles.getSelectedRow();
        int iID = Integer.parseInt(tblYourFiles.getValueAt(iRow, 0) + "");
        String sOriginal = "access";
        String sTransaction = sOriginal + " " + iID;
        if (cTransactionManagement.m_oTransactions.containsKey(sTransaction))
        {
          System.out.println("Transcation already exists! : " + sTransaction);
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
        m_oSelectUser.setBounds(getX() + getWidth() / 2, getY() + getHeight() / 2,
                m_oSelectUser.getWidth(), m_oSelectUser.getHeight());
        m_oSelectUser.setUsersExcluding(m_sDisplayName);
        m_oSelectUser.setParameters(sOriginal, m_sDisplayName);
        m_oSelectUser.setTransactionName(sTransaction);
        m_oSelectUser.setVisible(true);
      }
    });

    m_oYourPopupMenu.add(m_oShareMenuItem);
    m_oYourPopupMenu.add(m_oHistoryMenuItem);
    tblYourFiles.addMouseListener(new cYourPopupTriggerListener());

    m_oOtherPopupMenu.add(m_oLikeMenuItem);
    m_oOtherPopupMenu.add(m_oAccessMenuItem);
    tblOtherFiles.addMouseListener(new cOtherPopupTriggerListener());
  }

  public void setUsername(String sUsername)
  {
    m_bLogin = true;
    m_sUsername = sUsername;
    txtUsername.setText(sUsername);
    setLogin();
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

    btnLogin = new javax.swing.JButton();
    lblPassword = new javax.swing.JLabel();
    txtUsername = new javax.swing.JTextField();
    lblUsername = new javax.swing.JLabel();
    btnRegister = new javax.swing.JButton();
    txtPassword = new javax.swing.JPasswordField();
    jTabbedPane1 = new javax.swing.JTabbedPane();
    pnlYourFiles = new javax.swing.JPanel();
    jScrollPane1 = new javax.swing.JScrollPane();
    tblYourFiles = new javax.swing.JTable();
    btnUploadFile = new javax.swing.JButton();
    pnlOtherFiles = new javax.swing.JPanel();
    jScrollPane2 = new javax.swing.JScrollPane();
    tblOtherFiles = new javax.swing.JTable();

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

    tblYourFiles.setModel(new javax.swing.table.DefaultTableModel(
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
    tblYourFiles.setEnabled(false);
    jScrollPane1.setViewportView(tblYourFiles);

    btnUploadFile.setText("Upload");
    btnUploadFile.setEnabled(false);
    btnUploadFile.addActionListener(new java.awt.event.ActionListener()
    {
      public void actionPerformed(java.awt.event.ActionEvent evt)
      {
        btnUploadFileActionPerformed(evt);
      }
    });

    javax.swing.GroupLayout pnlYourFilesLayout = new javax.swing.GroupLayout(pnlYourFiles);
    pnlYourFiles.setLayout(pnlYourFilesLayout);
    pnlYourFilesLayout.setHorizontalGroup(
      pnlYourFilesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
      .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 972, Short.MAX_VALUE)
      .addGroup(pnlYourFilesLayout.createSequentialGroup()
        .addContainerGap()
        .addComponent(btnUploadFile)
        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
    );
    pnlYourFilesLayout.setVerticalGroup(
      pnlYourFilesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
      .addGroup(pnlYourFilesLayout.createSequentialGroup()
        .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 368, Short.MAX_VALUE)
        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
        .addComponent(btnUploadFile)
        .addContainerGap())
    );

    jTabbedPane1.addTab("Your Files", pnlYourFiles);

    tblOtherFiles.setModel(new javax.swing.table.DefaultTableModel(
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
    tblOtherFiles.setEnabled(false);
    jScrollPane2.setViewportView(tblOtherFiles);

    javax.swing.GroupLayout pnlOtherFilesLayout = new javax.swing.GroupLayout(pnlOtherFiles);
    pnlOtherFiles.setLayout(pnlOtherFilesLayout);
    pnlOtherFilesLayout.setHorizontalGroup(
      pnlOtherFilesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
      .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 972, Short.MAX_VALUE)
    );
    pnlOtherFilesLayout.setVerticalGroup(
      pnlOtherFilesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
      .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 410, Short.MAX_VALUE)
    );

    jTabbedPane1.addTab("Other Files", pnlOtherFiles);

    javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
    this.setLayout(layout);
    layout.setHorizontalGroup(
      layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
      .addGroup(layout.createSequentialGroup()
        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
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
        .addComponent(btnRegister)
        .addContainerGap())
      .addComponent(jTabbedPane1)
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
        .addComponent(jTabbedPane1))
    );
  }// </editor-fold>//GEN-END:initComponents

  private void btnUploadFileActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_btnUploadFileActionPerformed
  {//GEN-HEADEREND:event_btnUploadFileActionPerformed
    JFileChooser fileChooser = new JFileChooser();
    int result = fileChooser.showOpenDialog(tblYourFiles);
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
      m_sDisplayName = sDisplayName;
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
    tblYourFiles.setEnabled(m_bLogin);
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
        ArrayList<cDatabaseFile> lsYourFiles = new ArrayList();
        if (m_bLogin)
        {
          lsYourFiles = oDatabase.getYourFiles(m_sUsername);
          lsYourFiles.addAll(oDatabase.getSharedFiles(m_sDisplayName));
        }
        
        int iYourRowCount = tblYourFiles.getRowCount();
        DefaultTableModel oYourModel = (DefaultTableModel) tblYourFiles.getModel();
        for (int i = 0; i < iYourRowCount; i++)
        {
          oYourModel.removeRow(0);
        }
        
        for (cDatabaseFile oFile : lsYourFiles)
        {
          // Append a row 
          oYourModel.addRow(new Object[]
          {
            oFile.m_sFileID, oFile.m_sFilename, oFile.m_sOwner
          });
        }
        
        ArrayList<cDatabaseFile> lsOtherFiles = new ArrayList();
        if (m_bLogin)
        {
          lsOtherFiles = oDatabase.getOtherFiles(m_sUsername);
        }
        int iOtherFiles = tblOtherFiles.getRowCount();
        DefaultTableModel oOtherModel = (DefaultTableModel) tblOtherFiles.getModel();
        for (int i = 0; i < iOtherFiles; i++)
        {
          oOtherModel.removeRow(0);
        }

        for (cDatabaseFile oFile : lsOtherFiles)
        {
          // Append a row 
          oOtherModel.addRow(new Object[]
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
  private javax.swing.JScrollPane jScrollPane2;
  private javax.swing.JTabbedPane jTabbedPane1;
  private javax.swing.JLabel lblPassword;
  private javax.swing.JLabel lblUsername;
  private javax.swing.JPanel pnlOtherFiles;
  private javax.swing.JPanel pnlYourFiles;
  private javax.swing.JTable tblOtherFiles;
  private javax.swing.JTable tblYourFiles;
  private javax.swing.JPasswordField txtPassword;
  private javax.swing.JTextField txtUsername;
  // End of variables declaration//GEN-END:variables

}
