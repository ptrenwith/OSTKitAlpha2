package org.digitalpassport.ui.panels;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Objects;
import java.util.Vector;
import javax.swing.JOptionPane;
import javax.swing.SpinnerNumberModel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import org.digitalpassport.OSTKitAlpha;
import static org.digitalpassport.api.IConstants.*;
import org.digitalpassport.api.commands.cTransactionManagement;
import org.digitalpassport.api.commands.cUserManagement;
import org.digitalpassport.api.commands.eCurrencyType;
import org.digitalpassport.api.commands.eTransactionKind;
import org.digitalpassport.deserialize.json.cData;
import org.digitalpassport.deserialize.json.cError;
import org.digitalpassport.deserialize.json.cErrorData;
import org.digitalpassport.deserialize.json.cResponse;
import org.digitalpassport.deserialize.json.transactiontypes.cTransaction;
import org.digitalpassport.deserialize.json.transactiontypes.cTransactionTypes;
import org.digitalpassport.deserialize.json.users.lists.cEconomyUser;
import org.digitalpassport.serialization.cSerializationFactory;
import org.digitalpassport.transactions.cStatusRenderer;
import org.digitalpassport.deserialize.json.transactiontypes.eStatus;
import org.digitalpassport.jdbc.cDatabaseHandler;
import org.digitalpassport.ui.cAirdropList;
import org.digitalpassport.ui.cTransactionList;

/**
 *
 * @author Philip M. Trenwith
 */
public class cTokenManagementPanel extends javax.swing.JPanel
{

  private SimpleDateFormat m_oSimpleDateFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss.SSSZ");
  private cSerializationFactory m_oSerializationFactory = new cSerializationFactory();
  private cAirdropList m_oAirdrops = new cAirdropList();
  private cTransactionList m_oTransactions = new cTransactionList();
  private cUserManagement m_oUserManagement = new cUserManagement();
  private cTransactionManagement m_oTransactionManagement = new cTransactionManagement();
  private cUserTableModel m_oUserTableModel;
  private cEconomyUser[] m_lsUsers = null;

  private int m_iMaxPageNumber = 2;
  private int iOriginalTransactionHash = -1;
  private File fAirdrops;
  private File fTransactions;

  class cUserTableModel extends DefaultTableModel
  {

    public cUserTableModel()
    {
      super();
    }

    @Override
    public boolean isCellEditable(int row, int column)
    {
      return getColumnName(column).equals("Name");
    }
  }

  /**
   * Creates new form cTokenManagementPanel
   */
  public cTokenManagementPanel()
  {
    initComponents();

    ArrayList<String> lsTransactions = cDatabaseHandler.instance().getTransactions();

    for (String sItem : lsTransactions)
    {
      cmbTransactionHistory.addItem(sItem);
    }

    spnPage.setValue(1);
    spnPage.addChangeListener(new ChangeListener()
    {
      @Override
      public void stateChanged(ChangeEvent e)
      {
        try
        {
          int iPage = Integer.parseInt(spnPage.getValue() + "");
          loadUsers();
        }
        catch (Exception ex)
        {
          System.err.println("Invalid page: " + ex.getMessage());
        }
      }
    });

    fAirdrops = new File("airdrops.ser");
    if (fAirdrops.exists())
    {
      m_oAirdrops = (cAirdropList) m_oSerializationFactory.deserialize(fAirdrops, false);
      ArrayList<String> lsPreviousAirdrops = m_oAirdrops.getlsAirdropUUIDs();
      for (String sAirdrop : lsPreviousAirdrops)
      {
        System.out.println("Previous airdrop: " + sAirdrop);
        cmbAirdrops.addItem(sAirdrop);
      }
    }

    fTransactions = new File("transactions.ser");
    if (fTransactions.exists())
    {
      m_oTransactions = (cTransactionList) m_oSerializationFactory.deserialize(fTransactions, false);
      ArrayList<String> lsPreviousTransactions = m_oTransactions.getTransactionUUIDs();
      for (String sTransaction : lsPreviousTransactions)
      {
        System.out.println("Previous transaction: " + sTransaction);
        cmbTransactionHistory.addItem(sTransaction);
      }
    }

    spnAirdropAmount.setModel(new SpinnerNumberModel(10, 1, Integer.MAX_VALUE, 1));

    m_oUserTableModel = new cUserTableModel();
    TableModel oUserTableModel = tblUsers.getModel();
    int iUserTableColumnCount = oUserTableModel.getColumnCount();
    for (int i = 0; i < iUserTableColumnCount; i++)
    {
      m_oUserTableModel.addColumn(oUserTableModel.getColumnName(i));
    }
    tblUsers.setModel(m_oUserTableModel);
    tblUsers.addPropertyChangeListener(new PropertyChangeListener()
    {
      @Override
      public void propertyChange(PropertyChangeEvent evt)
      {
        if ("tableCellEditor".equals(evt.getPropertyName()))
        {
          if (evt.getNewValue() == null)
          {
            int iRow = tblUsers.getSelectedRow();
            int iCol = tblUsers.getSelectedColumn();
            int iUUIDIndex = getUserTableColumnIndexByHeading("UUID");
            String sUUID = tblUsers.getValueAt(iRow, iUUIDIndex) + "";
            String sNewName = tblUsers.getValueAt(iRow, iCol) + "";
            System.out.println("User name changed: " + sUUID + " -> " + sNewName);
            cResponse oResponse = m_oUserManagement.editUser(sUUID, sNewName);

            if (oResponse != null && !oResponse.getsuccess())
            {
              JOptionPane.showMessageDialog(tblUsers, "Failed to edit user with UUID: "
                  + sUUID + "\nCode: " + oResponse.geterr().getcode()
                  + "\nMessage: " + oResponse.geterr().getmsg());
            }
          }
        }
      }
    });

    eTransactionKind[] oTransactionKinds = eTransactionKind.values();
    for (eTransactionKind oKind : oTransactionKinds)
    {
      cmbTransactionKind.addItem(oKind.name());
      cmbTransactionKind1.addItem(oKind.name());
    }

    eCurrencyType[] oCurrencyTypes = eCurrencyType.values();
    for (eCurrencyType oType : oCurrencyTypes)
    {
      cmbCurrencyType.addItem(oType.name());
      cmbCurrencyType1.addItem(oType.name());
    }

    spnCommissionPercentage.setModel(new SpinnerNumberModel(1, 0, 100, 1));
    spnCommissionPercentage1.setModel(new SpinnerNumberModel(1, 0, 100, 1));

    tblTransactions.setDefaultRenderer(eStatus.class, new cStatusRenderer());

    tblTransactions.addMouseListener(new MouseListener()
    {
      @Override
      public void mouseClicked(MouseEvent e)
      {
        btnExecute.setEnabled(true);

        lblTransactionId.setText("" + tblTransactions.getValueAt(tblTransactions.getSelectedRow(), getTransactionTableColumnIndexByHeading("ID")));
        txtTransactionName.setText("" + tblTransactions.getValueAt(tblTransactions.getSelectedRow(), getTransactionTableColumnIndexByHeading("Name")));
        cmbTransactionKind.setSelectedItem("" + tblTransactions.getValueAt(tblTransactions.getSelectedRow(), getTransactionTableColumnIndexByHeading("Kind")));
        cmbCurrencyType.setSelectedItem("" + tblTransactions.getValueAt(tblTransactions.getSelectedRow(), getTransactionTableColumnIndexByHeading("Currency Type")));
        txtCurrencyValue.setText("" + tblTransactions.getValueAt(tblTransactions.getSelectedRow(), getTransactionTableColumnIndexByHeading("Currency Value")));
        Object oCommissionPercentage = tblTransactions.getValueAt(tblTransactions.getSelectedRow(), getTransactionTableColumnIndexByHeading("Commission Percentage"));
        if (oCommissionPercentage != null)
        {
          spnCommissionPercentage.setValue(Double.parseDouble("" + oCommissionPercentage));
        }
        spnCommissionPercentage.setEnabled(eTransactionKind.valueOf("" + tblTransactions.getValueAt(tblTransactions.getSelectedRow(), getTransactionTableColumnIndexByHeading("Kind"))).equals(eTransactionKind.user_to_user));

        switch (eTransactionKind.valueOf("" + tblTransactions.getValueAt(tblTransactions.getSelectedRow(), getTransactionTableColumnIndexByHeading("Kind"))))
        {
          case user_to_user:
            cmbFromUser.setEnabled(true);
            cmbToUser.setEnabled(true);
            break;
          case user_to_company:
            cmbFromUser.setEnabled(true);
            cmbToUser.setEnabled(false);
            break;
          case company_to_user:
            cmbFromUser.setEnabled(false);
            cmbToUser.setEnabled(true);
            break;
          default:
            break;
        }

        iOriginalTransactionHash = transactionHashCode();
      }

      @Override
      public void mousePressed(MouseEvent e)
      {
      }

      @Override
      public void mouseReleased(MouseEvent e)
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
    });

    tblTokens.setValueAt("Client ID", 0, 0);
    tblTokens.setValueAt("Client Name", 1, 0);
    tblTokens.setValueAt("Symbol", 2, 0);
    tblTokens.setValueAt("Symbol Icon", 3, 0);
    tblTokens.setValueAt("Conversion Factor", 4, 0);
    tblTokens.setValueAt("Token ERC-20 Address", 5, 0);
    tblTokens.setValueAt("Airdrop Contract Address", 6, 0);
    tblTokens.setValueAt("Simple Stake Contract Address", 7, 0);

    btnListUsers.doClick();
    btnListTransactions.doClick();
  }

  public int transactionHashCode()
  {
    int hash = 5;
    hash = 29 * hash + Objects.hashCode(lblTransactionId.getText());
    hash = 29 * hash + Objects.hashCode(txtTransactionName.getText());
    hash = 29 * hash + Objects.hashCode(cmbTransactionKind.getSelectedItem() + "");
    hash = 29 * hash + Objects.hashCode(cmbCurrencyType.getSelectedItem() + "");
    hash = 29 * hash + Objects.hashCode(txtCurrencyValue.getText());
    hash = 29 * hash + Objects.hashCode(spnCommissionPercentage.getValue() + "");
    return hash;
  }

  public void terminate()
  {
    m_oSerializationFactory.serialize(m_oAirdrops, fAirdrops, false);
    m_oSerializationFactory.serialize(m_oTransactions, fTransactions, false);
  }

  /**
   * This method is called from within the constructor to initialize the form. WARNING: Do NOT modify this code. The
   * content of this method is always regenerated by the Form Editor.
   */
  @SuppressWarnings("unchecked")
  // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
  private void initComponents()
  {

    oMainTabPane = new javax.swing.JTabbedPane();
    oUsersTabScrollTab = new javax.swing.JScrollPane();
    oUsersPanel = new javax.swing.JPanel();
    jLabel3 = new javax.swing.JLabel();
    cmbFilter = new javax.swing.JComboBox<>();
    jLabel2 = new javax.swing.JLabel();
    cmbOrderBy = new javax.swing.JComboBox<>();
    cmbOrder = new javax.swing.JComboBox<>();
    jLabel1 = new javax.swing.JLabel();
    spnPage = new javax.swing.JSpinner();
    btnListUsers = new javax.swing.JButton();
    jScrollPane2 = new javax.swing.JScrollPane();
    tblUsers = new javax.swing.JTable();
    jLabel4 = new javax.swing.JLabel();
    txtName = new javax.swing.JTextField();
    btnCreateUser = new javax.swing.JButton();
    pnlAirdrops = new javax.swing.JPanel();
    lblAirdropToUsers = new javax.swing.JLabel();
    cmbAirdropTo = new javax.swing.JComboBox<>();
    lblAirdropAmount = new javax.swing.JLabel();
    spnAirdropAmount = new javax.swing.JSpinner();
    btnAirdrop = new javax.swing.JButton();
    lblAirdropUUIDs = new javax.swing.JLabel();
    cmbAirdrops = new javax.swing.JComboBox<>();
    btnAirdropStatus = new javax.swing.JButton();
    jScrollPane4 = new javax.swing.JScrollPane();
    tblAirdropStepsCompleted = new javax.swing.JTable();
    lblAirdropStatus = new javax.swing.JLabel();
    Tokens = new javax.swing.JPanel();
    jScrollPane5 = new javax.swing.JScrollPane();
    tblTokens = new javax.swing.JTable();
    jScrollPane1 = new javax.swing.JScrollPane();
    pnlTransactions = new javax.swing.JPanel();
    jTabbedPane1 = new javax.swing.JTabbedPane();
    pnlNewTransaction = new javax.swing.JPanel();
    lblTransactionName1 = new javax.swing.JLabel();
    lblKind1 = new javax.swing.JLabel();
    lblCurrencyType1 = new javax.swing.JLabel();
    lblCurrencyValue1 = new javax.swing.JLabel();
    lblCommissionPercentage1 = new javax.swing.JLabel();
    spnCommissionPercentage1 = new javax.swing.JSpinner();
    txtCurrencyValue1 = new javax.swing.JFormattedTextField();
    cmbCurrencyType1 = new javax.swing.JComboBox<>();
    cmbTransactionKind1 = new javax.swing.JComboBox<>();
    txtTransactionName1 = new javax.swing.JTextField();
    btnCreateTransaction = new javax.swing.JButton();
    pnlTransactionHistory = new javax.swing.JPanel();
    jLabel5 = new javax.swing.JLabel();
    cmbTransactionHistory = new javax.swing.JComboBox<>();
    btnTransactionStatus = new javax.swing.JButton();
    jScrollPane7 = new javax.swing.JScrollPane();
    tblTransactionStatus = new javax.swing.JTable();
    pnlExistingTransactions = new javax.swing.JPanel();
    btnListTransactions = new javax.swing.JButton();
    jScrollPane3 = new javax.swing.JScrollPane();
    tblTransactions = new javax.swing.JTable();
    lblTransactionName = new javax.swing.JLabel();
    txtTransactionName = new javax.swing.JTextField();
    btnSaveTransaction = new javax.swing.JButton();
    lblKind = new javax.swing.JLabel();
    lblCurrencyType = new javax.swing.JLabel();
    lblCurrencyValue = new javax.swing.JLabel();
    lblCommissionPercentage = new javax.swing.JLabel();
    cmbTransactionKind = new javax.swing.JComboBox<>();
    cmbCurrencyType = new javax.swing.JComboBox<>();
    txtCurrencyValue = new javax.swing.JFormattedTextField();
    spnCommissionPercentage = new javax.swing.JSpinner();
    lblTransactionIDLabel = new javax.swing.JLabel();
    lblFromUser = new javax.swing.JLabel();
    cmbFromUser = new javax.swing.JComboBox<>();
    lblToUser = new javax.swing.JLabel();
    cmbToUser = new javax.swing.JComboBox<>();
    btnExecute = new javax.swing.JButton();
    lblTransactionId = new javax.swing.JLabel();

    jLabel3.setText("Filter:");

    cmbFilter.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "all", "never_airdropped" }));
    cmbFilter.addActionListener(new java.awt.event.ActionListener()
    {
      public void actionPerformed(java.awt.event.ActionEvent evt)
      {
        cmbFilterActionPerformed(evt);
      }
    });

    jLabel2.setText("Order By:");

    cmbOrderBy.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "name", "creation_time" }));
    cmbOrderBy.addActionListener(new java.awt.event.ActionListener()
    {
      public void actionPerformed(java.awt.event.ActionEvent evt)
      {
        cmbOrderByActionPerformed(evt);
      }
    });

    cmbOrder.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "asc", "desc" }));
    cmbOrder.addActionListener(new java.awt.event.ActionListener()
    {
      public void actionPerformed(java.awt.event.ActionEvent evt)
      {
        cmbOrderActionPerformed(evt);
      }
    });

    jLabel1.setText("Page:");

    btnListUsers.setText("Get Users");
    btnListUsers.addActionListener(new java.awt.event.ActionListener()
    {
      public void actionPerformed(java.awt.event.ActionEvent evt)
      {
        btnListUsersActionPerformed(evt);
      }
    });

    tblUsers.setModel(new javax.swing.table.DefaultTableModel(
      new Object [][]
      {

      },
      new String []
      {
        "Name", "Tokens", "Airdropped Tokens", "UUID"
      }
    )
    {
      Class[] types = new Class []
      {
        java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class
      };
      boolean[] canEdit = new boolean []
      {
        false, false, false, false
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
    tblUsers.getTableHeader().setReorderingAllowed(false);
    jScrollPane2.setViewportView(tblUsers);

    jLabel4.setText("Name:");

    btnCreateUser.setText("Create User");
    btnCreateUser.addActionListener(new java.awt.event.ActionListener()
    {
      public void actionPerformed(java.awt.event.ActionEvent evt)
      {
        btnCreateUserActionPerformed(evt);
      }
    });

    javax.swing.GroupLayout oUsersPanelLayout = new javax.swing.GroupLayout(oUsersPanel);
    oUsersPanel.setLayout(oUsersPanelLayout);
    oUsersPanelLayout.setHorizontalGroup(
      oUsersPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
      .addGroup(oUsersPanelLayout.createSequentialGroup()
        .addGroup(oUsersPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
          .addComponent(jScrollPane2)
          .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, oUsersPanelLayout.createSequentialGroup()
            .addGap(0, 306, Short.MAX_VALUE)
            .addComponent(jLabel3)
            .addGap(18, 18, 18)
            .addComponent(cmbFilter, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
            .addGap(10, 10, 10)
            .addComponent(jLabel2)
            .addGap(18, 18, 18)
            .addComponent(cmbOrderBy, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
            .addGap(6, 6, 6)
            .addComponent(cmbOrder, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
            .addGap(18, 18, 18)
            .addComponent(jLabel1)
            .addGap(18, 18, 18)
            .addComponent(spnPage, javax.swing.GroupLayout.PREFERRED_SIZE, 48, javax.swing.GroupLayout.PREFERRED_SIZE)
            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
            .addComponent(btnListUsers, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE))
          .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, oUsersPanelLayout.createSequentialGroup()
            .addComponent(jLabel4)
            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
            .addComponent(txtName)
            .addGap(18, 18, 18)
            .addComponent(btnCreateUser, javax.swing.GroupLayout.PREFERRED_SIZE, 110, javax.swing.GroupLayout.PREFERRED_SIZE)))
        .addContainerGap())
    );
    oUsersPanelLayout.setVerticalGroup(
      oUsersPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
      .addGroup(oUsersPanelLayout.createSequentialGroup()
        .addGap(11, 11, 11)
        .addGroup(oUsersPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
          .addGroup(oUsersPanelLayout.createSequentialGroup()
            .addGap(4, 4, 4)
            .addComponent(jLabel3))
          .addGroup(oUsersPanelLayout.createSequentialGroup()
            .addGap(1, 1, 1)
            .addComponent(cmbFilter, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
          .addGroup(oUsersPanelLayout.createSequentialGroup()
            .addGap(4, 4, 4)
            .addComponent(jLabel2))
          .addGroup(oUsersPanelLayout.createSequentialGroup()
            .addGap(1, 1, 1)
            .addComponent(cmbOrderBy, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
          .addGroup(oUsersPanelLayout.createSequentialGroup()
            .addGap(1, 1, 1)
            .addComponent(cmbOrder, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
          .addGroup(oUsersPanelLayout.createSequentialGroup()
            .addGap(4, 4, 4)
            .addComponent(jLabel1))
          .addGroup(oUsersPanelLayout.createSequentialGroup()
            .addGap(1, 1, 1)
            .addComponent(spnPage, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
          .addComponent(btnListUsers))
        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
        .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 435, javax.swing.GroupLayout.PREFERRED_SIZE)
        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
        .addGroup(oUsersPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
          .addComponent(txtName, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
          .addComponent(btnCreateUser)
          .addComponent(jLabel4))
        .addContainerGap())
    );

    oUsersTabScrollTab.setViewportView(oUsersPanel);

    oMainTabPane.addTab("Users", oUsersTabScrollTab);

    lblAirdropToUsers.setText("Airdrop tokens to users:");

    cmbAirdropTo.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "all", "never airdropped" }));

    lblAirdropAmount.setText("Amount:");

    btnAirdrop.setText("Airdrop");
    btnAirdrop.addActionListener(new java.awt.event.ActionListener()
    {
      public void actionPerformed(java.awt.event.ActionEvent evt)
      {
        btnAirdropActionPerformed(evt);
      }
    });

    lblAirdropUUIDs.setText("Previous Airdrops:");
    lblAirdropUUIDs.setToolTipText("");

    btnAirdropStatus.setText("Status");
    btnAirdropStatus.setEnabled(false);
    btnAirdropStatus.addActionListener(new java.awt.event.ActionListener()
    {
      public void actionPerformed(java.awt.event.ActionEvent evt)
      {
        btnAirdropStatusActionPerformed(evt);
      }
    });

    tblAirdropStepsCompleted.setModel(new javax.swing.table.DefaultTableModel(
      new Object [][]
      {
        {null, null, null, null}
      },
      new String []
      {
        "Users Identified", "Tokens Transfered", "Contract Approved", "Allocation Done"
      }
    )
    {
      Class[] types = new Class []
      {
        java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class
      };
      boolean[] canEdit = new boolean []
      {
        false, false, false, false
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
    tblAirdropStepsCompleted.setRowSelectionAllowed(false);
    tblAirdropStepsCompleted.getTableHeader().setReorderingAllowed(false);
    jScrollPane4.setViewportView(tblAirdropStepsCompleted);

    lblAirdropStatus.setText("Status:");

    javax.swing.GroupLayout pnlAirdropsLayout = new javax.swing.GroupLayout(pnlAirdrops);
    pnlAirdrops.setLayout(pnlAirdropsLayout);
    pnlAirdropsLayout.setHorizontalGroup(
      pnlAirdropsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
      .addGroup(pnlAirdropsLayout.createSequentialGroup()
        .addContainerGap()
        .addGroup(pnlAirdropsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
          .addComponent(lblAirdropStatus, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
          .addGroup(pnlAirdropsLayout.createSequentialGroup()
            .addGroup(pnlAirdropsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
              .addComponent(lblAirdropUUIDs, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
              .addComponent(lblAirdropToUsers))
            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
            .addGroup(pnlAirdropsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
              .addComponent(cmbAirdropTo, 0, 1, Short.MAX_VALUE)
              .addComponent(cmbAirdrops, 0, 1, Short.MAX_VALUE))
            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
            .addComponent(lblAirdropAmount)
            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
            .addComponent(spnAirdropAmount, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
            .addGroup(pnlAirdropsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
              .addComponent(btnAirdropStatus, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
              .addComponent(btnAirdrop, javax.swing.GroupLayout.PREFERRED_SIZE, 112, javax.swing.GroupLayout.PREFERRED_SIZE)))
          .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pnlAirdropsLayout.createSequentialGroup()
            .addComponent(jScrollPane4, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 1, javax.swing.GroupLayout.PREFERRED_SIZE)))
        .addContainerGap())
    );
    pnlAirdropsLayout.setVerticalGroup(
      pnlAirdropsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
      .addGroup(pnlAirdropsLayout.createSequentialGroup()
        .addContainerGap()
        .addGroup(pnlAirdropsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
          .addComponent(lblAirdropToUsers)
          .addComponent(cmbAirdropTo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
          .addComponent(btnAirdrop)
          .addComponent(spnAirdropAmount, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
          .addComponent(lblAirdropAmount))
        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
        .addGroup(pnlAirdropsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
          .addComponent(lblAirdropUUIDs)
          .addComponent(cmbAirdrops, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
          .addComponent(btnAirdropStatus))
        .addGap(21, 21, 21)
        .addComponent(lblAirdropStatus)
        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
        .addComponent(jScrollPane4, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE)
        .addContainerGap())
    );

    oMainTabPane.addTab("Airdrops", pnlAirdrops);

    tblTokens.setModel(new javax.swing.table.DefaultTableModel(
      new Object [][]
      {
        {null, null},
        {null, null},
        {null, null},
        {null, null},
        {null, null},
        {null, null},
        {null, null},
        {null, null}
      },
      new String []
      {
        "Key", "Value"
      }
    )
    {
      Class[] types = new Class []
      {
        java.lang.String.class, java.lang.String.class
      };
      boolean[] canEdit = new boolean []
      {
        false, false
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
    jScrollPane5.setViewportView(tblTokens);

    javax.swing.GroupLayout TokensLayout = new javax.swing.GroupLayout(Tokens);
    Tokens.setLayout(TokensLayout);
    TokensLayout.setHorizontalGroup(
      TokensLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
      .addComponent(jScrollPane5, javax.swing.GroupLayout.DEFAULT_SIZE, 947, Short.MAX_VALUE)
    );
    TokensLayout.setVerticalGroup(
      TokensLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
      .addGroup(TokensLayout.createSequentialGroup()
        .addContainerGap()
        .addComponent(jScrollPane5, javax.swing.GroupLayout.PREFERRED_SIZE, 158, javax.swing.GroupLayout.PREFERRED_SIZE))
    );

    oMainTabPane.addTab("Tokens", Tokens);

    lblTransactionName1.setText("Name: ");

    lblKind1.setText("Kind:");

    lblCurrencyType1.setText("Currency Type:");

    lblCurrencyValue1.setText("Currency Value:");

    lblCommissionPercentage1.setText("Commission Percentage:");

    txtCurrencyValue1.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter(new java.text.DecimalFormat("###.#####"))));

    btnCreateTransaction.setText("CreateTransaction");
    btnCreateTransaction.addActionListener(new java.awt.event.ActionListener()
    {
      public void actionPerformed(java.awt.event.ActionEvent evt)
      {
        btnCreateTransactionActionPerformed(evt);
      }
    });

    javax.swing.GroupLayout pnlNewTransactionLayout = new javax.swing.GroupLayout(pnlNewTransaction);
    pnlNewTransaction.setLayout(pnlNewTransactionLayout);
    pnlNewTransactionLayout.setHorizontalGroup(
      pnlNewTransactionLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
      .addGroup(pnlNewTransactionLayout.createSequentialGroup()
        .addGroup(pnlNewTransactionLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
          .addComponent(lblCommissionPercentage1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
          .addComponent(lblCurrencyValue1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
          .addComponent(lblCurrencyType1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
          .addComponent(lblKind1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
          .addComponent(lblTransactionName1, javax.swing.GroupLayout.PREFERRED_SIZE, 118, javax.swing.GroupLayout.PREFERRED_SIZE))
        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
        .addGroup(pnlNewTransactionLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
          .addComponent(txtCurrencyValue1)
          .addComponent(cmbCurrencyType1, 0, 657, Short.MAX_VALUE)
          .addComponent(cmbTransactionKind1, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
          .addComponent(txtTransactionName1, javax.swing.GroupLayout.Alignment.TRAILING)
          .addComponent(spnCommissionPercentage1, javax.swing.GroupLayout.Alignment.TRAILING))
        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
        .addComponent(btnCreateTransaction, javax.swing.GroupLayout.PREFERRED_SIZE, 140, javax.swing.GroupLayout.PREFERRED_SIZE))
    );
    pnlNewTransactionLayout.setVerticalGroup(
      pnlNewTransactionLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
      .addGroup(pnlNewTransactionLayout.createSequentialGroup()
        .addContainerGap()
        .addGroup(pnlNewTransactionLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
          .addComponent(btnCreateTransaction)
          .addComponent(txtTransactionName1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
          .addComponent(lblTransactionName1))
        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
        .addGroup(pnlNewTransactionLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
          .addComponent(lblKind1)
          .addComponent(cmbTransactionKind1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
        .addGroup(pnlNewTransactionLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
          .addComponent(lblCurrencyType1)
          .addComponent(cmbCurrencyType1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
        .addGroup(pnlNewTransactionLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
          .addComponent(lblCurrencyValue1)
          .addComponent(txtCurrencyValue1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
        .addGroup(pnlNewTransactionLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
          .addComponent(lblCommissionPercentage1)
          .addComponent(spnCommissionPercentage1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        .addContainerGap())
    );

    jTabbedPane1.addTab("New", pnlNewTransaction);

    jLabel5.setText("Transaction:");

    btnTransactionStatus.setLabel("Transaction Status");
    btnTransactionStatus.addActionListener(new java.awt.event.ActionListener()
    {
      public void actionPerformed(java.awt.event.ActionEvent evt)
      {
        btnTransactionStatusActionPerformed(evt);
      }
    });

    tblTransactionStatus.setModel(new javax.swing.table.DefaultTableModel(
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
    tblTransactionStatus.getTableHeader().setReorderingAllowed(false);
    jScrollPane7.setViewportView(tblTransactionStatus);

    javax.swing.GroupLayout pnlTransactionHistoryLayout = new javax.swing.GroupLayout(pnlTransactionHistory);
    pnlTransactionHistory.setLayout(pnlTransactionHistoryLayout);
    pnlTransactionHistoryLayout.setHorizontalGroup(
      pnlTransactionHistoryLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
      .addGroup(pnlTransactionHistoryLayout.createSequentialGroup()
        .addContainerGap()
        .addGroup(pnlTransactionHistoryLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
          .addComponent(jScrollPane7, javax.swing.GroupLayout.DEFAULT_SIZE, 915, Short.MAX_VALUE)
          .addGroup(pnlTransactionHistoryLayout.createSequentialGroup()
            .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 95, javax.swing.GroupLayout.PREFERRED_SIZE)
            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
            .addComponent(cmbTransactionHistory, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
            .addComponent(btnTransactionStatus)))
        .addContainerGap())
    );
    pnlTransactionHistoryLayout.setVerticalGroup(
      pnlTransactionHistoryLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
      .addGroup(pnlTransactionHistoryLayout.createSequentialGroup()
        .addContainerGap()
        .addGroup(pnlTransactionHistoryLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
          .addComponent(jLabel5)
          .addComponent(cmbTransactionHistory, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
          .addComponent(btnTransactionStatus))
        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
        .addComponent(jScrollPane7, javax.swing.GroupLayout.DEFAULT_SIZE, 585, Short.MAX_VALUE))
    );

    jTabbedPane1.addTab("History", pnlTransactionHistory);

    btnListTransactions.setText("Refresh Transactions");
    btnListTransactions.setToolTipText("");
    btnListTransactions.addActionListener(new java.awt.event.ActionListener()
    {
      public void actionPerformed(java.awt.event.ActionEvent evt)
      {
        btnListTransactionsActionPerformed(evt);
      }
    });

    tblTransactions.setModel(new javax.swing.table.DefaultTableModel(
      new Object [][]
      {

      },
      new String []
      {
        "ID", "Client Transaction ID", "Name", "Kind", "Currency Type", "Currency Value", "Commission Percentage", "Status"
      }
    )
    {
      Class[] types = new Class []
      {
        java.lang.String.class, java.lang.String.class, java.lang.String.class, eTransactionKind.class, eCurrencyType.class, java.lang.String.class, java.lang.String.class, eStatus.class
      };
      boolean[] canEdit = new boolean []
      {
        false, false, false, false, false, false, false, false
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
    tblTransactions.getTableHeader().setReorderingAllowed(false);
    jScrollPane3.setViewportView(tblTransactions);

    lblTransactionName.setText("Name: ");

    btnSaveTransaction.setText("Save Changes");
    btnSaveTransaction.addActionListener(new java.awt.event.ActionListener()
    {
      public void actionPerformed(java.awt.event.ActionEvent evt)
      {
        btnSaveTransactionActionPerformed(evt);
      }
    });

    lblKind.setText("Kind:");

    lblCurrencyType.setText("Currency Type:");

    lblCurrencyValue.setText("Currency Value:");

    lblCommissionPercentage.setText("Commission Percentage:");

    txtCurrencyValue.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter(new java.text.DecimalFormat("###.#####"))));

    lblTransactionIDLabel.setText("ID:");

    lblFromUser.setText("From User:");

    cmbFromUser.setEnabled(false);
    cmbFromUser.addActionListener(new java.awt.event.ActionListener()
    {
      public void actionPerformed(java.awt.event.ActionEvent evt)
      {
        cmbFromUserActionPerformed(evt);
      }
    });

    lblToUser.setText("To User:");

    cmbToUser.setEnabled(false);

    btnExecute.setText("Execute Transaction");
    btnExecute.setEnabled(false);
    btnExecute.addActionListener(new java.awt.event.ActionListener()
    {
      public void actionPerformed(java.awt.event.ActionEvent evt)
      {
        btnExecuteActionPerformed(evt);
      }
    });

    lblTransactionId.setToolTipText("");

    javax.swing.GroupLayout pnlExistingTransactionsLayout = new javax.swing.GroupLayout(pnlExistingTransactions);
    pnlExistingTransactions.setLayout(pnlExistingTransactionsLayout);
    pnlExistingTransactionsLayout.setHorizontalGroup(
      pnlExistingTransactionsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
      .addGroup(pnlExistingTransactionsLayout.createSequentialGroup()
        .addContainerGap()
        .addGroup(pnlExistingTransactionsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
          .addComponent(jScrollPane3, javax.swing.GroupLayout.DEFAULT_SIZE, 915, Short.MAX_VALUE)
          .addGroup(pnlExistingTransactionsLayout.createSequentialGroup()
            .addGroup(pnlExistingTransactionsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
              .addComponent(lblToUser, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
              .addComponent(lblFromUser, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
              .addComponent(lblCommissionPercentage, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
              .addComponent(lblCurrencyValue, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
              .addComponent(lblCurrencyType, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
              .addComponent(lblKind, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
              .addComponent(lblTransactionName, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
              .addComponent(lblTransactionIDLabel, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
            .addGroup(pnlExistingTransactionsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
              .addComponent(txtTransactionName, javax.swing.GroupLayout.Alignment.TRAILING)
              .addComponent(cmbTransactionKind, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
              .addComponent(cmbCurrencyType, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
              .addComponent(txtCurrencyValue)
              .addComponent(spnCommissionPercentage, javax.swing.GroupLayout.Alignment.TRAILING)
              .addComponent(cmbFromUser, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
              .addComponent(cmbToUser, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
              .addComponent(lblTransactionId, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
            .addGroup(pnlExistingTransactionsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
              .addComponent(btnListTransactions, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
              .addComponent(btnSaveTransaction, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
              .addComponent(btnExecute, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
        .addContainerGap())
    );
    pnlExistingTransactionsLayout.setVerticalGroup(
      pnlExistingTransactionsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
      .addGroup(pnlExistingTransactionsLayout.createSequentialGroup()
        .addContainerGap()
        .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 241, javax.swing.GroupLayout.PREFERRED_SIZE)
        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
        .addGroup(pnlExistingTransactionsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
          .addGroup(pnlExistingTransactionsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
            .addComponent(btnListTransactions)
            .addComponent(lblTransactionIDLabel))
          .addComponent(lblTransactionId, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        .addGap(12, 12, 12)
        .addGroup(pnlExistingTransactionsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
          .addComponent(btnSaveTransaction)
          .addComponent(txtTransactionName, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
          .addComponent(lblTransactionName))
        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
        .addGroup(pnlExistingTransactionsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
          .addComponent(cmbTransactionKind, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
          .addComponent(lblKind))
        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
        .addGroup(pnlExistingTransactionsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
          .addComponent(cmbCurrencyType, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
          .addComponent(lblCurrencyType))
        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
        .addGroup(pnlExistingTransactionsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
          .addComponent(txtCurrencyValue, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
          .addComponent(lblCurrencyValue))
        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
        .addGroup(pnlExistingTransactionsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
          .addComponent(spnCommissionPercentage, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
          .addComponent(lblCommissionPercentage))
        .addGap(18, 18, 18)
        .addGroup(pnlExistingTransactionsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
          .addComponent(cmbFromUser, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
          .addComponent(lblFromUser)
          .addComponent(btnExecute))
        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
        .addGroup(pnlExistingTransactionsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
          .addComponent(cmbToUser, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
          .addComponent(lblToUser))
        .addContainerGap())
    );

    jTabbedPane1.addTab("Existing", pnlExistingTransactions);

    javax.swing.GroupLayout pnlTransactionsLayout = new javax.swing.GroupLayout(pnlTransactions);
    pnlTransactions.setLayout(pnlTransactionsLayout);
    pnlTransactionsLayout.setHorizontalGroup(
      pnlTransactionsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
      .addComponent(jTabbedPane1)
    );
    pnlTransactionsLayout.setVerticalGroup(
      pnlTransactionsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
      .addComponent(jTabbedPane1)
    );

    jScrollPane1.setViewportView(pnlTransactions);

    oMainTabPane.addTab("Transactions", jScrollPane1);

    javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
    this.setLayout(layout);
    layout.setHorizontalGroup(
      layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
      .addComponent(oMainTabPane)
    );
    layout.setVerticalGroup(
      layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
      .addComponent(oMainTabPane)
    );
  }// </editor-fold>//GEN-END:initComponents

  private void cmbFilterActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_cmbFilterActionPerformed
  {//GEN-HEADEREND:event_cmbFilterActionPerformed
    spnPage.setValue(1);
  }//GEN-LAST:event_cmbFilterActionPerformed

  private void cmbOrderByActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_cmbOrderByActionPerformed
  {//GEN-HEADEREND:event_cmbOrderByActionPerformed
    spnPage.setValue(1);
  }//GEN-LAST:event_cmbOrderByActionPerformed

  private void cmbOrderActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_cmbOrderActionPerformed
  {//GEN-HEADEREND:event_cmbOrderActionPerformed
    spnPage.setValue(1);
  }//GEN-LAST:event_cmbOrderActionPerformed

  private void btnListUsersActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_btnListUsersActionPerformed
  {//GEN-HEADEREND:event_btnListUsersActionPerformed
    loadUsers();
  }//GEN-LAST:event_btnListUsersActionPerformed

  private void btnCreateUserActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_btnCreateUserActionPerformed
  {//GEN-HEADEREND:event_btnCreateUserActionPerformed
    btnCreateUser.setText("Please wait...");
    btnCreateUser.setEnabled(false);
    new Thread(new Runnable()
    {
      @Override
      public void run()
      {
        cResponse oResponse = m_oUserManagement.createUser(txtName.getText());

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
          loadUsers();
        }

        btnCreateUser.setText("Get Users");
        btnCreateUser.setEnabled(true);
      }
    }).start();
  }//GEN-LAST:event_btnCreateUserActionPerformed

  private void btnAirdropActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_btnAirdropActionPerformed
  {//GEN-HEADEREND:event_btnAirdropActionPerformed
    new Thread(new Runnable()
    {
      @Override
      public void run()
      {
        String sAirdropTo = (cmbAirdropTo.getSelectedItem() + "").replace(" ", "_");
        cResponse oResponse = m_oUserManagement.sendAirdropTo(sAirdropTo, Integer.parseInt(spnAirdropAmount.getValue() + ""));
        if (oResponse.getsuccess())
        {
          String sAirdrop_uuid = oResponse.getdata().getairdrop_uuid();
          m_oAirdrops.addAirdropUuid(sAirdrop_uuid);
          loadUsers();
        }
        else
        {
          showError(oResponse.geterr(), "Failed to send airdrop");
        }
      }
    }).start();
  }//GEN-LAST:event_btnAirdropActionPerformed

  private void btnAirdropStatusActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_btnAirdropStatusActionPerformed
  {//GEN-HEADEREND:event_btnAirdropStatusActionPerformed
    new Thread(new Runnable()
    {
      @Override
      public void run()
      {
        String sAirdropUUID = cmbAirdrops.getSelectedItem() + "";
        if (sAirdropUUID != null)
        {
          cResponse oResponse = m_oUserManagement.airdropStatus(sAirdropUUID);
          if (oResponse.getsuccess())
          {
            String airdrop_uuid = oResponse.getdata().getairdrop_uuid();
            String current_status = oResponse.getdata().getcurrent_status();
            String[] steps_complete = oResponse.getdata().getsteps_complete();

            lblAirdropStatus.setText("Status: " + current_status);

            tblAirdropStepsCompleted.setValueAt("NO", 0, 0);
            tblAirdropStepsCompleted.setValueAt("NO", 0, 1);
            tblAirdropStepsCompleted.setValueAt("NO", 0, 2);
            tblAirdropStepsCompleted.setValueAt("NO", 0, 3);
            for (String step : steps_complete)
            {
              if (step.equals(g_sPARAM_AIRDROP_USERS_IDENTIFIED))
              {
                tblAirdropStepsCompleted.setValueAt("YES", 0, 0);
              }
              else if (step.equals(g_sPARAM_AIRDROP_TOKENS_TRANSFERED))
              {
                tblAirdropStepsCompleted.setValueAt("YES", 0, 1);
              }
              else if (step.equals(g_sPARAM_AIRDROP_CONTRACT_APPORVED))
              {
                tblAirdropStepsCompleted.setValueAt("YES", 0, 2);
              }
              else if (step.equals(g_sPARAM_AIRDROP_ALOC_DONE))
              {
                tblAirdropStepsCompleted.setValueAt("YES", 0, 3);
              }
            }
          }
          else
          {
            showError(oResponse.geterr(), "Airdrop Status");
          }
        }
      }
    }).start();
  }//GEN-LAST:event_btnAirdropStatusActionPerformed

  private void btnCreateTransactionActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_btnCreateTransactionActionPerformed
  {//GEN-HEADEREND:event_btnCreateTransactionActionPerformed
    new Thread(new Runnable()
    {
      @Override
      public void run()
      {
        btnCreateTransaction.setEnabled(false);
        btnCreateTransaction.setText("Please wait...");
        //        int iNewTransactionHash = transactionHashCode();
        //        if (iOriginalTransactionHash != iNewTransactionHash)
        {
          String sTransactionName = txtTransactionName1.getText();
          eTransactionKind oTransactionKind = eTransactionKind.valueOf("" + cmbTransactionKind1.getSelectedItem());
          eCurrencyType oCurrencyType = eCurrencyType.valueOf("" + cmbCurrencyType1.getSelectedItem());
          String sCurrencyValue = txtCurrencyValue1.getText();
          String sCommissionPercentage = "" + spnCommissionPercentage1.getValue();

          cResponse oResponse = m_oTransactionManagement.createTransaction(sTransactionName, oTransactionKind, oCurrencyType,
              Float.parseFloat(sCurrencyValue), Float.parseFloat(sCommissionPercentage));
          if (oResponse.getsuccess())
          {
            btnListTransactions.doClick();
          }
          else
          {
            showError(oResponse.geterr(), "Transaction Error");
          }
        }
        btnCreateTransaction.setEnabled(true);
        btnCreateTransaction.setText("Save Transaction");
      }
    }).start();
  }//GEN-LAST:event_btnCreateTransactionActionPerformed

  private void btnTransactionStatusActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_btnTransactionStatusActionPerformed
  {//GEN-HEADEREND:event_btnTransactionStatusActionPerformed
    btnListUsers.setText("Please wait...");
    btnListUsers.setEnabled(false);
    new Thread(new Runnable()
    {
      @Override
      public void run()
      {
        String id = cmbTransactionHistory.getSelectedItem() + "";
        cResponse oResponse = m_oTransactionManagement.getTransactionStatus(id);

        if (oResponse.getsuccess())
        {
          cTransactionTypes transaction_type = oResponse.getdata().gettransaction_types()[0];
          cEconomyUser fromUser = oResponse.getdata().geteconomy_users()[0];
          cEconomyUser toUser = oResponse.getdata().geteconomy_users()[1];
          DefaultTableModel oTransactionModel = (DefaultTableModel) tblTransactionStatus.getModel();

          Vector<Object> vRow = new Vector<Object>();
          int iRowNumber = oTransactionModel.getRowCount();
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
        else
        {
          showError(oResponse.geterr(), "Transaction Status");
        }
        btnListUsers.setText("Transaction Status");
        btnListUsers.setEnabled(true);
      }
    }).start();
  }//GEN-LAST:event_btnTransactionStatusActionPerformed

  private void btnListTransactionsActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_btnListTransactionsActionPerformed
  {//GEN-HEADEREND:event_btnListTransactionsActionPerformed
    btnListTransactions.setText("Please wait...");
    btnListTransactions.setEnabled(false);
    new Thread(new Runnable()
    {
      @Override
      public void run()
      {
        clearTransactionTable();

        cResponse oResponse = m_oTransactionManagement.listTransactions();
        if (oResponse.getsuccess())
        {
          cTransactionTypes[] lsTransaction_types = oResponse.getdata().gettransaction_types();
          DefaultTableModel oTransactionModel = (DefaultTableModel) tblTransactions.getModel();

          for (cTransactionTypes oTransaction : lsTransaction_types)
          {
            cTransactionManagement.m_oTransactions.put(oTransaction.getname(), oTransaction);
            String sStatus = oTransaction.getstatus();
            Vector<Object> vRow = new Vector<Object>();
            int iRowNumber = oTransactionModel.getRowCount();
            oTransactionModel.addRow(vRow);
            oTransactionModel.setValueAt(oTransaction.getid(), iRowNumber, getTransactionTableColumnIndexByHeading("ID"));
            oTransactionModel.setValueAt(oTransaction.getclient_transaction_id(), iRowNumber, getTransactionTableColumnIndexByHeading("Client Transaction ID"));
            oTransactionModel.setValueAt(oTransaction.getname(), iRowNumber, getTransactionTableColumnIndexByHeading("Name"));
            oTransactionModel.setValueAt(oTransaction.getkind(), iRowNumber, getTransactionTableColumnIndexByHeading("Kind"));
            oTransactionModel.setValueAt(oTransaction.getcurrency_type(), iRowNumber, getTransactionTableColumnIndexByHeading("Currency Type"));
            oTransactionModel.setValueAt(oTransaction.getcurrency_value(), iRowNumber, getTransactionTableColumnIndexByHeading("Currency Value"));
            oTransactionModel.setValueAt(oTransaction.getcommission_percent(), iRowNumber, getTransactionTableColumnIndexByHeading("Commission Percentage"));
            if (sStatus != null)
            {
              oTransactionModel.setValueAt(eStatus.valueOf(sStatus), iRowNumber, getTransactionTableColumnIndexByHeading("Status"));
            }
          }

          tblTokens.setValueAt(oResponse.getdata().getclient_tokens().getclient_id(), 0, 1);
          tblTokens.setValueAt(oResponse.getdata().getclient_tokens().getname(), 1, 1);
          tblTokens.setValueAt(oResponse.getdata().getclient_tokens().getsymbol(), 2, 1);
          tblTokens.setValueAt(oResponse.getdata().getclient_tokens().getsymbol_icon(), 3, 1);
          tblTokens.setValueAt(oResponse.getdata().getclient_tokens().getconversion_factor(), 4, 1);
          tblTokens.setValueAt(oResponse.getdata().getclient_tokens().gettoken_erc20_address(), 5, 1);
          tblTokens.setValueAt(oResponse.getdata().getclient_tokens().getairdrop_contract_addr(), 6, 1);
          tblTokens.setValueAt(oResponse.getdata().getclient_tokens().getsimple_stake_contract_addr(), 7, 1);
        }
        else
        {
          showError(oResponse.geterr(), "List Transactions");
        }
        btnListTransactions.setText("List Transactions");
        btnListTransactions.setEnabled(true);
      }
    }).start();
  }//GEN-LAST:event_btnListTransactionsActionPerformed

  private void btnSaveTransactionActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_btnSaveTransactionActionPerformed
  {//GEN-HEADEREND:event_btnSaveTransactionActionPerformed
    new Thread(new Runnable()
    {
      @Override
      public void run()
      {
        btnSaveTransaction.setEnabled(false);
        btnSaveTransaction.setText("Please wait...");
        int iNewTransactionHash = transactionHashCode();
        if (iOriginalTransactionHash != iNewTransactionHash)
        {
          String sTransactionId = lblTransactionId.getText();
          String sTransactionName = txtTransactionName.getText();
          eTransactionKind oTransactionKind = eTransactionKind.valueOf("" + cmbTransactionKind.getSelectedItem());
          eCurrencyType oCurrencyType = eCurrencyType.valueOf("" + cmbCurrencyType.getSelectedItem());
          String sCurrencyValue = txtCurrencyValue.getText();
          String sCommissionPercentage = "" + spnCommissionPercentage.getValue();

          cResponse oResponse = m_oTransactionManagement.editTransaction_sandbox(sTransactionId, sTransactionName, oTransactionKind, oCurrencyType,
              Float.parseFloat(sCurrencyValue), Float.parseFloat(sCommissionPercentage));
          if (oResponse.getsuccess())
          {
            btnListTransactions.doClick();
          }
          else
          {
            showError(oResponse.geterr(), "Transaction Error");
          }
        }
        btnSaveTransaction.setEnabled(true);
        btnSaveTransaction.setText("Save Transaction");
      }
    }).start();
  }//GEN-LAST:event_btnSaveTransactionActionPerformed

  private void cmbFromUserActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_cmbFromUserActionPerformed
  {//GEN-HEADEREND:event_cmbFromUserActionPerformed
    cmbToUser.removeAllItems();
    for (cEconomyUser oUser : m_lsUsers)
    {
      if (!(cmbFromUser.getSelectedItem() + "").equals(oUser.getName() + " - " + oUser.getUuid()))
      {
        cmbToUser.addItem(oUser.getName() + " - " + oUser.getUuid());
      }
    }
  }//GEN-LAST:event_cmbFromUserActionPerformed

  private void btnExecuteActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_btnExecuteActionPerformed
  {//GEN-HEADEREND:event_btnExecuteActionPerformed
    btnExecute.setText("Please wait...");
    btnExecute.setEnabled(false);
    new Thread(new Runnable()
    {
      @Override
      public void run()
      {
        String sFromUser = cmbFromUser.getSelectedItem() + "";
        String sToUser = cmbToUser.getSelectedItem() + "";

        String sFromUserUuid = sFromUser.substring(sFromUser.indexOf(" - ") + 3);
        String sToUserUuid = sToUser.substring(sToUser.indexOf(" - ") + 3);

        String sName = tblTransactions.getValueAt(tblTransactions.getSelectedRow(), getTransactionTableColumnIndexByHeading("Name")) + "";
        cResponse oResponse = null;
        // company address 0x2abf49729f033cd72b5a875bad81dc9b305f06de
        // company uuid 1ec0b428-1b27-4218-95ae-b116f14b0450
        switch (eTransactionKind.valueOf("" + tblTransactions.getValueAt(tblTransactions.getSelectedRow(), getTransactionTableColumnIndexByHeading("Kind"))))
        {
          case user_to_user:
            oResponse = m_oTransactionManagement.executeTransaction(sFromUserUuid, sToUserUuid, sName);
            break;
          case user_to_company:
            oResponse = m_oTransactionManagement.executeTransaction(sFromUserUuid, "1ec0b428-1b27-4218-95ae-b116f14b0450", sName);
            break;
          case company_to_user:
            oResponse = m_oTransactionManagement.executeTransaction("1ec0b428-1b27-4218-95ae-b116f14b0450", sToUserUuid, sName);
            break;
          default:
            break;
        }

        if (oResponse != null)
        {
          if (oResponse.getsuccess())
          {
            //{"success":true,"data":{"transaction_uuid":"4d30bdeb-e156-4746-baa3-f6a8d5d99eff","transaction_hash":null,"from_uuid":"06efebcc-2422-4fb1-a2da-a654e7992fc6","to_uuid":"8613864a-97b5-4802-a745-a4bc72511cd3","transaction_kind":"like"}}
//              m_oTransactions.addTransactionUuid(oResponse.getdata().gettransaction_uuid());
//              cmbTransactionHistory.addItem(oResponse.getdata().gettransaction_uuid());

            String sKind = "-1";
            String[] sTransactionName = oResponse.getdata().gettransaction().getkind().split(" ");
            if (sTransactionName.length>1)
            {
              sKind = sTransactionName[1].trim();
            }
            String transaction_uuid = oResponse.getdata().gettransaction().getid();
            cDatabaseHandler.instance().saveTransaction(transaction_uuid, sKind);
            cmbTransactionHistory.addItem(oResponse.getdata().gettransaction_uuid());

            showInfo("Transaction Successfully executed! :-)");
          }
          else
          {
            showError(oResponse.geterr(), "Transaction Error");
          }
        }
        btnExecute.setText("Execute Transaction");
        btnExecute.setEnabled(true);
      }
    }).start();
  }//GEN-LAST:event_btnExecuteActionPerformed

  public void loadUsers()
  {
    btnListUsers.setText("Please wait...");
    btnListUsers.setEnabled(false);
    new Thread(new Runnable()
    {
      @Override
      public void run()
      {
        clearUsersTable();

        String sOrderBy = cmbOrderBy.getSelectedItem() + "";
        String sOrder = cmbOrder.getSelectedItem() + "";
        String sFilter = cmbFilter.getSelectedItem() + "";
        String sPage = spnPage.getValue() + "";

        cResponse oResponse = m_oUserManagement.listUsers(sPage, sFilter, sOrderBy, sOrder);

        if (oResponse.getsuccess())
        {
          cData oUserData = oResponse.getdata();
          m_lsUsers = oUserData.geteconomy_users();

          String sPage_no = oUserData.getmeta().getoNextPagePayload().getPage_no();
          if (sPage_no != null)
          {
            int iPage = Integer.parseInt(sPage);
            int iNextPage = Integer.parseInt(sPage_no);
            if (iNextPage > m_iMaxPageNumber)
            {
              m_iMaxPageNumber = iNextPage;
              spnPage.setModel(new SpinnerNumberModel(iPage, 1, m_iMaxPageNumber, 1));
            }
          }

          cmbFromUser.removeAllItems();
          cmbToUser.removeAllItems();
          cUserManagement.m_oUsers.clear();
          DefaultTableModel oUserModel = (DefaultTableModel) tblUsers.getModel();
          int iItem = 0;
          for (cEconomyUser oUser : m_lsUsers)
          {
            cUserManagement.m_oUsers.put(oUser.getName(), oUser.getUuid());
            cDatabaseHandler.instance().setUUID(oUser.getName(), oUser.getUuid());

            oUserModel.addRow(new Object[]
            {
              oUser.getName(), oUser.getToken_balance(), oUser.getTotal_airdropped_tokens(), oUser.getUuid()
            });
          }
          
          for (cEconomyUser oUser : m_lsUsers)
          {
            cmbFromUser.addItem(oUser.getName() + " - " + oUser.getUuid());
            if (iItem++ != 0)
            {
              cmbToUser.addItem(oUser.getName() + " - " + oUser.getUuid());
            }
            if (cDatabaseHandler.instance().addUserIfNotExists(oUser.getName(), oUser.getUuid()))
            {
              System.out.println("Added non-existing user: '" + oUser.getName() + "' - " + oUser.getUuid());
            }
          }
        }
        else
        {
          showError(oResponse.geterr(), "Failed to retrieve users");
        }
        btnListUsers.setText("Get Users");
        btnListUsers.setEnabled(true);
      }
    }).start();
  }

  public void clearUsersTable()
  {
    DefaultTableModel dm = (DefaultTableModel) tblUsers.getModel();
    int rowCount = dm.getRowCount();
    //Remove rows one by one from the end of the table
    for (int i = rowCount - 1; i >= 0; i--)
    {
      dm.removeRow(i);
    }
    tblUsers.repaint();
  }

  private void clearTransactionTable()
  {
    DefaultTableModel dm = (DefaultTableModel) tblTransactions.getModel();
    int rowCount = dm.getRowCount();
    //Remove rows one by one from the end of the table
    for (int i = rowCount - 1; i >= 0; i--)
    {
      dm.removeRow(i);
    }
    tblUsers.repaint();
  }

  public int getUserTableColumnIndexByHeading(String _sColumnHeading)
  {
    DefaultTableModel oUserModel = (DefaultTableModel) tblUsers.getModel();
    for (int iCol = 0; iCol < oUserModel.getColumnCount(); iCol++)
    {
      if (oUserModel.getColumnName(iCol).equals(_sColumnHeading))
      {
        return iCol;
      }
    }
    return -1;
  }

  public int getTransactionTableColumnIndexByHeading(String _sColumnHeading)
  {
    DefaultTableModel oTransactionModel = (DefaultTableModel) tblTransactions.getModel();
    for (int iCol = 0; iCol < oTransactionModel.getColumnCount(); iCol++)
    {
      if (oTransactionModel.getColumnName(iCol).equals(_sColumnHeading))
      {
        return iCol;
      }
    }
    return -1;
  }

  public int getTransactionHistoryTableColumnIndexByHeading(String _sColumnHeading)
  {
    DefaultTableModel oTransactionModel = (DefaultTableModel) tblTransactionStatus.getModel();
    for (int iCol = 0; iCol < oTransactionModel.getColumnCount(); iCol++)
    {
      if (oTransactionModel.getColumnName(iCol).equals(_sColumnHeading))
      {
        return iCol;
      }
    }
    return -1;
  }

  public static void showInfo(String sMessage)
  {
    JOptionPane.showMessageDialog(OSTKitAlpha.m_oTokenManagementPanel, sMessage);
  }

  public static void showError(cError oError, String sMessage)
  {
    if (oError != null)
    {
      JOptionPane.showMessageDialog(OSTKitAlpha.m_oTokenManagementPanel, sMessage
          + "\nCode: " + oError.getcode()
          + "\nMessage: " + oError.getmsg());
    }
    else
    {
      JOptionPane.showMessageDialog(OSTKitAlpha.m_oTokenManagementPanel, sMessage);
    }
  }

  // Variables declaration - do not modify//GEN-BEGIN:variables
  private javax.swing.JPanel Tokens;
  private javax.swing.JButton btnAirdrop;
  private javax.swing.JButton btnAirdropStatus;
  private javax.swing.JButton btnCreateTransaction;
  private javax.swing.JButton btnCreateUser;
  private javax.swing.JButton btnExecute;
  private javax.swing.JButton btnListTransactions;
  private javax.swing.JButton btnListUsers;
  private javax.swing.JButton btnSaveTransaction;
  private javax.swing.JButton btnTransactionStatus;
  private javax.swing.JComboBox<String> cmbAirdropTo;
  private javax.swing.JComboBox<String> cmbAirdrops;
  private javax.swing.JComboBox<String> cmbCurrencyType;
  private javax.swing.JComboBox<String> cmbCurrencyType1;
  private javax.swing.JComboBox<String> cmbFilter;
  private javax.swing.JComboBox<String> cmbFromUser;
  private javax.swing.JComboBox<String> cmbOrder;
  private javax.swing.JComboBox<String> cmbOrderBy;
  private javax.swing.JComboBox<String> cmbToUser;
  private javax.swing.JComboBox<String> cmbTransactionHistory;
  private javax.swing.JComboBox<String> cmbTransactionKind;
  private javax.swing.JComboBox<String> cmbTransactionKind1;
  private javax.swing.JLabel jLabel1;
  private javax.swing.JLabel jLabel2;
  private javax.swing.JLabel jLabel3;
  private javax.swing.JLabel jLabel4;
  private javax.swing.JLabel jLabel5;
  private javax.swing.JScrollPane jScrollPane1;
  private javax.swing.JScrollPane jScrollPane2;
  private javax.swing.JScrollPane jScrollPane3;
  private javax.swing.JScrollPane jScrollPane4;
  private javax.swing.JScrollPane jScrollPane5;
  private javax.swing.JScrollPane jScrollPane7;
  private javax.swing.JTabbedPane jTabbedPane1;
  private javax.swing.JLabel lblAirdropAmount;
  private javax.swing.JLabel lblAirdropStatus;
  private javax.swing.JLabel lblAirdropToUsers;
  private javax.swing.JLabel lblAirdropUUIDs;
  private javax.swing.JLabel lblCommissionPercentage;
  private javax.swing.JLabel lblCommissionPercentage1;
  private javax.swing.JLabel lblCurrencyType;
  private javax.swing.JLabel lblCurrencyType1;
  private javax.swing.JLabel lblCurrencyValue;
  private javax.swing.JLabel lblCurrencyValue1;
  private javax.swing.JLabel lblFromUser;
  private javax.swing.JLabel lblKind;
  private javax.swing.JLabel lblKind1;
  private javax.swing.JLabel lblToUser;
  private javax.swing.JLabel lblTransactionIDLabel;
  private javax.swing.JLabel lblTransactionId;
  private javax.swing.JLabel lblTransactionName;
  private javax.swing.JLabel lblTransactionName1;
  private javax.swing.JTabbedPane oMainTabPane;
  private javax.swing.JPanel oUsersPanel;
  private javax.swing.JScrollPane oUsersTabScrollTab;
  private javax.swing.JPanel pnlAirdrops;
  private javax.swing.JPanel pnlExistingTransactions;
  private javax.swing.JPanel pnlNewTransaction;
  private javax.swing.JPanel pnlTransactionHistory;
  private javax.swing.JPanel pnlTransactions;
  private javax.swing.JSpinner spnAirdropAmount;
  private javax.swing.JSpinner spnCommissionPercentage;
  private javax.swing.JSpinner spnCommissionPercentage1;
  private javax.swing.JSpinner spnPage;
  private javax.swing.JTable tblAirdropStepsCompleted;
  private javax.swing.JTable tblTokens;
  private javax.swing.JTable tblTransactionStatus;
  private javax.swing.JTable tblTransactions;
  private javax.swing.JTable tblUsers;
  private javax.swing.JFormattedTextField txtCurrencyValue;
  private javax.swing.JFormattedTextField txtCurrencyValue1;
  private javax.swing.JTextField txtName;
  private javax.swing.JTextField txtTransactionName;
  private javax.swing.JTextField txtTransactionName1;
  // End of variables declaration//GEN-END:variables
}
