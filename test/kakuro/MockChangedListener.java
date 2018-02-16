package kakuro;

public class MockChangedListener implements ICellValueChangedListener {

    boolean cellValueHasChanged = false;
    SetableCell changedCell;

    public boolean isCellValueHasChanged() {
        return cellValueHasChanged;
    }

    public SetableCell getChangedCell() {
        return changedCell;
    }

    @Override
    public void cellValueChanged(SetableCell cell) {
        this.cellValueHasChanged = true;
        this.changedCell = cell;
    }
}
