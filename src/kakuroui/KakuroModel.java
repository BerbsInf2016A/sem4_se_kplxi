package kakuroui;

import kakuro.Cell;
import kakuro.SetableCell;

import java.util.ArrayList;
import java.util.List;

/**
 * A model for the kakuro ui.
 */
public class KakuroModel {
    /**
     * A list of all setable cell models to bind the ui to.
     */
    private final List<SetableCellModel> setableCellModels;

    /**
     * All cells of the playing field.
     */
    private final List<Cell> rawCells;

    /**
     * Constructor for the kakuro model.
     *
     * @param rawCells The cells of the playing field.
     */
    public KakuroModel(List<Cell> rawCells) {
        this.rawCells = rawCells;
        this.setableCellModels = new ArrayList<>();
        this.rawCells.stream().filter(cell -> cell instanceof SetableCell)
                .forEach(cell -> this.setableCellModels.add(new SetableCellModel((SetableCell) cell)));
    }

    /**
     * Get a list of all setable cell models.
     *
     * @return A list of all setable cell models.
     */
    public List<SetableCellModel> getSetableCellModels() {
        return setableCellModels;
    }

    /**
     * Get a list of all cells.
     *
     * @return A list of all cells.
     */
    public List<Cell> getRawCells() {
        return rawCells;
    }
}
