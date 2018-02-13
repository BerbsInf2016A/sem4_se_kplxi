package kakuro;

import java.util.List;
import java.util.stream.Collectors;

public class Helpers {
    public static int getMaxValueFromConstraintCells(Cell cell, Orientation orientation) {
        if (cell instanceof SingleConstraintCell) {
            SingleConstraintCell constraint = (SingleConstraintCell) cell;
            if (constraint.getOrientation() == orientation) {
                return constraint.getMaxValue();
            }
        }

        if (cell instanceof DoubleConstraintCell) {
            DoubleConstraintCell doubleConstraintCell = (DoubleConstraintCell) cell;
            switch (orientation){

                case VERTICAL:
                    return doubleConstraintCell.getVerticalMax();
                case HORIZONTAL:
                    return doubleConstraintCell.getHorizontalMax();
            }
        }
        throw new RuntimeException("Invalid cell type.");
    }

    public static boolean isConstraintCellForOrientation(Cell cell, Orientation orientation) {
        if (cell instanceof SingleConstraintCell) {
            SingleConstraintCell constraint = (SingleConstraintCell) cell;
            if (constraint.getOrientation() == orientation) {
                return true;
            }
        }

        if (cell instanceof DoubleConstraintCell) {
            return true;
        }
        return false;
    }

    public static List<Integer> intersect(List<Integer> firstList, List<Integer> secondList) {
        return firstList.stream().filter(secondList::contains).collect(Collectors.toList());
    }
}
