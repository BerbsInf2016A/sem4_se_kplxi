package kakuroui;

import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import kakuro.ICellValueChangedListener;
import kakuro.SettableCell;

/**
 * A model of a settable cell to bind the ui to.
 */
public class SettableCellModel implements ICellValueChangedListener {
    /**
     * The row of the model.
     */
    private final int row;

    /**
     * The column of the model.
     */
    private final int column;

    /**
     * A property to bind the ui to. Representing the value of the cell.
     */
    private final SimpleStringProperty value = new SimpleStringProperty(this, "value");

    public SettableCellModel(SettableCell cell) {
        cell.addValueChangedListener(this);
        this.row = cell.getRow();
        this.column = cell.getColumn();
        this.value.set("-");
    }

    /**
     * Get the row of the model.
     *
     * @return The row of the model.
     */
    public int getRow() {
        return row;
    }

    /**
     * Get the column of the model.
     *
     * @return The column of the model.
     */
    public int getColumn() {
        return column;
    }

    /**
     * A property to bind the ui to. Representing the value of the cell.
     *
     * @return The property to bind to.
     */
    public SimpleStringProperty valueProperty() {
        return value;
    }

    /**
     * Handle the change in the value of a cell.
     *
     * @param cell The cell, which value is changed.
     */
    @Override
    public void cellValueChanged(SettableCell cell) {
        String cellValue = cell.getUIValue();
        // Update the ui on the ui thread:
        Platform.runLater(new UpdateUIModelTask(this, cellValue));
    }
}