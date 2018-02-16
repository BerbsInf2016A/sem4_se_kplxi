package kakuro;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertTrue;

public class KakuroSolverTest {
    @Before
    public void before() {
        Configuration.instance.printDebugMessages = false;
        Configuration.instance.stepDelayInMilliseconds = 0;
    }

    @Test
    public void solveField() {
        Configuration.instance.stepDelayInMilliseconds = 0;
        KakuroSolver solver = new KakuroSolver();
        solver.generateField();
        solver.solveField();

        PuzzleField field = solver.getField();

        assertTrue("Cells should be valid", field.validateCellValues());
    }

}