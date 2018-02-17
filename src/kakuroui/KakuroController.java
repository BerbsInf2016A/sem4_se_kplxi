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
class KakuroController {

    /**
     * EventHandler for the start button.
     */
    public final EventHandler<ActionEvent> startButtonEventHandler;
    /**
     * EventHandler for the exit button.
     */
    public final EventHandler<ActionEvent> exitButtonEventHandler;
    /**
     * EventHandler for the window closed event.
     */
    public final EventHandler<WindowEvent> windowIsClosedEventHandler;
    /**
     * EventHandler for the change of the slider.
     */
    public final ChangeListener<? super Number> sliderChangeListener;
    /**
     * The thread to run the solver algorithm on.
     */
    private final Thread solverThread;
    /**
     * Property to bind the start button disabled property to.
     */
    private final SimpleBooleanProperty canStart = new SimpleBooleanProperty(this, "canStart");

    /**
     * Constructor for the controller.
     *
     * @param kakuroSolver The solver algorithm.
     */
    public KakuroController(KakuroSolver kakuroSolver) {

        this.solverThread = new Thread(kakuroSolver::solveField);

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
     *
     * @return The property to bind to.
     */
    public SimpleBooleanProperty canStartProperty() {
        return canStart;
    }

    /**
     * Ends the execution.
     */
    private void close() {
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
