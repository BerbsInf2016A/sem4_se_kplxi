package kakuro;

/**
 * A vertical cell set.
 */
public class VerticalCellSet extends CellSet {
    /**
     * Constructor for a vertical cell set.
     *
     * @param maxValue The max value of the set.
     */
    public VerticalCellSet(int maxValue) {
        super(Orientation.VERTICAL, maxValue);
    }

    /**
     * Add a cell to the set.
     *
     * @param cell The cell to add.
     */
    @Override
    protected void addCell(SetableCell cell) {
        this.getCells().add(cell);
        cell.setVerticalSet(this);
    }
}
