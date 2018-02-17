package kakuroui;

/**
 * A task to update the ui.
 */
class UpdateUIModelTask implements Runnable {

    /**
     * The model of the cell.
     */
    private final SettableCellModel model;

    /**
     * The value of the cell.
     */
    private final String value;

    /**
     * Constructor for the UpdateUIModelTask.
     *
     * @param model The model to update.
     * @param value The new value of the model.
     */
    public UpdateUIModelTask(SettableCellModel model, String value) {
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
