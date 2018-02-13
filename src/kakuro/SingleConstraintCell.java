package kakuro;

public class SingleConstraintCell extends ConstraintCell {
    public Orientation getOrientation() {
        return orientation;
    }

    public int getMaxValue() {
        return maxValue;
    }

    private final Orientation orientation;
    private final int maxValue;

    public SingleConstraintCell(int xPos, int yPos, Orientation orientation, int maxValue) {
        super(xPos, yPos);
        this.orientation = orientation;
        this.maxValue = maxValue;
    }
}
