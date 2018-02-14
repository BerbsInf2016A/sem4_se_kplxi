package kakuro;

import java.util.ArrayList;
import java.util.List;
import java.util.OptionalInt;

public class SetableCell extends Cell {
    private List<ICellValueChangedListener> listeners;


    public SetableCell(int xPos, int yPos) {
        super(xPos, yPos);
        this.listeners = new ArrayList<>();
        this.value = OptionalInt.empty();

    }


    OptionalInt value;

    public void setValue(int value){
        this.value = OptionalInt.of(value);
        this.valueChanged();
    }

    private void valueChanged(){
        for (ICellValueChangedListener listener : this.listeners ) {
            listener.cellValueChanged(this);
        }
        System.out.println("DEBUG: Cell value changed: row: " + this.row + " column: " + this.column + " value: " + this.value.getAsInt());
    }

    public HorizontalCellSet getHorizontalSet() {
        return hSet;
    }

    public VerticalCellSet getVerticalSet() {
        return vSet;
    }

    public void setHorizontalSet(HorizontalCellSet hSet) {
        this.hSet = hSet;
    }

    public void setVerticalSet(VerticalCellSet vSet) {
        this.vSet = vSet;
    }

    HorizontalCellSet hSet;
    VerticalCellSet vSet;

    public void addValueChangedListener(ICellValueChangedListener listener) {
        this.listeners.add(listener);
    }


    @Override
    public String toString(){
        String value = "null";
        if (this.value.isPresent()) {
            value = this.value.toString();
        }
        return super.toString() + " Value: " + value;
    }

    public String getUIValue() {
        if (this.value.isPresent()){
            return String.valueOf(this.value.getAsInt());
        }
        else return "-";
    }
}
