package kakuro;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Abstract class for a set of cells.
 */
public abstract class CellSet {

    /**
     * The maximum value of the set.
     */
    protected int maxValue;
    /**
     * The orientation of the set.
     */
    protected Orientation orientation;
    /**
     * The possible combinations of the set.
     */
    private List<List<Integer>> possibleCombinations;
    /**
     * The currently used combination.
     */
    private List<Integer> currentlyUsedCombination;
    /**
     * The cells of the set.
     */
    private List<SetableCell> cells;

    /**
     * Constructor for a cell set.
     *
     * @param orientation The orientation of the set.
     * @param maxValue    The maximum value of the set.
     */
    public CellSet(Orientation orientation, int maxValue) {
        this.orientation = orientation;
        this.maxValue = maxValue;
        this.possibleCombinations = new ArrayList<>();
        this.cells = new ArrayList<>();
    }

    /**
     * Get the possible combinations.
     *
     * @return The possible combinations.
     */
    public List<List<Integer>> getPossibleCombinations() {
        return possibleCombinations;
    }

    /**
     * Get the currently used combination.
     *
     * @return The currently used combination.
     */
    public List<Integer> getCurrentlyUsedCombination() {
        return currentlyUsedCombination;
    }

    /**
     * Set the currently used combination.
     *
     * @param combination The combination to set.
     */
    public void setCurrentlyUsedCombination(List<Integer> combination) {
        // Only allow the setting of a combination in this set.
        if (this.possibleCombinations.contains(combination)) {
            this.currentlyUsedCombination = new ArrayList<>(combination);
            if (Configuration.instance.printDebugMessages) {
                System.out.println("DEBUG: " + this.orientation + " set changed combination to "
                        + this.currentlyUsedCombination.stream().map(Object::toString).collect(Collectors.joining(", ")));
            }
            return;
        }
        this.currentlyUsedCombination = null;
    }

    /**
     * Get the cells of the set.
     *
     * @return The cells of the set.
     */
    public List<SetableCell> getCells() {
        return cells;
    }

    /**
     * Add a cell to the set.
     *
     * @param cell The cell to add.
     */
    protected abstract void addCell(SetableCell cell);

    /**
     * Updates the metadata of the set, like the possible combinations for the max value.
     */
    public void updateMetadata() {
        // Update available values
        List<List<Integer>> possiblePartitions = Partition.partition(this.maxValue);
        List<List<Integer>> partitionsWithCorrectLength = possiblePartitions.stream()
                .filter(t -> t.size() == this.cells.size()).collect(Collectors.toList());

        List<List<Integer>> partitionsWithValidCombinations = new ArrayList<>();
        for (List<Integer> combination : partitionsWithCorrectLength) {
            int distinctCount = (int) combination.stream().distinct().count();
            if (distinctCount == combination.size()) {
                if (combination.stream().max(Integer::max).get() <= 9)
                    partitionsWithValidCombinations.add(combination);
            }
        }
        this.possibleCombinations = partitionsWithValidCombinations;
        if (this.possibleCombinations.size() == 1) {
            this.currentlyUsedCombination = this.possibleCombinations.get(0);
        }
        if (Configuration.instance.printDebugMessages) {
            System.out.println("DEBUG: Set: possible combinations: " + this.possibleCombinations.size());
        }
    }

    /**
     * Reset the currently used combination.
     */
    public void resetCurrentlyUsedCombination() {
        this.currentlyUsedCombination = null;
    }

    /**
     * Check, if a value can be set to a cell of the set.
     *
     * @param possibleCandidate Value which should be set.
     * @return True if the value can be set, false if not.
     */
    public boolean canBeSet(int possibleCandidate) {
        int sum = 0;
        for (SetableCell cell : this.cells) {
            if (cell.getValue().isPresent()) {
                int value = cell.getValue().getAsInt();
                if (value == possibleCandidate) return false;
                sum += value;
            }
        }
        if (sum + possibleCandidate > this.maxValue) {
            return false;
        }
        return true;
    }
}
