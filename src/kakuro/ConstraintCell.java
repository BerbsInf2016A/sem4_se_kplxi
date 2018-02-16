package kakuro;

/**
 * A abstract cell for constraints.
 */
public abstract class ConstraintCell extends Cell {

    /**
     * Constructor for a constraint cell.
     *
     * @param xPos Index of the column.
     * @param yPos Index of the row.
     */
    public ConstraintCell(int xPos, int yPos) {
        super(xPos, yPos);
    }
}
