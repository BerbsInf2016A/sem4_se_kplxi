package kakuro;

public class DoubleConstraintCell extends Cell {
    private final int horizontalMax;
    private final int verticalMax;

    public DoubleConstraintCell(int xPos, int yPos, int horizontalMax, int verticalMax) {
        super(xPos, yPos);
        this.horizontalMax = horizontalMax;
        this.verticalMax = verticalMax;
    }
}
