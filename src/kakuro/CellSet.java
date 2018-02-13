package kakuro;

import java.util.List;

public class CellSet implements ICellValueChangedListener{


    public CellSet(Orientation orientation, int maxValue, List<List<Integer>> possibleCombinations) {
        this.orientation = orientation;
        this.maxValue = maxValue;
        this.possibleCombinations = possibleCombinations;
    }

    Orientation orientation;
    List<Cell> cells;

    int maxValue;

    public List<List<Integer>> possibleCombinations;

    public List<Integer> usedCombination;

    public List<Integer> availableValues;

    public void addCell(Cell cell) {
        if (cell instanceof SetableCell) {
            SetableCell setableCell = (SetableCell) cell;
            setableCell.addValueChangedListener(this);
        }
    }

    public void cellValueChanged(SetableCell cell) {
        // Update available values
    }
}
