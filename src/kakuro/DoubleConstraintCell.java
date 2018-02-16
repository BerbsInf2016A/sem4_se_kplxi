package kakuro;

/**
 * A double constraint cell contains a horizontal and a vertical constraint.
 */
public class DoubleConstraintCell extends ConstraintCell {
    /**
     * The horizontal max value for the constraint.
     */
    private final int horizontalMax;

    /**
     * The vertical max value for the constraint.
     */
    private final int verticalMax;

    /**
     * Constructor for the a double constraint cell.
     *
     * @param column Index for the column.
     * @param row Index for the row.
     * @param horizontalMax The max value for the horizontal constraint.
     * @param verticalMax The max value for the vertical constraint.
     */
    public DoubleConstraintCell(int column, int row, int horizontalMax, int verticalMax) {
        super(column, row);
        this.horizontalMax = horizontalMax;
        this.verticalMax = verticalMax;
    }

    /**
     * Get the max value of the horizontal constraint.
     *
     * @return The max value.
     */
    public int getHorizontalMax() {
        return horizontalMax;
    }

    /**
     * Get the max value of the vertical constraint.
     *
     * @return The max value.
     */
    public int getVerticalMax() {
        return verticalMax;
    }
}
