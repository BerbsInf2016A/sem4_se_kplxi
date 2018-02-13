package kakuro;

public class ConstraintCell extends Cell {
    private final Orientation orientation;
    private final int maxValue;

    public ConstraintCell(int xPos, int yPos, Orientation orientation, int maxValue) {
        super(xPos, yPos);
        this.orientation = orientation;
        this.maxValue = maxValue;
    }
}
