package kakuro;

/**
 * Interface for listeners, which are interested in the event of a value change of a cell.
 */
public interface ICellValueChangedListener {
    /**
     * The method which should be called, when the value of a cell is changed.
     *
     * @param cell The cell, which value is changed.
     */
    void cellValueChanged(SetableCell cell);
}
