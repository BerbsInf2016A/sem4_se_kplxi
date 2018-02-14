package kakuroui;

import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import kakuro.ICellValueChangedListener;
import kakuro.SetableCell;

public class SetableCellModel implements ICellValueChangedListener{
    private final int row;

    public int getRow() {
        return row;
    }

    public int getColumn() {
        return column;
    }

    private final int column;
    private SimpleStringProperty value = new SimpleStringProperty(this, "value");

    public SetableCellModel(SetableCell cell){
        cell.addValueChangedListener(this);
        this.row = cell.getRow();
        this.column = cell.getColumn();
        this.value.set("-");
    }

    public SimpleStringProperty valueProperty(){
        return value;
    }

    @Override
    public void cellValueChanged(SetableCell cell) {
        String cellValue = cell.getUIValue();
        Platform.runLater(new UpdateUiModelTask(this, cellValue));
    }
}