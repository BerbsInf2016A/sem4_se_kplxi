package kakuro;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * A PuzzleField contains all necessary data to solve a kakuro puzzle.
 */
public class PuzzleField {
    /**
     * The horizontal sets of the field.
     */
    final List<HorizontalCellSet> hSets;
    /**
     * The vertical sets of the field.
     */
    final List<VerticalCellSet> vSets;
    /**
     * The cells of the field.
     */
    List<Cell> cells;

    /**
     * Constructor for the puzzle field.
     */
    public PuzzleField() {
        this.cells = new ArrayList<>();
        this.hSets = new ArrayList<>();
        this.vSets = new ArrayList<>();
    }

    /**
     * Get the cells of the field.
     *
     * @return The cells of the field.
     */
    public List<Cell> getCells() {
        return cells;
    }

    /**
     * Get a specific cell.
     *
     * @param row    The row of the wanted cell.
     * @param column The column of the wanted cell.
     * @return The wanted cell.
     */
    public Cell getFieldCell(int row, int column) {
        if (row > Configuration.instance.rowAndColumnSize || column > Configuration.instance.rowAndColumnSize || row < 0 || column < 0) {
            throw new RuntimeException("Invalid row column combination. row: " + row + " column: " + column);
        }
        Optional<Cell> cell = this.cells.stream().filter(c -> c.column == column && c.row == row).findFirst();
        return cell.orElse(null);
    }

    /**
     * Get the string representation of the object.
     *
     * @return The string representation of the object.
     */
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        String separator = System.getProperty("line.separator");
        this.cells.forEach(c -> sb.append(c.column).append("|").append(c.row).append(": Type: ")
                .append(c.getClass()).append(separator));
        return sb.toString();
    }

    /**
     * Triggers the update of the set metadata.
     */
    public void triggerSetUpdates() {
        for (HorizontalCellSet set : this.hSets) {
            set.updateMetadata();
        }
        for (VerticalCellSet set : this.vSets) {
            set.updateMetadata();
        }
    }

    /**
     * Get the next empty cell after the cell, specified by the parameter.
     *
     * @param cell The current cell.
     * @return The next empty cell.
     */
    public SettableCell getNextEmptyCell(SettableCell cell) {
        int columnOffset = cell.column;
        for (int row = cell.row; row < Configuration.instance.rowAndColumnSize; row++) {
            for (int column = columnOffset; column < Configuration.instance.rowAndColumnSize; column++) {
                Cell nextCell = this.getFieldCell(row, column);
                if (nextCell == cell) continue;
                if (nextCell instanceof SettableCell) {
                    SettableCell settableCell = (SettableCell) nextCell;
                    if (!settableCell.getValue().isPresent()) return settableCell;
                }
            }
            columnOffset = 0;
        }
        return null;
    }

    /**
     * Validates the cell values within the sets.
     *
     * @return True if the cell values are valid, false if not.
     */
    public boolean validateCellValues() {
        for (HorizontalCellSet set : this.hSets) {
            int setSum = 0;
            for (SettableCell cell : set.getCells()) {
                if (cell.getValue().isPresent()) {
                    setSum += cell.getValue().getAsInt();
                } else {
                    return false;
                }
            }
            if (setSum != set.maxValue) return false;
        }
        for (VerticalCellSet set : this.vSets) {
            int setSum = 0;
            for (SettableCell cell : set.getCells()) {
                if (cell.getValue().isPresent()) {
                    setSum += cell.getValue().getAsInt();
                } else {
                    return false;
                }
            }
            if (setSum != set.maxValue) return false;
        }
        return true;
    }
}
