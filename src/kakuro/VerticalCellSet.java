package kakuro;

import java.util.List;

public class VerticalCellSet extends CellSet {
    public VerticalCellSet(Orientation orientation, int maxValue, List<List<Integer>> possibleCombinations) {
        super(Orientation.VERTICAL, maxValue, possibleCombinations);
    }
}
