package kakuro;

import java.util.ArrayList;
import java.util.List;
import java.util.OptionalInt;

/**
 * A SettableCell is a cell, which value can be set.
 */
public class SettableCell extends Cell {
    /**
     * A list of listeners, which are interested in the event of an changed value.
     */
    private final List<ICellValueChangedListener> listeners;
    /**
     * The value of the cell.
     */
    private OptionalInt value;
    /**
     * The horizontal set of the cell.
     */
    private HorizontalCellSet hSet;
    /**
     * The vertical set of the cell.
     */
    private VerticalCellSet vSet;

    /**
     * Constructor for a SettableCell.
     *
     * @param column The column of the cell.
     * @param row    The row of the cell.
     */
    public SettableCell(int column, int row) {
        super(column, row);
        this.listeners = new ArrayList<>();
        this.value = OptionalInt.empty();

    }

    /**
     * Get the value of the cell.
     *
     * @return The value of the cell.
     */
    public OptionalInt getValue() {
        return value;
    }

    /**
     * Set the value of the cell.
     *
     * @param value The value to set.
     */
    public void setValue(int value) {
        this.value = OptionalInt.of(value);
        this.valueChanged();
    }

    /**
     * Reset the value of the cell.
     */
    public void resetValue() {
        this.value = OptionalInt.empty();
        this.valueChanged();
    }

    /**
     * Called when the value of the cell is changed.
     */
    private void valueChanged() {
        for (ICellValueChangedListener listener : this.listeners) {
            listener.cellValueChanged(this);
        }
        if (Configuration.instance.printDebugMessages) {
            System.out.println("DEBUG: Cell value changed: row: " + this.row + " column: " + this.column + " value: "
                    + this.getUIValue());
        }
    }

    /**
     * Get the horizontal set of the cell.
     *
     * @return The horizontal set of the cell.
     */
    public HorizontalCellSet getHorizontalSet() {
        return hSet;
    }

    /**
     * Set the horizontal set of the cell.
     *
     * @param hSet The set to set.
     */
    public void setHorizontalSet(HorizontalCellSet hSet) {
        this.hSet = hSet;
    }

    /**
     * Get the vertical set of the cell.
     *
     * @return The vertical set of the cell.
     */
    public VerticalCellSet getVerticalSet() {
        return vSet;
    }

    /**
     * Set the vertical set of the cell.
     *
     * @param vSet The set to set.
     */
    public void setVerticalSet(VerticalCellSet vSet) {
        this.vSet = vSet;
    }

    /**
     * Add a listener, which is interested in the event of a changed cell value.
     *
     * @param listener The listener to add.
     */
    public void addValueChangedListener(ICellValueChangedListener listener) {
        if (this.listeners.contains(listener)) return;
        this.listeners.add(listener);
    }

    /**
     * Get the UI Value of this cell.
     *
     * @return A value, which can be used by a UI.
     */
    public String getUIValue() {
        if (this.value.isPresent()) {
            return String.valueOf(this.value.getAsInt());
        } else return "-";
    }

    /**
     * Get the string representation of this class.
     *
     * @return The string representation of this class.
     */
    @Override
    public String toString() {
        String value = "null";
        if (this.value.isPresent()) {
            value = String.valueOf(this.value.getAsInt());
        }
        return super.toString() + " Value: " + value;
    }
}
