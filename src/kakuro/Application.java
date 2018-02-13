package kakuro;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class Application {
    public void run() {
        PuzzleField field = this.generatePuzzle();
        System.out.println(field);
    }

    private PuzzleField generatePuzzle() {
        List<Cell> cells = this.generateConstraintAndBlockCells();
        PuzzleField field = new PuzzleField();
        field.cells.addAll(cells);
        field.cells.addAll(this.generateMissingValueCells(field));
        Comparator<Cell> comparator = Comparator.comparing(c -> c.xPos);
        comparator = comparator.thenComparing(Comparator.comparing(c -> c.yPos));
        field.cells = field.cells.stream().sorted(comparator).collect(Collectors.toList());
        return field;
    }

    private Collection<? extends Cell> generateMissingValueCells(PuzzleField field) {
        List<Cell> setableCells = new ArrayList<>();
        for (int row = 0; row < Configuration.instance.rowAndColumnSize; row++) {
            for (int column = 0; column < Configuration.instance.rowAndColumnSize; column++){
                Cell cell = field.getFieldCell(row, column);
                if (cell == null) {
                    setableCells.add(new SetableCell(row, column));
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
        cells.add(new ConstraintCell(2, row, Orientation.VERTICAL, 19));
        cells.add(new ConstraintCell(3, row, Orientation.VERTICAL, 12));
        cells.add(new BlockingCell(4, row));
        cells.add(new BlockingCell(5, row));
        cells.add(new BlockingCell(6, row));
        cells.add(new ConstraintCell(7, row, Orientation.VERTICAL, 7));
        cells.add(new ConstraintCell(8, row, Orientation.VERTICAL, 10));

        // row 1:
        row = 1;
        cells.add(new BlockingCell(0, row));
        cells.add(new ConstraintCell(1, row, Orientation.VERTICAL, 14));
        cells.add(new ConstraintCell(4, row, Orientation.VERTICAL, 4));
        cells.add(new ConstraintCell(5, row, Orientation.VERTICAL, 11));
        cells.add(new DoubleConstraintCell(6, row, 4, 17));

        // row 2:
        row = 2;
        cells.add(new BlockingCell(0,row));
        cells.add(new DoubleConstraintCell(1, row, 36, 7));

        // row 3:
        row = 3;
        cells.add(new ConstraintCell(0, row, Orientation.HORIZONTAL, 12));
        cells.add(new ConstraintCell(3, row, Orientation.HORIZONTAL, 10));
        cells.add(new ConstraintCell(7, row, Orientation.VERTICAL, 25));
        cells.add(new ConstraintCell(8, row, Orientation.VERTICAL, 14));

        // row 4:
        row = 4;
        cells.add(new ConstraintCell(0, row, Orientation.HORIZONTAL, 3));
        cells.add(new ConstraintCell(3, row, Orientation.VERTICAL, 20));
        cells.add(new DoubleConstraintCell(4, row, 20, 100));

        // row 5:
        row = 5;
        cells.add(new ConstraintCell(0, row, Orientation.HORIZONTAL, 17));
        cells.add(new ConstraintCell(5, row, Orientation.VERTICAL, 8));
        cells.add(new ConstraintCell(6, row, Orientation.HORIZONTAL, 6));

        // row 6:
        row = 6;
        cells.add(new BlockingCell(0, row));
        cells.add(new ConstraintCell(1, row, Orientation.VERTICAL, 11));
        cells.add(new DoubleConstraintCell(2, row, 13, 7));
        cells.add(new DoubleConstraintCell(6, row, 10, 4));

        // row 7:
        row = 7;
        cells.add(new ConstraintCell(0, row, Orientation.HORIZONTAL, 28));
        cells.add(new BlockingCell(8, row));

        // row 8:
        row = 8;
        cells.add(new ConstraintCell(0, row, Orientation.HORIZONTAL, 6));
        cells.add(new BlockingCell(3, row));
        cells.add(new BlockingCell(4, row));
        cells.add(new ConstraintCell(5, row, Orientation.HORIZONTAL, 8));
        cells.add(new BlockingCell(8, row));

        return cells;
    }
}
