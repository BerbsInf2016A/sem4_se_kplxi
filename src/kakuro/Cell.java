package kakuro;

public abstract class Cell {

    public Cell(int column, int row) {
        this.column = column;
        this.row = row;
    }

    public int getColumn() {
        return column;
    }

    public int getRow() {
        return row;
    }

    int column;

    int row;

    @Override
    public String toString(){
        return "Row: " + this.row + " Column: " + this.column;
    }


}
