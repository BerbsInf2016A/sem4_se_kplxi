package kakuro;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class PuzzleField {

    public List<Cell> getCells() {
        return cells;
    }

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
        Optional<Cell> cell = this.cells.stream().filter(c -> c.column == column && c.row == row).findFirst();
        if (cell.isPresent()) return cell.get();
        return null;
    }
    @Override
    public String toString(){
        StringBuilder sb = new StringBuilder();
        //TODO fix line seperator.
        this.cells.forEach(c -> sb.append(c.column + "|" + c.row + ": Type: " + c.getClass() + "\n" ));
        return sb.toString();
    }

    public void triggerSetUpdates(){
        for (HorizontalCellSet set : this.hSets ) {
            set.updateMetadata();
        }
        for (VerticalCellSet set: this.vSets ) {
            set.updateMetadata();
        }
    }

    public SetableCell getNextExmptyCell(SetableCell cell) {
        int columnOffset = cell.column;
        for (int row = cell.row; row < Configuration.instance.rowAndColumnSize; row++){
            for (int column = columnOffset; column < Configuration.instance.rowAndColumnSize; column++){
                Cell nextCell = this.getFieldCell(row, column);
                if (nextCell == cell) continue;
                if (nextCell instanceof SetableCell ){
                    SetableCell setableCell = (SetableCell) nextCell;
                    if (!setableCell.value.isPresent()) return setableCell;
                    continue;
                }
            }
            columnOffset = 0;
        }
        return null;
    }

    public void printCellValues() {
        for (Cell cell : this.cells ) {
            if (cell instanceof SetableCell) {
                SetableCell sCell = (SetableCell) cell;
                System.out.println(sCell);
            }

        }
    }

    public boolean validateCellValues() {
        for (HorizontalCellSet set : this.hSets ) {
            int setSum = 0;
            for (SetableCell cell : set.cells) {
                if (cell.value.isPresent()) {
                    setSum += cell.value.getAsInt();
                } else {
                    return false;
                }
            }
            if (setSum != set.maxValue) return false;
        }
        for (VerticalCellSet set : this.vSets ) {
            int setSum = 0;
            for (SetableCell cell : set.cells) {
                if (cell.value.isPresent()) {
                    setSum += cell.value.getAsInt();
                } else {
                    return false;
                }
            }
            if (setSum != set.maxValue) return false;
        }
        return true;
    }
}
