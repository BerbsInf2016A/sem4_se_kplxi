package kakuro;

/**
 * A horizontal cell set.
 */
public class HorizontalCellSet extends CellSet {
    /**
     * Constructor for a horizontal cell set.
     *
     * @param maxValue The maximum value of the set.
     */
    public HorizontalCellSet(int maxValue) {
        super(Orientation.HORIZONTAL, maxValue);
    }

    /**
     * Add a cell to the set.
     *
     * @param cell The cell to add.
     */
    void addCell(SettableCell cell) {
        this.getCells().add(cell);
        cell.setHorizontalSet(this);
    }
}
