package kakuro;

import java.util.List;

public class HorizontalCellSet extends CellSet {
    public HorizontalCellSet(int maxValue, List<List<Integer>> possibleCombinations) {
        super(Orientation.HORIZONTAL, maxValue, possibleCombinations);
    }
}
