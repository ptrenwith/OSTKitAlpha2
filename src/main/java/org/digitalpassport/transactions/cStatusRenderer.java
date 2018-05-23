package org.digitalpassport.transactions;

import org.digitalpassport.deserialize.json.transactiontypes.eStatus;
import java.awt.Color;
import java.awt.Component;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.table.TableCellRenderer;

/**
 *
 * @author Philip
 */
public class cStatusRenderer implements TableCellRenderer
{

  private JLabel oLabel = new JLabel();

  public cStatusRenderer()
  {
    oLabel.setOpaque(true);
  }

  @Override
  public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected,
      boolean hasFocus, int row, int column)
  {
    eStatus status = (eStatus) value;
    if (status != null)
    {
      oLabel.setText(status.name());
      if (eStatus.active.equals(status))
      {
        oLabel.setBackground(Color.GREEN);
      }
      else
      {
        oLabel.setBackground(Color.RED);
      }
    }
    return oLabel;
  }
}
