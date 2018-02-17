package kakuro;


import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static junit.framework.TestCase.*;
import static org.junit.Assert.assertTrue;


public class CellSetTests {
    @Before
    public void before() {
        Configuration.instance.printDebugMessages = false;
        Configuration.instance.stepDelayInMilliseconds = 0;
    }


    @Test
    public void horizontalSet_addCell() {
        SettableCell cell = new SettableCell(0, 0);

        HorizontalCellSet cellSet = new HorizontalCellSet(5);
        cellSet.addCell(cell);

        assertEquals("CellSet should contain one cell", 1, cellSet.getCells().size());
        assertEquals("CellSet should be set as horizontal cell set of the cell", cellSet, cell.getHorizontalSet());
    }


    @Test
    public void verticalSet_addCell() {
        SettableCell cell = new SettableCell(1, 1);

        VerticalCellSet cellSet = new VerticalCellSet(5);
        cellSet.addCell(cell);

        assertEquals("CellSet should contain one cell", 1, cellSet.getCells().size());
        assertEquals("CellSet should be set as vertical cell set of the cell", cellSet, cell.getVerticalSet());
    }

    @Test
    public void cellSet_addCell_SetsAreSetInTheCell() {
        SettableCell cell = new SettableCell(1, 1);

        VerticalCellSet vCellSet = new VerticalCellSet(5);
        vCellSet.addCell(cell);

        HorizontalCellSet hCellSet = new HorizontalCellSet(5);
        hCellSet.addCell(cell);

        assertEquals("CellSet should contain one cell", 1, vCellSet.getCells().size());
        assertEquals("CellSet should be set as vertical cell set of the cell", vCellSet, cell.getVerticalSet());

        assertEquals("CellSet should contain one cell", 1, hCellSet.getCells().size());
        assertEquals("CellSet should be set as horizontal cell set of the cell", hCellSet, cell.getHorizontalSet());
    }

    @Test
    public void cellSet_UpdateMetadata() {
        VerticalCellSet vCellSet = new VerticalCellSet(5);

        SettableCell firstCell = new SettableCell(1, 1);
        SettableCell secondCell = new SettableCell(1, 2);
        vCellSet.addCell(firstCell);
        vCellSet.addCell(secondCell);

        vCellSet.updateMetadata();

        // A 5 can be partitioned into 3 + 2 or 4 + 1

        List<List<Integer>> actualCombinations = vCellSet.getPossibleCombinations();

        List<List<Integer>> expectedCombinations = new ArrayList<>(2);
        expectedCombinations.add(Arrays.asList(4, 1));
        expectedCombinations.add(Arrays.asList(3, 2));


        assertEquals("Combination should have the correct count", expectedCombinations.size(), actualCombinations.size());
        for (int i = 0; i < expectedCombinations.size(); i++) {
            List<Integer> expected = expectedCombinations.get(i);
            List<Integer> actual = actualCombinations.get(i);

            assertEquals("Expected partition should be equal to the actual partition", expected, actual);
        }
    }

    @Test
    public void cellSet_valueCanBeSet_True() {
        VerticalCellSet vCellSet = new VerticalCellSet(5);

        SettableCell firstCell = new SettableCell(1, 1);
        firstCell.setValue(2);
        SettableCell secondCell = new SettableCell(1, 2);
        vCellSet.addCell(firstCell);
        vCellSet.addCell(secondCell);

        assertTrue("Value is not set in another cell of the set, should return true", vCellSet.canBeSet(3));
    }

    @Test
    public void cellSet_valueCanBeSet_False() {
        VerticalCellSet vCellSet = new VerticalCellSet(5);

        SettableCell firstCell = new SettableCell(1, 1);
        firstCell.setValue(2);
        SettableCell secondCell = new SettableCell(1, 2);
        vCellSet.addCell(firstCell);
        vCellSet.addCell(secondCell);

        assertFalse("Value is already set in another cell of the set, should return false", vCellSet.canBeSet(2));
    }

    @Test
    public void cellSet_setCurrentCombination() {
        VerticalCellSet vCellSet = new VerticalCellSet(5);

        SettableCell firstCell = new SettableCell(1, 1);
        SettableCell secondCell = new SettableCell(1, 2);
        vCellSet.addCell(firstCell);
        vCellSet.addCell(secondCell);

        vCellSet.updateMetadata();

        // Set a invalid combination:
        vCellSet.setCurrentlyUsedCombination(Arrays.asList(5, 6));
        assertNull("It should not be possible to set an invalid combination", vCellSet.getCurrentlyUsedCombination());

        // Set a valid combination:
        vCellSet.setCurrentlyUsedCombination(Arrays.asList(4, 1));
        assertEquals("A valid combination should be set", Arrays.asList(4, 1), vCellSet.getCurrentlyUsedCombination());
    }
}