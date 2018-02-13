package kakuro;

import java.util.List;
import java.util.stream.Collectors;

public class HorizontalCellSet extends CellSet {
    public HorizontalCellSet(int maxValue) {
        super(Orientation.HORIZONTAL, maxValue);
    }

    @Override
    protected void addCell(SetableCell cell) {
        cell.addValueChangedListener(this);
        this.cells.add(cell);
        cell.setHorizontalSet(this);
    }

    public boolean canBeUsedElseWhere(SetableCell cell, int possibleCandidate) {
        List<SetableCell> otherCells = this.cells.stream().filter(c -> c != cell).collect(Collectors.toList());
        for (SetableCell otherCell : otherCells ) {
            List<List<Integer>> verticalCombinations = otherCell.getVerticalSet().possibleCombinations;
            for (List<Integer> combination : verticalCombinations ) {
                boolean canBeUsed = combination.contains(possibleCandidate);
                if (canBeUsed) return true;
            }
        }
        return false;
    }
}
