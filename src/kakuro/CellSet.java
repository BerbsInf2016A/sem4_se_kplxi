package kakuro;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public abstract class CellSet implements ICellValueChangedListener{

    public CellSet(Orientation orientation, int maxValue) {
        this.orientation = orientation;
        this.maxValue = maxValue;
        this.possibleCombinations = new ArrayList<>();
        this.cells = new ArrayList<>();
    }

    private Orientation orientation;
    protected List<SetableCell> cells;

    int maxValue;

    public List<List<Integer>> possibleCombinations;

    public List<Integer> usedCombination;


    protected abstract void addCell(SetableCell cell);

    public void cellValueChanged(SetableCell cell) {
        int value = cell.value.getAsInt();
        // TODO: Remove
    }
    public void updateMetadata() {
        // Update available values
        ArrayList<ArrayList<Integer>> possiblePartitions = Partition.partition(this.maxValue);
        List<ArrayList<Integer>> partitionsWithCorrectLength = possiblePartitions.stream().filter(t -> t.size() == this.cells.size()).collect(Collectors.toList());

        List<List<Integer>> partitionsWithValidCombinations = new ArrayList<>();
        for (List<Integer> combination : partitionsWithCorrectLength ) {
            int distinctCount = (int)combination.stream().distinct().count();
            if (distinctCount == combination.size()) {
                if (combination.stream().max(Integer::max).get() <= 9)
                partitionsWithValidCombinations.add(combination);
            }
        }
        this.possibleCombinations = partitionsWithValidCombinations;
        if (this.possibleCombinations.size() == 1){
            this.usedCombination = this.possibleCombinations.get(0);
        }
        System.out.println("DEBUG: Set: possible combinations: " + this.possibleCombinations.size());
    }

    public void resetUsedCombination() {
        this.usedCombination = null;
    }

    public void setUsedCombination(List<Integer> hCombination) {
        this.usedCombination = new ArrayList<>(hCombination);
    }

    public boolean valueCanBeUsed(int possibleCandidate) {
        int sum = 0;
        for (SetableCell cell : this.cells ) {
            if (cell.value.isPresent()) {
                int value =cell.value.getAsInt();
                if (value == possibleCandidate) return false;
                sum += value;
            }
        }
        if (sum  + possibleCandidate > this.maxValue) {
            return false;
        }
        return true;
    }

    public boolean canBeSet(int possibleCandidate) {
        int sum = 0;
        for (SetableCell cell : this.cells ) {
            if (cell.value.isPresent()) {
                int value =cell.value.getAsInt();
                if (value == possibleCandidate) return false;
                sum += value;
            }
        }
        if (sum  + possibleCandidate > this.maxValue) {
            return false;
        }
        return true;
    }
}
