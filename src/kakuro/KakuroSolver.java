package kakuro;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

/**
 * An algorithm to solve a kakuro puzzle.
 */
public class KakuroSolver {
    /**
     * The play field.
     */
    private PuzzleField field;

    /**
     * Get the field of the puzzle.
     *
     * @return The field of the puzzle.
     */
    public PuzzleField getField() {
        return field;
    }

    /**
     * Generate the play field.
     */
    public void generateField() {
        PuzzleField field = this.generatePuzzle();
        if (Configuration.instance.printDebugMessages) {
            System.out.println(field);
        }

        this.generateHorizontalSets(field);
        this.generateVerticalSets(field);

        field.triggerSetUpdates();
        this.field = field;
    }

    /**
     * Solve the play field.
     *
     * @return True if the puzzle is solved, false if not.
     */
    public boolean solveField() {
        return this.solve(this.field);
    }

    /**
     * Solve the play field.
     *
     * @param field the play field to solve.
     * @return True if the puzzle is solved, false if not.
     */
    private boolean solve(PuzzleField field) {
        for (Cell rawCell : field.cells) {
            if (rawCell instanceof SettableCell) {
                return solveForCell((SettableCell) field.getFieldCell(1, 2), field);
            }
        }
        return false;
    }

    /**
     * Tries every candidate for a cell in the specified intersection.
     *
     * @param intersections The possible values of the intersection of set combinations.
     * @param cell          The cell to add the value to.
     * @param hSet          The horizontal set of the cell.
     * @param vSet          The vertical set of the cell.
     * @param field         The play field.
     * @return True if the field is solved, false if not.
     */
    private boolean tryInterSections(List<Integer> intersections, SettableCell cell, HorizontalCellSet hSet, VerticalCellSet vSet, PuzzleField field) {
        for (int possibleCandidate : intersections) {
            if (hSet.canBeSet(possibleCandidate) && vSet.canBeSet(possibleCandidate)) {
                cell.setValue(possibleCandidate);
                SettableCell nextCell = field.getNextEmptyCell(cell);
                if (nextCell == null) {
                    return field.validateCellValues();
                }
                if (!solveForCell(nextCell, field)) {
                    cell.resetValue();
                } else {
                    return true;
                }
            }
        }
        cell.resetValue();
        return false;
    }

    /**
     * Tries to solve the puzzle for a specified cell.
     *
     * @param cell  The cell to solve.
     * @param field The current playing field.
     * @return True if solved, false if not.
     */
    private boolean solveForCell(SettableCell cell, PuzzleField field) {
        try {
            Thread.sleep(Configuration.instance.stepDelayInMilliseconds);

        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            e.printStackTrace();
        }
        if (Thread.currentThread().isInterrupted()) {
            return false;
        }

        // There are four possible states for the two sets of a cell:
        HorizontalCellSet hSet = cell.getHorizontalSet();
        VerticalCellSet vSet = cell.getVerticalSet();

        // Both sets have a combination set.
        if (hSet.getCurrentlyUsedCombination() != null && vSet.getCurrentlyUsedCombination() != null) {
            List<Integer> intersections = Helpers.intersect(vSet.getCurrentlyUsedCombination(), hSet.getCurrentlyUsedCombination());
            return tryInterSections(intersections, cell, hSet, vSet, field);
        }

        // No set has a combination set.
        if (hSet.getCurrentlyUsedCombination() == null && vSet.getCurrentlyUsedCombination() == null) {
            for (List<Integer> hCombination : hSet.getPossibleCombinations()) {
                for (List<Integer> vCombination : vSet.getPossibleCombinations()) {
                    List<Integer> intersections = Helpers.intersect(hCombination, vCombination);
                    if (intersections.size() > 0) {
                        hSet.setCurrentlyUsedCombination(hCombination);
                        vSet.setCurrentlyUsedCombination(vCombination);

                        if (!tryInterSections(intersections, cell, hSet, vSet, field)) {
                            hSet.resetCurrentlyUsedCombination();
                            vSet.resetCurrentlyUsedCombination();
                        } else {
                            return true;
                        }
                    }
                }
            }
        }

        // Horizontal set has a combination set, vertical set has no combination set.
        if (hSet.getCurrentlyUsedCombination() != null && vSet.getCurrentlyUsedCombination() == null) {
            for (List<Integer> vCombination : vSet.getPossibleCombinations()) {
                List<Integer> intersections = Helpers.intersect(hSet.getCurrentlyUsedCombination(), vCombination);
                if (intersections.size() > 0) {
                    vSet.setCurrentlyUsedCombination(vCombination);

                    if (!tryInterSections(intersections, cell, hSet, vSet, field)) {
                        vSet.resetCurrentlyUsedCombination();
                    } else {
                        return true;
                    }
                }
            }
        }

        // Vertical set has a combination set, horizontal set has no combination set.
        if (hSet.getCurrentlyUsedCombination() == null && vSet.getCurrentlyUsedCombination() != null) {
            for (List<Integer> hCombination : hSet.getPossibleCombinations()) {
                List<Integer> intersections = Helpers.intersect(vSet.getCurrentlyUsedCombination(), hCombination);
                if (intersections.size() > 0) {
                    hSet.setCurrentlyUsedCombination(hCombination);

                    if (!tryInterSections(intersections, cell, hSet, vSet, field)) {
                        hSet.resetCurrentlyUsedCombination();
                    } else {
                        return true;
                    }
                }
            }
        }

        cell.resetValue();
        return false;
    }

    /**
     * Generates the horizontal sets.
     *
     * @param field The play field to generate the sets on.
     */
    private void generateHorizontalSets(PuzzleField field) {
        for (int row = 0; row < Configuration.instance.rowAndColumnSize; row++) {
            HorizontalCellSet set = null;
            for (int column = 0; column < Configuration.instance.rowAndColumnSize; column++) {
                Cell cell = field.getFieldCell(row, column);
                // Start of a horizontal set
                if (cell instanceof ConstraintCell && set == null) {
                    if (Helpers.isConstraintCellForOrientation(cell, Orientation.HORIZONTAL)) {
                        set = new HorizontalCellSet(Helpers.getMaxValueFromConstraintCells(cell, Orientation.HORIZONTAL));
                    }
                    continue;
                }
                // End of horizontal set, start of a new one
                if (cell instanceof ConstraintCell) {
                    field.hSets.add(set);
                    set = null;
                    if (Helpers.isConstraintCellForOrientation(cell, Orientation.HORIZONTAL)) {
                        set = new HorizontalCellSet(Helpers.getMaxValueFromConstraintCells(cell, Orientation.HORIZONTAL));
                    }
                    continue;
                }
                // End of horizontal set
                if (cell instanceof BlockingCell) {
                    if (set != null) {
                        field.hSets.add(set);
                        set = null;
                        continue;
                    }
                    continue;
                }
                // Cell part of the sets
                if (cell instanceof SettableCell) {
                    if (set != null) {
                        set.addCell((SettableCell) cell);
                    }
                }

                if (column == Configuration.instance.rowAndColumnSize - 1) {
                    if (set != null) {
                        field.hSets.add(set);
                        set = null;
                    }
                }
            }
        }
    }

    /**
     * Generates the vertical sets.
     *
     * @param field The playing field to generate the sets on.
     */
    private void generateVerticalSets(PuzzleField field) {
        for (int column = 0; column < Configuration.instance.rowAndColumnSize; column++) {
            VerticalCellSet set = null;
            for (int row = 0; row < Configuration.instance.rowAndColumnSize; row++) {
                Cell cell = field.getFieldCell(row, column);
                // Start of a horizontal set
                if (cell instanceof ConstraintCell && set == null) {
                    if (Helpers.isConstraintCellForOrientation(cell, Orientation.VERTICAL)) {
                        set = new VerticalCellSet(Helpers.getMaxValueFromConstraintCells(cell, Orientation.VERTICAL));
                    }
                    continue;
                }
                // End of horizontal set, start of a new one
                if (cell instanceof ConstraintCell) {
                    field.vSets.add(set);
                    set = null;
                    if (Helpers.isConstraintCellForOrientation(cell, Orientation.VERTICAL)) {
                        set = new VerticalCellSet(Helpers.getMaxValueFromConstraintCells(cell, Orientation.VERTICAL));
                    }
                    continue;
                }
                // End of horizontal set
                if (cell instanceof BlockingCell) {
                    if (set != null) {
                        field.vSets.add(set);
                        set = null;
                        continue;
                    }
                    continue;
                }
                // Cell part of the sets
                if (cell instanceof SettableCell) {
                    if (set != null) {
                        set.addCell((SettableCell) cell);
                    }
                }

                if (row == Configuration.instance.rowAndColumnSize - 1) {
                    if (set != null) {
                        field.vSets.add(set);
                        set = null;
                    }
                }
            }
        }
    }

    /**
     * Generates the puzzle.
     *
     * @return The puzzle field.
     */
    private PuzzleField generatePuzzle() {
        List<Cell> cells = this.generateConstraintAndBlockCells();
        PuzzleField field = new PuzzleField();
        field.cells.addAll(cells);
        field.cells.addAll(this.generateMissingValueCells(field));
        Comparator<Cell> comparator = Comparator.comparing(c -> c.row);
        comparator = comparator.thenComparing(Comparator.comparing(c -> c.column));
        field.cells = field.cells.stream().sorted(comparator).collect(Collectors.toList());
        return field;
    }

    /**
     * Generates all missing value cells.
     *
     * @param field The field to generate the cells on.
     * @return A list of generated cells.
     */
    private Collection<? extends Cell> generateMissingValueCells(PuzzleField field) {
        List<Cell> settableCells = new ArrayList<>();
        for (int row = 0; row < Configuration.instance.rowAndColumnSize; row++) {
            for (int column = 0; column < Configuration.instance.rowAndColumnSize; column++) {
                Cell cell = field.getFieldCell(row, column);
                if (cell == null) {
                    settableCells.add(new SettableCell(column, row));
                }
            }
        }
        return settableCells;
    }

    /**
     * Generates all constraint and blocking cells.
     *
     * @return A list of the generated cells.
     */
    private List<Cell> generateConstraintAndBlockCells() {
        List<Cell> cells = new ArrayList<>(Configuration.instance.rowAndColumnSize * Configuration.instance.rowAndColumnSize);

        // row 0:
        int row = 0;
        cells.add(new BlockingCell(0, row));
        cells.add(new BlockingCell(1, row));
        cells.add(new SingleConstraintCell(2, row, Orientation.VERTICAL, 19));
        cells.add(new SingleConstraintCell(3, row, Orientation.VERTICAL, 12));
        cells.add(new BlockingCell(4, row));
        cells.add(new BlockingCell(5, row));
        cells.add(new BlockingCell(6, row));
        cells.add(new SingleConstraintCell(7, row, Orientation.VERTICAL, 7));
        cells.add(new SingleConstraintCell(8, row, Orientation.VERTICAL, 10));

        // row 1:
        row = 1;
        cells.add(new BlockingCell(0, row));
        cells.add(new SingleConstraintCell(1, row, Orientation.HORIZONTAL, 14));
        cells.add(new SingleConstraintCell(4, row, Orientation.VERTICAL, 4));
        cells.add(new SingleConstraintCell(5, row, Orientation.VERTICAL, 11));
        cells.add(new DoubleConstraintCell(6, row, 4, 17));

        // row 2:
        row = 2;
        cells.add(new BlockingCell(0, row));
        cells.add(new DoubleConstraintCell(1, row, 36, 7));

        // row 3:
        row = 3;
        cells.add(new SingleConstraintCell(0, row, Orientation.HORIZONTAL, 12));
        cells.add(new SingleConstraintCell(3, row, Orientation.HORIZONTAL, 10));
        cells.add(new SingleConstraintCell(7, row, Orientation.VERTICAL, 25));
        cells.add(new SingleConstraintCell(8, row, Orientation.VERTICAL, 14));

        // row 4:
        row = 4;
        cells.add(new SingleConstraintCell(0, row, Orientation.HORIZONTAL, 3));
        cells.add(new SingleConstraintCell(3, row, Orientation.VERTICAL, 20));
        cells.add(new DoubleConstraintCell(4, row, 20, 11));

        // row 5:
        row = 5;
        cells.add(new SingleConstraintCell(0, row, Orientation.HORIZONTAL, 17));
        cells.add(new SingleConstraintCell(5, row, Orientation.VERTICAL, 8));
        cells.add(new SingleConstraintCell(6, row, Orientation.HORIZONTAL, 6));

        // row 6:
        row = 6;
        cells.add(new BlockingCell(0, row));
        cells.add(new SingleConstraintCell(1, row, Orientation.VERTICAL, 11));
        cells.add(new DoubleConstraintCell(2, row, 13, 7));
        cells.add(new DoubleConstraintCell(6, row, 10, 4));

        // row 7:
        row = 7;
        cells.add(new SingleConstraintCell(0, row, Orientation.HORIZONTAL, 28));
        cells.add(new BlockingCell(8, row));

        // row 8:
        row = 8;
        cells.add(new SingleConstraintCell(0, row, Orientation.HORIZONTAL, 6));
        cells.add(new BlockingCell(3, row));
        cells.add(new BlockingCell(4, row));
        cells.add(new SingleConstraintCell(5, row, Orientation.HORIZONTAL, 8));
        cells.add(new BlockingCell(8, row));

        return cells;
    }
}
