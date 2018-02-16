package kakuro;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class SetableCellTest {
    @Before
    public void before() {
        Configuration.instance.printDebugMessages = false;
        Configuration.instance.stepDelayInMilliseconds = 0;
    }

    @Test
    public void addValueChangedListener() {
        MockChangedListener listener = new MockChangedListener();
        SetableCell testCell = new SetableCell(1, 1);
        testCell.addValueChangedListener(listener);

        testCell.setValue(4);

        assertTrue("Listener should be called", listener.isCellValueHasChanged());
        assertEquals("Listener should be called with correct cell value"
                , testCell, listener.getChangedCell());
    }

    @Test
    public void SetableCell_toString() {
        SetableCell emptyCell = new SetableCell(1, 1);
        assertEquals("Row: 1 Column: 1 Value: null", emptyCell.toString());

        SetableCell cellWithValue = new SetableCell(1, 1);
        cellWithValue.setValue(4);
        assertEquals("Row: 1 Column: 1 Value: 4", cellWithValue.toString());
    }

    @Test
    public void SetableCell_getUIValue() {
        SetableCell emptyCell = new SetableCell(1, 1);
        assertEquals("-", emptyCell.getUIValue());

        SetableCell cellWithValue = new SetableCell(1, 1);
        cellWithValue.setValue(4);
        assertEquals("4", cellWithValue.getUIValue());
    }

}