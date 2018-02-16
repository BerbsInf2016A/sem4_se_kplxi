package kakuroui;

import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.value.ChangeListener;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.stage.WindowEvent;
import kakuro.Configuration;
import kakuro.KakuroSolver;

public class KakuroController {

    private final Thread solverThread;
    public EventHandler<ActionEvent> startButtonEventHandler;
    public EventHandler<ActionEvent> exitButtonEventHandler;
    public EventHandler<WindowEvent> windowIsClosedEventHandler;
    public ChangeListener<? super Number> sliderChangeListener;
    private SimpleBooleanProperty canStart = new SimpleBooleanProperty(this, "canStart");

    public KakuroController(KakuroSolver kakuroSolver) {

        this.solverThread = new Thread() {
            public void run() {
                kakuroSolver.solveField();
            }
        };

        this.startButtonEventHandler =
                event -> this.startSolvingThread();

        this.exitButtonEventHandler =
                event -> this.close();

        this.sliderChangeListener = (ChangeListener<Number>) (observable, oldValue, newValue)
                -> Configuration.instance.stepDelayInMilliseconds = newValue.intValue();

        this.windowIsClosedEventHandler = event -> this.close();

        this.canStart.set(true);

    }

    public SimpleBooleanProperty canStartProperty() {
        return canStart;
    }

    public void close() {
        this.stopSolvingThread();
        System.exit(0);
    }

    private void stopSolvingThread() {
        if (this.solverThread.isAlive())
            this.solverThread.interrupt();

    }

    private void startSolvingThread() {
        if (!this.solverThread.isAlive()) {
            this.solverThread.start();
            this.canStart.set(false);
        }
    }

}
