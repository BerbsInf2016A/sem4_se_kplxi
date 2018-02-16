package kakuro;

/**
 * A single constraint cell represents a constraint for a specific orientation.
 */
public class SingleConstraintCell extends ConstraintCell {
    /**
     * The orientation of the constraint.
     */
    private final Orientation orientation;

    /**
     * The max value of the constraint.
     */
    private final int maxValue;

    /**
     * Constructor for the single constraint cell.
     *
     * @param column The column of the cell.
     * @param row The row of the cell.
     * @param orientation The orientation of the constraint.
     * @param maxValue The max value of the constraint.
     */
    public SingleConstraintCell(int column, int row, Orientation orientation, int maxValue) {
        super(column, row);
        this.orientation = orientation;
        this.maxValue = maxValue;
    }

    /**
     * Get the orientation of the constraint.
     *
     * @return The orientation of the constraint.
     */
    public Orientation getOrientation() {
        return orientation;
    }

    /**
     * Get the max value of the constraint.
     *
     * @return The max value of the constraint.
     */
    public int getMaxValue() {
        return maxValue;
    }
}
