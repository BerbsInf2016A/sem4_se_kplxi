package kakuro;

public class DoubleConstraintCell extends ConstraintCell {
    private final int horizontalMax;

    public int getHorizontalMax() {
        return horizontalMax;
    }

    public int getVerticalMax() {
        return verticalMax;
    }

    private final int verticalMax;

    public DoubleConstraintCell(int xPos, int yPos, int horizontalMax, int verticalMax) {
        super(xPos, yPos);
        this.horizontalMax = horizontalMax;
        this.verticalMax = verticalMax;
    }
}
