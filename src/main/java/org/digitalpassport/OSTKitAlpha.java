package org.digitalpassport;

import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.File;
import javax.swing.ImageIcon;
import javax.swing.JOptionPane;
import javax.swing.WindowConstants;
import org.digitalpassport.ui.cWindowBounds;
import org.digitalpassport.serialization.cSerializationFactory;
import org.digitalpassport.ui.cFileSharingFrame;
import org.digitalpassport.ui.panels.cTokenManagementPanel;

/**
 *
 * @author Philip Trenwith
 */
public class OSTKitAlpha
{
  public static cFileSharingFrame m_oFileSharingFrame;
  public static cTokenManagementPanel m_oTokenManagementPanel;
  private cWindowBounds m_oWindowBounds;
  private cSerializationFactory m_oSerializationFactory = new cSerializationFactory();
    
  private File m_fBounds;
  
  /**
   * @param args the command line arguments
   */
  public static void main(String[] args)
  {
    boolean m_bManagement = (args.length > 0 && args[0].equals("-m"));  
    
    new OSTKitAlpha(m_bManagement);
  }

  public OSTKitAlpha(boolean bManagement)
  {
    m_fBounds = new File("bounds.ser");
    // default window bounds
    m_oWindowBounds = new cWindowBounds();
    
    m_oWindowBounds.set_main_x(50);
    m_oWindowBounds.set_main_y(50);
    m_oWindowBounds.set_main_w(1200);
    m_oWindowBounds.set_main_h(800);
    
    m_oWindowBounds.set_api_x(70);
    m_oWindowBounds.set_api_y(70);
    m_oWindowBounds.set_api_w(1200);
    m_oWindowBounds.set_api_h(800);

    if (m_fBounds.exists())
    {
      m_oWindowBounds = (cWindowBounds) m_oSerializationFactory.deserialize(m_fBounds, false);
    }
    
    String sTitle = "Digital Passport";
    m_oTokenManagementPanel = new cTokenManagementPanel();
    
    m_oFileSharingFrame = new cFileSharingFrame(m_oTokenManagementPanel, bManagement);
    m_oFileSharingFrame.setTitle(sTitle);
    m_oFileSharingFrame.setBounds(m_oWindowBounds.get_main_x(), m_oWindowBounds.get_main_y(), m_oWindowBounds.get_main_w(), m_oWindowBounds.get_main_h());
    m_oFileSharingFrame.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
    m_oFileSharingFrame.addWindowListener(new WindowListener()
    {
      @Override
      public void windowClosing(WindowEvent e)
      {
        if (JOptionPane.showConfirmDialog(m_oFileSharingFrame,
            "Are you sure you want to exit?", "Please Confirm",
            JOptionPane.OK_OPTION, 0, new ImageIcon("")) != 0)
        {
          m_oFileSharingFrame.setVisible(true);
          return;
        }
        else
        {
          terminate();
        }
      }

      @Override
      public void windowOpened(WindowEvent e)
      {
      }

      @Override
      public void windowClosed(WindowEvent e)
      {
      }

      @Override
      public void windowIconified(WindowEvent e)
      {
      }

      @Override
      public void windowDeiconified(WindowEvent e)
      {
      }

      @Override
      public void windowActivated(WindowEvent e)
      {
      }

      @Override
      public void windowDeactivated(WindowEvent e)
      {
      }
    });

    m_oFileSharingFrame.setVisible(true);
  }

  private void terminate()
  {
    m_oWindowBounds.set_main_x(m_oFileSharingFrame.getX());
    m_oWindowBounds.set_main_y(m_oFileSharingFrame.getY());
    m_oWindowBounds.set_main_h(m_oFileSharingFrame.getHeight());
    m_oWindowBounds.set_main_w(m_oFileSharingFrame.getWidth());
        
    if (m_oFileSharingFrame != null)
    {
      m_oFileSharingFrame.terminate();
    }

    m_oSerializationFactory.serialize(m_oWindowBounds, m_fBounds, false);
    System.exit(0);
  }
}
