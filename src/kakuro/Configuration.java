package kakuro;

/**
 * Configuration for the Kakuro solver.
 */
public enum Configuration {
    instance;

    /**
     * The row and column size of the play field.
     */
    public final int rowAndColumnSize = 9;
    /**
     * The simulation step delay.
     */
    public int stepDelayInMilliseconds = 50;
    /**
     * Flag to enable debug messages.
     */
    public boolean printDebugMessages = true;
}
