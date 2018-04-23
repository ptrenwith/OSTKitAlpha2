
package org.digitalpassport;

import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.File;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.WindowConstants;
import org.digitalpassport.ui.cWindowBounds;
import org.digitalpassport.serialization.cSerializationFactory;
import org.digitalpassport.ui.cMainPanel;

/**
 *
 * @author Philip Trenwith
 */
public class OSTKitAlpha
{
  public static JFrame oFrame;
  private cMainPanel m_oMainPanel = null;
  private cWindowBounds oWindowBounds;
  private File fBounds;
  private cSerializationFactory oSerializationFactory = new cSerializationFactory();
  
  /**
   * @param args the command line arguments
   */
  public static void main(String[] args)
  {
    new OSTKitAlpha();
  }
  
  public OSTKitAlpha()
  {
    oFrame = new JFrame("Digital Passport");
    m_oMainPanel = new cMainPanel();
    fBounds = new File("bounds.ser");
    // default window bounds
    oWindowBounds = new cWindowBounds();
    oWindowBounds.setX(50);
    oWindowBounds.setY(50);
    oWindowBounds.setW(1200);
    oWindowBounds.setH(800);
    
    if (fBounds.exists())
    {
      oWindowBounds = (cWindowBounds) oSerializationFactory.deserialize(fBounds, false);
    }
    
    oFrame.setBounds(oWindowBounds.getX(), oWindowBounds.getY(), oWindowBounds.getW(), oWindowBounds.getH());
    oFrame.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE );
    oFrame.addWindowListener(new WindowListener() 
    {
        @Override
        public void windowClosing(WindowEvent e) 
        {
            if (JOptionPane.showConfirmDialog(oFrame, 
                    "Are you sure you want to exit?", "Please Confirm", 
                    JOptionPane.OK_OPTION, 0, new ImageIcon("")) != 0) 
            {
              oFrame.setVisible(true);
              return;
            }
            else
            {
              terminate();
            }
        }

        @Override 
        public void windowOpened(WindowEvent e) {}

        @Override 
        public void windowClosed(WindowEvent e) {}

        @Override 
        public void windowIconified(WindowEvent e) {}

        @Override 
        public void windowDeiconified(WindowEvent e) {}

        @Override 
        public void windowActivated(WindowEvent e) {}

        @Override 
        public void windowDeactivated(WindowEvent e) {}

    });
    
    oFrame.add(m_oMainPanel);
    
    oFrame.setVisible(true);
  }
  
  private void terminate()
  {
    oWindowBounds.setX(oFrame.getX());
    oWindowBounds.setY(oFrame.getY());
    oWindowBounds.setH(oFrame.getHeight());
    oWindowBounds.setW(oFrame.getWidth());
    
    if (m_oMainPanel != null)
    {
      m_oMainPanel.terminate();
    }
    
    oSerializationFactory.serialize(oWindowBounds, fBounds, false);
    System.exit(0);
  }
}
