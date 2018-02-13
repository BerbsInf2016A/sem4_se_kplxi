package kakuro;

import java.util.ArrayList;
import java.util.List;
import java.util.OptionalInt;

public class SetableCell extends Cell {
    private List<ICellValueChangedListener> listeners;

    public SetableCell(int xPos, int yPos) {
        super(xPos, yPos);
        this.listeners = new ArrayList<>();

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
    }

    public void sethSet(HorizontalCellSet hSet) {
        this.hSet = hSet;
    }

    public void setvSet(VerticalCellSet vSet) {
        this.vSet = vSet;
    }

    HorizontalCellSet hSet;
    VerticalCellSet vSet;

    public void addValueChangedListener(ICellValueChangedListener listener) {
        this.listeners.add(listener);
    }


}
