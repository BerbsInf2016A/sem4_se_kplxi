package kakuro;

import java.util.List;
import java.util.stream.Collectors;

class Helpers {
    /**
     * Get the maximum value from a constraint cell.
     *
     * @param cell        The cell.
     * @param orientation The orientation the max value should be for.
     * @return The maximum value.
     */
    public static int getMaxValueFromConstraintCells(Cell cell, Orientation orientation) {
        if (cell instanceof SingleConstraintCell) {
            SingleConstraintCell constraint = (SingleConstraintCell) cell;
            if (constraint.getOrientation() == orientation) {
                return constraint.getMaxValue();
            }
        }

        if (cell instanceof DoubleConstraintCell) {
            DoubleConstraintCell doubleConstraintCell = (DoubleConstraintCell) cell;
            switch (orientation) {

                case VERTICAL:
                    return doubleConstraintCell.getVerticalMax();
                case HORIZONTAL:
                    return doubleConstraintCell.getHorizontalMax();
            }
        }
        throw new RuntimeException("Invalid cell type.");
    }

    /**
     * Checks, if a cell is a constraint cell for a specified orientation.
     *
     * @param cell        The cell to check.
     * @param orientation The orientation.
     * @return True if the cell is a constraint cell for the orientation, false if not.
     */
    public static boolean isConstraintCellForOrientation(Cell cell, Orientation orientation) {
        if (cell instanceof SingleConstraintCell) {
            SingleConstraintCell constraint = (SingleConstraintCell) cell;
            if (constraint.getOrientation() == orientation) {
                return true;
            }
        }

        return cell instanceof DoubleConstraintCell;
    }

    /**
     * Get the intersection of two lists.
     *
     * @param firstList  The first list.
     * @param secondList The second list.
     * @return The intersection of the two lists.
     */
    public static List<Integer> intersect(List<Integer> firstList, List<Integer> secondList) {
        return firstList.stream().filter(secondList::contains).collect(Collectors.toList());
    }
}
