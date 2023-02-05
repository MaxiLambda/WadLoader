package app.gui.swingExtension;

import java.awt.*;

public class GridBagConstraintsFixedColumnCount extends GridBagConstraints {
    int iterator;
    int columnCount;

    public GridBagConstraintsFixedColumnCount(int columnCount) {
        this.columnCount = columnCount;
        iterator = 0;
        gridx = 0;
        gridy = 0;
        fill = GridBagConstraints.BOTH;
    }

    private void prepareNext() {
        iterator++;
        gridx = iterator % columnCount;
        gridy = iterator / columnCount;
    }

    public GridBagConstraintsFixedColumnCount getNextConstraints() {
        GridBagConstraintsFixedColumnCount constraints = (GridBagConstraintsFixedColumnCount) this.clone();
        prepareNext();
        return constraints;
    }

    public GridBagConstraintsFixedColumnCount getNextConstraints(double weight) {
        weightx = weight;
        weighty = weight;
        GridBagConstraintsFixedColumnCount constraints = (GridBagConstraintsFixedColumnCount) this.clone();
        prepareNext();
        return constraints;
    }

}
