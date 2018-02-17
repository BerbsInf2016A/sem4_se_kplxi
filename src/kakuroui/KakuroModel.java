package kakuroui;

import kakuro.Cell;
import kakuro.SettableCell;

import java.util.ArrayList;
import java.util.List;

/**
 * A model for the kakuro ui.
 */
class KakuroModel {
    /**
     * A list of all settable cell models to bind the ui to.
     */
    private final List<SettableCellModel> settableCellModels;

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
        this.settableCellModels = new ArrayList<>();
        this.rawCells.stream().filter(cell -> cell instanceof SettableCell)
                .forEach(cell -> this.settableCellModels.add(new SettableCellModel((SettableCell) cell)));
    }

    /**
     * Get a list of all settable cell models.
     *
     * @return A list of all settable cell models.
     */
    public List<SettableCellModel> getSettableCellModels() {
        return settableCellModels;
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
