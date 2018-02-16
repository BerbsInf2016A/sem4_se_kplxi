package kakuroui;

import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import kakuro.ICellValueChangedListener;
import kakuro.SetableCell;

public class SetableCellModel implements ICellValueChangedListener {
    private final int row;
    private final int column;
    private SimpleStringProperty value = new SimpleStringProperty(this, "value");

    public SetableCellModel(SetableCell cell) {
        cell.addValueChangedListener(this);
        this.row = cell.getRow();
        this.column = cell.getColumn();
        this.value.set("-");
    }

    public int getRow() {
        return row;
    }

    public int getColumn() {
        return column;
    }

    public SimpleStringProperty valueProperty() {
        return value;
    }

    @Override
    public void cellValueChanged(SetableCell cell) {
        String cellValue = cell.getUIValue();
        Platform.runLater(new UpdateUiModelTask(this, cellValue));
    }
}