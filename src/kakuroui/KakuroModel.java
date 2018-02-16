package kakuroui;

import kakuro.Cell;
import kakuro.SetableCell;

import java.util.ArrayList;
import java.util.List;

public class KakuroModel {
    private final List<SetableCellModel> setableCellModels;
    private final List<Cell> rawCells;

    public KakuroModel(List<Cell> rawCells) {
        this.rawCells = rawCells;
        this.setableCellModels = new ArrayList<>();
        this.rawCells.stream().filter(cell -> cell instanceof SetableCell)
                .forEach(cell -> this.setableCellModels.add(new SetableCellModel((SetableCell) cell)));
    }

    public List<SetableCellModel> getSetableCellModels() {
        return setableCellModels;
    }

    public List<Cell> getRawCells() {
        return rawCells;
    }
}
