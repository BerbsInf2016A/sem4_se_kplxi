package kakuro;

import java.util.List;
import java.util.stream.Collectors;

public class VerticalCellSet extends CellSet {
    public VerticalCellSet( int maxValue) {
        super(Orientation.VERTICAL, maxValue);
    }

    @Override
    protected void addCell(SetableCell cell) {
        cell.addValueChangedListener(this);
        this.cells.add(cell);
        cell.setVerticalSet(this);
    }

    public boolean canBeUsedElseWhere(SetableCell cell, int possibleCandidate) {
        List<SetableCell> otherCells = this.cells.stream().filter(c -> c != cell).collect(Collectors.toList());
        for (SetableCell otherCell : otherCells ) {
            List<List<Integer>> horizontalCombinations = otherCell.getHorizontalSet().possibleCombinations;
            for (List<Integer> combination : horizontalCombinations ) {
                boolean canBeUsed = combination.contains(possibleCandidate);
                if (canBeUsed) return true;
            }
        }
        return false;
    }
}
