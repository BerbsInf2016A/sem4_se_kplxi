package kakuro;

import org.junit.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.*;

public class HelpersTest {

    @Test
    public void getMaxValueFromConstraintCells() {
        DoubleConstraintCell cell = new DoubleConstraintCell(0, 6, 5, 9);
        assertEquals("Horizontal max should be 5", 5,
                Helpers.getMaxValueFromConstraintCells(cell, Orientation.HORIZONTAL));
        assertEquals("Vertical max should be 5", 9,
                Helpers.getMaxValueFromConstraintCells(cell, Orientation.VERTICAL));

        SingleConstraintCell singleConstraintCell = new SingleConstraintCell(6, 6, Orientation.HORIZONTAL, 8);
        assertEquals("Horizontal max should be 8", 8,
                Helpers.getMaxValueFromConstraintCells(singleConstraintCell, Orientation.HORIZONTAL));
    }

    @Test
    public void isConstraintCellForOrientation() {
        DoubleConstraintCell cell = new DoubleConstraintCell(0, 6, 5, 9);
        assertTrue("Cell should be a horizontal constraint",
                Helpers.isConstraintCellForOrientation(cell, Orientation.HORIZONTAL));
        assertTrue("Cell should be a vertical constraint",
                Helpers.isConstraintCellForOrientation(cell, Orientation.VERTICAL));

        SingleConstraintCell horizontalConstraint =
                new SingleConstraintCell(5, 5, Orientation.HORIZONTAL, 5);
        assertTrue("Cell should be a horizontal constraint",
                Helpers.isConstraintCellForOrientation(horizontalConstraint, Orientation.HORIZONTAL));
        assertFalse("Cell should not be a vertical constraint",
                Helpers.isConstraintCellForOrientation(horizontalConstraint, Orientation.VERTICAL));
    }

    @Test
    public void intersect() {
        List<Integer> firstList = Arrays.asList(1, 2, 3);
        List<Integer> secondList = Arrays.asList(8, 7, 3, 6, 2);
        assertEquals("Intersection should be 2 and 3", Arrays.asList(2, 3), Helpers.intersect(firstList, secondList));
    }

}