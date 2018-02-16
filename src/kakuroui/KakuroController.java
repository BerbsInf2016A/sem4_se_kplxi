package kakuroui;

import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.value.ChangeListener;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.stage.WindowEvent;
import kakuro.Configuration;
import kakuro.KakuroSolver;

/**
 * A controller for the kakuro ui.
 */
public class KakuroController {

    /**
     * The thread to run the solver algorithm on.
     */
    private final Thread solverThread;

    /**
     * EventHandler for the start button.
     */
    public EventHandler<ActionEvent> startButtonEventHandler;

    /**
     * EventHandler for the exit button.
     */
    public EventHandler<ActionEvent> exitButtonEventHandler;
    /**
     * EventHandler for the window closed event.
     */
    public EventHandler<WindowEvent> windowIsClosedEventHandler;

    /**
     * EventHandler for the change of the slider.
     */
    public ChangeListener<? super Number> sliderChangeListener;

    /**
     * Property to bind the start button disabled property to.
     */
    private SimpleBooleanProperty canStart = new SimpleBooleanProperty(this, "canStart");

    /**
     * Constructor for the controller.
     *
     * @param kakuroSolver The solver algorithm.
     */
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

    /**
     * Get the property to bind the start button disabled property to.
     * @return
     */
    public SimpleBooleanProperty canStartProperty() {
        return canStart;
    }

    /**
     * Ends the execution.
     */
    public void close() {
        this.stopSolvingThread();
        System.exit(0);
    }

    /**
     * Stops the thread of the solver algorithm.
     */
    private void stopSolvingThread() {
        if (this.solverThread.isAlive())
            this.solverThread.interrupt();

    }

    /**
     * Start the solving algorithm.
     */
    private void startSolvingThread() {
        if (!this.solverThread.isAlive()) {
            this.solverThread.start();
            this.canStart.set(false);
        }
    }

}
