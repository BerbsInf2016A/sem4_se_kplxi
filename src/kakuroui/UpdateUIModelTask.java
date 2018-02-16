package kakuroui;

/**
 * A task to update the ui.
 */
public class UpdateUIModelTask implements Runnable {

    /**
     * The model of the cell.
     */
    SetableCellModel model;

    /**
     * The value of the cell.
     */
    String value;

    /**
     * Constructor for the UpdateUIModelTask.
     *
     * @param model The model to update.
     * @param value The new value of the model.
     */
    public UpdateUIModelTask(SetableCellModel model, String value) {
        this.model = model;
        this.value = value;
    }

    /**
     * Set the value on the model.
     */
    @Override
    public void run() {
        this.model.valueProperty().set(this.value);
    }
}
