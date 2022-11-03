package bsu.rfct.course2.group9.Indyukov;

import java.awt.Color;
import java.awt.Component;
import java.awt.FlowLayout;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.table.TableCellRenderer;

public class HornerTableCellRenderer implements TableCellRenderer {
    private JPanel panel = new JPanel();
    private JLabel label = new JLabel();
    private String needle = null;
    private DecimalFormat formatter =
            (DecimalFormat) NumberFormat.getInstance();

    public HornerTableCellRenderer() {
        formatter.setMaximumFractionDigits(10);
        formatter.setGroupingUsed(false);
        DecimalFormatSymbols dottedDouble = formatter.getDecimalFormatSymbols();
        dottedDouble.setDecimalSeparator('.');
        formatter.setDecimalFormatSymbols(dottedDouble);

        panel.add(label);

        panel.setLayout(new FlowLayout(FlowLayout.LEFT));
    }

    public Component getTableCellRendererComponent(JTable table,
                                                   Object value, boolean isSelected, boolean hasFocus, int row, int col) {

        String formattedDouble = formatter.format(value);

        label.setText(formattedDouble);
        if (col == 1 && needle != null &&
                Double.parseDouble(formattedDouble) - Math.round(Double.parseDouble(formattedDouble))
                        <= Double.parseDouble(needle) &&
                isPrime(Math.round(Double.parseDouble(formattedDouble)))) {
            panel.setBackground(Color.GREEN);
        } else {
            panel.setBackground(Color.WHITE);
        }
        return panel;
    }

    public void setNeedle(String needle) {
        this.needle = needle;
    }

    private boolean isPrime(long x) {

        if (x == 0 || x == 1){
            return false;
        }

        for (long i = 2; i <= (long) Math.floor(Math.sqrt(x)); i++) {
            if (x % i == 0)
                return false;
        }
        return true;
    }
}
