package kakuro;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class PuzzleField {

    public PuzzleField(){
        this.cells = new ArrayList<>();
        this.hSets = new ArrayList<>();
        this.vSets = new ArrayList<>();
    }
    List<Cell> cells;

    List<HorizontalCellSet> hSets;
    List<VerticalCellSet> vSets;

    public Cell getFieldCell(int row, int column){
        if (row > Configuration.instance.rowAndColumnSize || column > Configuration.instance.rowAndColumnSize || row < 0 || column < 0) {
            throw new RuntimeException("Invalid row column combination. row: " + row + " column: " + column);
        }
        Optional<Cell> cell = this.cells.stream().filter(c -> c.xPos == row && c.yPos == column).findFirst();
        if (cell.isPresent()) return cell.get();
        return null;
    }
    @Override
    public String toString(){
        StringBuilder sb = new StringBuilder();
        //TODO fix line seperator.
        this.cells.forEach(c -> sb.append(c.xPos + "|" + c.yPos + ": Type: " + c.getClass() + "\n" ));
        return sb.toString();
    }

}
