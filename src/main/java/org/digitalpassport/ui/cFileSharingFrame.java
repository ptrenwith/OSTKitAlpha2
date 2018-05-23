package org.digitalpassport.ui;

import java.awt.BorderLayout;
import javax.swing.JTabbedPane;
import org.digitalpassport.ui.panels.cFileSharingPanel;
import org.digitalpassport.ui.panels.cTokenManagementPanel;

/**
 *
 * @author Philip M. Trenwith
 */
public class cFileSharingFrame extends javax.swing.JFrame
{

  private cFileSharingPanel m_oFileSharingPanel = null;
  private cTokenManagementPanel m_oTokenManagementPanel = null;
  private boolean m_bManagement;

  public cFileSharingFrame(cTokenManagementPanel oTokenManagementPanel, boolean bManagement)
  {
    initComponents();
    m_oTokenManagementPanel = oTokenManagementPanel;
    m_bManagement = bManagement;
    m_oFileSharingPanel = new cFileSharingPanel(this);
    if (m_bManagement)
    {
      JTabbedPane oTabbedPane = new JTabbedPane();
      oTabbedPane.addTab("File Sharing", m_oFileSharingPanel);
      oTabbedPane.addTab("Token Management", m_oTokenManagementPanel);
      oMainPanel.add(oTabbedPane, BorderLayout.CENTER);
    }
    else
    {
      oMainPanel.add(m_oFileSharingPanel, BorderLayout.CENTER);
    }
  }

  /**
   * This method is called from within the constructor to initialize the form. WARNING: Do NOT modify this code. The
   * content of this method is always regenerated by the Form Editor.
   */
  @SuppressWarnings("unchecked")
  // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
  private void initComponents()
  {

    oMainPanel = new javax.swing.JPanel();

    setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

    oMainPanel.setLayout(new java.awt.BorderLayout());

    javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
    getContentPane().setLayout(layout);
    layout.setHorizontalGroup(
      layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
      .addComponent(oMainPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
    );
    layout.setVerticalGroup(
      layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
      .addComponent(oMainPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
    );

    pack();
  }// </editor-fold>//GEN-END:initComponents

  // Variables declaration - do not modify//GEN-BEGIN:variables
  private javax.swing.JPanel oMainPanel;
  // End of variables declaration//GEN-END:variables

  public void terminate()
  {

  }
}
