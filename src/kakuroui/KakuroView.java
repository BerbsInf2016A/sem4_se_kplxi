package kakuroui;

import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.StrokeType;
import javafx.scene.text.Text;
import kakuro.BlockingCell;
import kakuro.Cell;
import kakuro.Configuration;
import kakuro.ConstraintCell;
import kakuro.DoubleConstraintCell;
import kakuro.SingleConstraintCell;

import java.util.Optional;

/**
 * A view for the kakuro solving algorithm
 */
class KakuroView {

    /**
     * The controller for the view.
     */
    private final KakuroController controller;

    /**
     * The model for the view.
     */
    private final KakuroModel model;


    /**
     * Constructor for the view.
     *
     * @param controller The controller for the view.
     * @param model      The model for the view.
     */
    public KakuroView(KakuroController controller, KakuroModel model) {
        this.controller = controller;
        this.model = model;
    }

    /**
     * Generate the scene for the ui.
     *
     * @return The generated scene.
     */
    public Scene generateUIScene() {
        GridPane root = new GridPane();
        root.setPadding(new Insets(10, 10, 10, 10));

        int rowOffset = this.addCellRectanglesToThePane(root, 0);
        // Insert a empty row.
        root.addRow(rowOffset, new Text(""));
        rowOffset++;
        rowOffset = this.addButtonControlsToThePane(root, rowOffset);
        // Insert a empty row.
        root.addRow(rowOffset, new Text(""));
        rowOffset++;
        this.addSimulationSliderToThePane(root, rowOffset);

        return new Scene(root, 610, 725);
    }

    /**
     * Add the rectangles for the cells to the view.
     *
     * @param root The root element to add the ui elements to.
     */
    private int addCellRectanglesToThePane(GridPane root, int rowOffset) {
        int playFieldSize = Configuration.instance.rowAndColumnSize;
        for (int row = 0; row < playFieldSize; row++) {
            for (int col = 0; col < playFieldSize; col++) {

                Rectangle rectangle = new Rectangle();

                StackPane stack = this.bindKakuroCellToRectangle(rectangle, row, col);

                root.add(stack, col, row + rowOffset);
                rectangle.widthProperty().setValue(65);
                rectangle.heightProperty().setValue(65);
                rectangle.setStroke(Color.WHITE);
                rectangle.setStrokeType(StrokeType.INSIDE);
            }
        }
        return playFieldSize + rowOffset + 1;
    }

    /**
     * Gets cell for the specified position.
     *
     * @param row    The row position.
     * @param column The column position.
     * @return The cell at the position.
     */
    private Cell getFieldCell(int row, int column) {
        if (row > Configuration.instance.rowAndColumnSize || column > Configuration.instance.rowAndColumnSize || row < 0 || column < 0) {
            throw new RuntimeException("Invalid row column combination. row: " + row + " column: " + column);
        }
        Optional<Cell> cell = this.model.getRawCells().stream().filter(c -> c.getColumn() == column && c.getRow() == row).findFirst();
        return cell.orElse(null);
    }

    /**
     * Gets the settable cell for the specified position.
     *
     * @param row    The row position.
     * @param column The column position.
     * @return The cell at the position.
     */
    private SettableCellModel getModelCell(int row, int column) {
        if (row > Configuration.instance.rowAndColumnSize || column > Configuration.instance.rowAndColumnSize || row < 0 || column < 0) {
            throw new RuntimeException("Invalid row column combination. row: " + row + " column: " + column);
        }
        Optional<SettableCellModel> cell = this.model.getSettableCellModels().stream().filter(c -> c.getColumn() == column && c.getRow() == row).findFirst();
        return cell.orElse(null);
    }

    /**
     * Bind the cells of the kakuro solving algorithm to the ui rectangles.
     *
     * @param rectangle The rectangle which should be bound.
     * @param row       The row index.
     * @param col       The column index.
     * @return A stack pane containing the ui elements.
     */
    private StackPane bindKakuroCellToRectangle(Rectangle rectangle, int row, int col) {
        Cell fieldCell = this.getFieldCell(row, col);
        StackPane stack = new StackPane();

        if (fieldCell instanceof ConstraintCell || fieldCell instanceof BlockingCell) {
            rectangle.setFill(Color.web("0x0F1112", 1.0));
            stack.getChildren().add(rectangle);
            if (fieldCell instanceof ConstraintCell) {
                UIGenerationHelpers.generateDefaultConstraintCell(stack, rectangle);
                if (fieldCell instanceof SingleConstraintCell) {
                    SingleConstraintCell single = (SingleConstraintCell) fieldCell;
                    UIGenerationHelpers.generateDefaultSingleConstraintCell(stack, single);
                }
                if (fieldCell instanceof DoubleConstraintCell) {
                    DoubleConstraintCell doubleConstraint = (DoubleConstraintCell) fieldCell;
                    UIGenerationHelpers.generateDefaultDoubleConstraintCell(stack, doubleConstraint);
                }
            }

        } else {
            SettableCellModel model = this.getModelCell(row, col);
            UIGenerationHelpers.generateAndBindSettableCellRectangle(stack, rectangle, model);
        }
        return stack;
    }

    /**
     * Add a slider for the simulation speed to the ui.
     *
     * @param root The root ui element to add the slider to.
     * @param row  The row in which the slider should be placed.
     */
    private void addSimulationSliderToThePane(GridPane root, int row) {
        Label descriptionLabel = new Label("Simulation-Delay:");

        Label valueLabel = new Label();
        valueLabel.textProperty().setValue(String.valueOf(Configuration.instance.stepDelayInMilliseconds + " ms"));

        Slider slider = UIGenerationHelpers.generateDefaultSlider();
        GridPane.setFillWidth(slider, true);

        // Update the controller and the solving algorithm.
        slider.valueProperty().addListener(this.controller.sliderChangeListener);

        // Update the ui.
        slider.valueProperty().addListener((observable, oldValue, newValue)
                -> valueLabel.textProperty().setValue(String.valueOf(newValue.intValue() + " ms")));

        root.add(descriptionLabel, 1, row, 4, 1);
        root.add(valueLabel, 6, row, 1, 1);
        root.add(slider, 1, (row + 1), Configuration.instance.rowAndColumnSize - 2, 1);
    }

    /**
     * Add the button controls to the ui.
     *
     * @param root The ui root element to add the new ui elements to.
     * @param row  The row in which the buttons should be placed.
     */
    private int addButtonControlsToThePane(GridPane root, int row) {
        Button startButton = UIGenerationHelpers.generateDefaultButton("Start");

        startButton.setOnAction(this.controller.startButtonEventHandler);
        startButton.disableProperty().bind(this.controller.canStartProperty().not());


        root.add(startButton, 1, row, 3, 1);


        Button exitButton = UIGenerationHelpers.generateDefaultButton("Close");

        exitButton.setOnAction(this.controller.exitButtonEventHandler);
        root.add(exitButton, 5, row, 3, 1);
        return row + 1;
    }
}
