package kakuroui;

public class UpdateUiModelTask implements Runnable {

    SetableCellModel model;
    String value;

    public UpdateUiModelTask(SetableCellModel model, String value) {
        this.model = model;
        this.value = value;
    }

    @Override
    public void run() {
        this.model.valueProperty().set(this.value);
    }
}
