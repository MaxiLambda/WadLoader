package app.gui.swingExtension;

import javax.swing.*;
import java.awt.*;

public class CheckboxListCellRenderer<E> extends JCheckBox implements ListCellRenderer<E> {
    @Override
    public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
        setComponentOrientation(list.getComponentOrientation());

        //System.out.printf("%s: %s: isSelectedAtTheMoment - %s, willBeSelected -%s, hasFocus -%s\n",Instant.now().toString(),String.valueOf(value),isSelected(),isSelected,cellHasFocus);
        setFont(list.getFont());
        setText(String.valueOf(value));

        setBackground(list.getBackground());
        setForeground(list.getForeground());

        setSelected(isSelected);

        setEnabled(list.isEnabled());

        return this;
    }
}
