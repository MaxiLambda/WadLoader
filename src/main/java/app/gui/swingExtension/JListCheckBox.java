package app.gui.swingExtension;

import javax.swing.*;
import java.util.Vector;

public class JListCheckBox<E> extends JList<E> {

    public JListCheckBox(ListModel<E> dataModel) {
        super(dataModel);
        setUp();
    }

    public JListCheckBox(E[] listData) {
        super(listData);
        setUp();
    }

    public JListCheckBox(Vector<? extends E> listData) {
        super(listData);
        setUp();
    }

    public JListCheckBox() {
        setUp();
    }

    private void setUp() {
        setCellRenderer(new CheckboxListCellRenderer<E>());
        setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);

    }
}
