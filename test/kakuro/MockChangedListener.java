package kakuro;

public class MockChangedListener implements ICellValueChangedListener {

    private boolean cellValueHasChanged = false;
    private SettableCell changedCell;

    public boolean isCellValueHasChanged() {
        return cellValueHasChanged;
    }

    public SettableCell getChangedCell() {
        return changedCell;
    }

    @Override
    public void cellValueChanged(SettableCell cell) {
        this.cellValueHasChanged = true;
        this.changedCell = cell;
    }
}
