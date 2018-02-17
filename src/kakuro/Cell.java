package kakuro;

/**
 * Abstract class containing general cell information.
 */
public abstract class Cell {

    /**
     * The column of the cell
     */
    final int column;
    /**
     * The row of the cell.
     */
    final int row;

    /**
     * Constructor for the cell class.
     *
     * @param column The index of the column.
     * @param row    The index of the row.
     */
    Cell(int column, int row) {
        this.column = column;
        this.row = row;
    }

    /**
     * Gets the column.
     *
     * @return The index of the column.
     */
    public int getColumn() {
        return column;
    }

    /**
     * Gets the row.
     *
     * @return The index of the row.
     */
    public int getRow() {
        return row;
    }

    /**
     * Get a string representation of the object.
     *
     * @return A string representation of the object.
     */
    @Override
    public String toString() {
        return "Row: " + this.row + " Column: " + this.column;
    }
}
