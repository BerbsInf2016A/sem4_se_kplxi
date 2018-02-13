package kakuro;

import java.util.*;
import java.util.stream.Collectors;

public class Application {
    public void run() {
        PuzzleField field = this.generatePuzzle();
        System.out.println(field);

        this.generateHorizontalSets(field);
        this.generateVerticalSets(field);
        // start solver
        field.triggerSetUpdates();
        this.solve2(field);
    }

    private void solve(PuzzleField field) {
        for (Cell rawCell : field.cells) {
            if (rawCell instanceof SetableCell){
                SetableCell cell = (SetableCell) rawCell;
                HorizontalCellSet hSet = cell.getHorizontalSet();
                VerticalCellSet vSet = cell.getVerticalSet();
                if (hSet.possibleCombinations.size() == 1 && vSet.possibleCombinations.size() == 1){

                    List<Integer> intersection = Helpers.intersect(hSet.possibleCombinations.get(0), vSet.possibleCombinations.get(0));
                    for (int possibleCandidate : intersection ) {
                        boolean candidateCanBeUsedElsewhereInHorizontalSet = hSet.canBeUsedElseWhere(cell, possibleCandidate);
                        boolean candidateCanBeUsedElsewhereInVerticalSet = vSet.canBeUsedElseWhere(cell, possibleCandidate);
                        if (!candidateCanBeUsedElsewhereInHorizontalSet || !candidateCanBeUsedElsewhereInVerticalSet) {
                            int g = 0;
                        }
                    }
                }
            }
        }
    }

    private void solve2(PuzzleField field) {
        for (Cell rawCell : field.cells) {
            if (rawCell instanceof SetableCell){
                SetableCell cell = (SetableCell) rawCell;
                boolean value = solveForCell((SetableCell) field.getFieldCell(1,2), field);
                int g = 3;
            }
        }
    }

    private boolean solveForCell(SetableCell cell, PuzzleField field ){
        HorizontalCellSet hSet = cell.getHorizontalSet();
        VerticalCellSet vSet = cell.getVerticalSet();

        for (List<Integer> vCombination : cell.getVerticalSet().possibleCombinations ) {
            for (List<Integer> hCombination : cell.getHorizontalSet().possibleCombinations) {
                List<Integer> intersections = Helpers.intersect(vCombination, hCombination);
                for (int possibleCandidate : intersections) {
                    cell.getHorizontalSet().setUsedCombination(hCombination);
                    cell.getVerticalSet().setUsedCombination(vCombination);

                    if (!hSet.valueCanBeUsed(possibleCandidate) || !vSet.valueCanBeUsed(possibleCandidate)) {
                        continue;
                    }
// TODO Somehow endlees loop
                    cell.setValue(possibleCandidate);
                    SetableCell nextCell = field.getNextExmptyCell(cell);
                    if (nextCell == null) {
                        int g = 0;
                        field.printCellValues();
                    }
                    if (!solveForCell(nextCell, field)) {
                        cell.value = OptionalInt.empty();
                        continue;
                    } else {
                        return true;
                    }
                }

            }
        }

        cell.getHorizontalSet().resetUsedCombination();
        cell.getVerticalSet().resetUsedCombination();
        cell.value = OptionalInt.empty();
        return false;
    }

    private void generateHorizontalSets(PuzzleField field) {
        for (int row = 0; row < Configuration.instance.rowAndColumnSize; row++) {
            HorizontalCellSet set = null;
            for (int column = 0; column < Configuration.instance.rowAndColumnSize; column++){
                Cell cell = field.getFieldCell(row, column);
                // Start of a horizontal set
                if ( cell instanceof ConstraintCell && set == null) {
                    if (Helpers.isConstraintCellForOrientation(cell, Orientation.HORIZONTAL)) {
                        set = new HorizontalCellSet(Helpers.getMaxValueFromConstraintCells(cell, Orientation.HORIZONTAL));
                    }
                    continue;
                }
                // End of horizontal set, start of a new one
                if ( cell instanceof ConstraintCell && set != null) {
                    if (set != null) {
                        field.hSets.add(set);
                        set = null;
                        if (Helpers.isConstraintCellForOrientation(cell, Orientation.HORIZONTAL)) {
                            set = new HorizontalCellSet(Helpers.getMaxValueFromConstraintCells(cell, Orientation.HORIZONTAL));
                        }
                        continue;
                    }
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
                if (cell instanceof SetableCell){
                    set.addCell((SetableCell)cell);
                }

                if(column == Configuration.instance.rowAndColumnSize - 1){
                    if (set != null) {
                        field.hSets.add(set);
                        set = null;
                    }
                }
            }
        }
    }

    private void generateVerticalSets(PuzzleField field) {
        for (int column = 0; column < Configuration.instance.rowAndColumnSize; column++) {
            VerticalCellSet set = null;
            for (int row = 0; row < Configuration.instance.rowAndColumnSize; row++){
                Cell cell = field.getFieldCell(row, column);
                // Start of a horizontal set
                if ( cell instanceof ConstraintCell && set == null) {
                    if (Helpers.isConstraintCellForOrientation(cell, Orientation.VERTICAL)) {
                        set = new VerticalCellSet(Helpers.getMaxValueFromConstraintCells(cell, Orientation.VERTICAL));
                    }
                    continue;
                }
                // End of horizontal set, start of a new one
                if ( cell instanceof ConstraintCell && set != null) {
                    if (set != null) {
                        field.vSets.add(set);
                        set = null;
                        if (Helpers.isConstraintCellForOrientation(cell, Orientation.VERTICAL)) {
                            set = new VerticalCellSet(Helpers.getMaxValueFromConstraintCells(cell, Orientation.VERTICAL));
                        }
                        continue;
                    }
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
                if (cell instanceof SetableCell){
                    set.addCell((SetableCell)cell);
                }

                if(row == Configuration.instance.rowAndColumnSize - 1){
                    if (set != null) {
                        field.vSets.add(set);
                        set = null;
                    }
                }
            }
        }
    }

    private PuzzleField generatePuzzle() {
        List<Cell> cells = this.generateConstraintAndBlockCells();
        PuzzleField field = new PuzzleField();
        field.cells.addAll(cells);
        field.cells.addAll(this.generateMissingValueCells(field));
        Comparator<Cell> comparator = Comparator.comparing(c -> c.column);
        comparator = comparator.thenComparing(Comparator.comparing(c -> c.row));
        field.cells = field.cells.stream().sorted(comparator).collect(Collectors.toList());
        return field;
    }

    private Collection<? extends Cell> generateMissingValueCells(PuzzleField field) {
        List<Cell> setableCells = new ArrayList<>();
        for (int row = 0; row < Configuration.instance.rowAndColumnSize; row++) {
            for (int column = 0; column < Configuration.instance.rowAndColumnSize; column++){
                Cell cell = field.getFieldCell(row, column);
                if (cell == null) {
                    setableCells.add(new SetableCell(column, row));
                }
            }
        }
        return setableCells;
    }

    private List<Cell> generateConstraintAndBlockCells() {
        List<Cell> cells = new ArrayList<>(Configuration.instance.rowAndColumnSize * Configuration.instance.rowAndColumnSize);

        // row 0:
        int row = 0;
        cells.add(new BlockingCell(0,row));
        cells.add(new BlockingCell(1,row));
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
        cells.add(new BlockingCell(0,row));
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
