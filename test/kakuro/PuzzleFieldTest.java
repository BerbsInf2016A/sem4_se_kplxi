package kakuro;

import org.junit.Test;

import java.util.OptionalInt;

import static org.junit.Assert.assertEquals;

public class PuzzleFieldTest {
    @Test
    public void getFieldCell() {
        BlockingCell firstCell = new BlockingCell(1, 1);
        SingleConstraintCell secondCell = new SingleConstraintCell(1, 2, Orientation.VERTICAL, 6);
        PuzzleField field = new PuzzleField();
        field.getCells().add(firstCell);
        field.getCells().add(secondCell);

        assertEquals("Blocking cell should be equal", firstCell, field.getFieldCell(1, 1));
        assertEquals("Constraint cell should be equal", secondCell, field.getFieldCell(2, 1));
    }

    @Test
    public void getNextExmptyCell() {
        SetableCell cellWithValue = new SetableCell(1, 1);
        cellWithValue.setValue(OptionalInt.of(4));
        SingleConstraintCell blockCell = new SingleConstraintCell(2, 1, Orientation.VERTICAL, 6);
        SetableCell cellWithOutValue = new SetableCell(3, 1);
        PuzzleField field = new PuzzleField();
        field.getCells().add(cellWithValue);
        field.getCells().add(blockCell);
        field.getCells().add(cellWithOutValue);

        // The next cell which can store a value is the empty SetableCell. The blocking cell should be skipped.
        assertEquals("The empty SetableCell should be returned", cellWithOutValue, field.getNextEmptyCell(cellWithValue));
    }

}