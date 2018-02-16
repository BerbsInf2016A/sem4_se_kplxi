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
import kakuro.BlockingCell;
import kakuro.Cell;
import kakuro.Configuration;
import kakuro.ConstraintCell;
import kakuro.DoubleConstraintCell;
import kakuro.SingleConstraintCell;

import java.util.Optional;

public class KakuroView {

    final int playFieldSize = Configuration.instance.rowAndColumnSize;

    private final KakuroController controller;
    private KakuroModel model;


    public KakuroView(KakuroController controller, KakuroModel model) {

        this.controller = controller;
        this.model = model;
    }

    public Scene generateUIScene() {
        GridPane root = new GridPane();
        root.setPadding(new Insets(10, 10, 10, 10));

        this.addRectanglesToThePane(root);
        this.addButtonControlsToThePane(root, Configuration.instance.rowAndColumnSize + 1);
        this.addSimulationSliderToThePane(root, Configuration.instance.rowAndColumnSize + 2);

        return new Scene(root, 610, 700);
    }

    private void addRectanglesToThePane(GridPane root) {
        for (int row = 0; row < playFieldSize; row++) {
            for (int col = 0; col < playFieldSize; col++) {

                Rectangle rectangle = new Rectangle();

                StackPane stack = this.bindKakuroCellToRectangle(rectangle, row, col);

                root.add(stack, col, row);
                rectangle.widthProperty().setValue(65);
                rectangle.heightProperty().setValue(65);
                rectangle.setStroke(Color.WHITE);
                rectangle.setStrokeType(StrokeType.INSIDE);
            }
        }
    }

    private Cell getFieldCell(int row, int column) {
        if (row > Configuration.instance.rowAndColumnSize || column > Configuration.instance.rowAndColumnSize || row < 0 || column < 0) {
            throw new RuntimeException("Invalid row column combination. row: " + row + " column: " + column);
        }
        Optional<Cell> cell = this.model.getRawCells().stream().filter(c -> c.getColumn() == column && c.getRow() == row).findFirst();
        if (cell.isPresent()) return cell.get();
        return null;
    }

    private SetableCellModel getModelCell(int row, int column) {
        if (row > Configuration.instance.rowAndColumnSize || column > Configuration.instance.rowAndColumnSize || row < 0 || column < 0) {
            throw new RuntimeException("Invalid row column combination. row: " + row + " column: " + column);
        }
        Optional<SetableCellModel> cell = this.model.getSetableCellModels().stream().filter(c -> c.getColumn() == column && c.getRow() == row).findFirst();
        if (cell.isPresent()) return cell.get();
        return null;
    }

    private StackPane bindKakuroCellToRectangle(Rectangle rectangle, int row, int col) {
        Cell fieldCell = this.getFieldCell(row, col);
        StackPane stack = new StackPane();

        if (fieldCell instanceof ConstraintCell || fieldCell instanceof BlockingCell) {
            rectangle.setFill(Color.BLACK);
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
            SetableCellModel model = this.getModelCell(row, col);
            UIGenerationHelpers.generateAndBindSetableCellRectangle(stack, rectangle, model);
        }
        return stack;
    }


    private void addSimulationSliderToThePane(GridPane root, int row) {

        Label descriptionLabel = new Label("Simulation-Delay:");

        Label valueLabel = new Label();
        valueLabel.textProperty().setValue(String.valueOf(Configuration.instance.stepDelayInMilliseconds + " ms"));

        Slider slider = UIGenerationHelpers.generateDefaultSlider();

        // Update the controller and the solving algorithm.
        slider.valueProperty().addListener(this.controller.sliderChangeListener);

        // Update the ui.
        slider.valueProperty().addListener((observable, oldValue, newValue)
                -> valueLabel.textProperty().setValue(String.valueOf(newValue.intValue() + " ms")));

        root.add(descriptionLabel, 1, row, 4, 1);
        root.add(valueLabel, 6, row, 1, 1);
        root.add(slider, 1, (row + 1), Configuration.instance.rowAndColumnSize - 2, 1);
    }

    private void addButtonControlsToThePane(GridPane root, int row) {
        Button startButton = UIGenerationHelpers.generateDefaultButton("Start");

        startButton.setOnAction(this.controller.startButtonEventHandler);
        startButton.disableProperty().bind(this.controller.canStartProperty().not());


        root.add(startButton, 1, row, 3, 1);


        Button exitButton = UIGenerationHelpers.generateDefaultButton("Close");

        exitButton.setOnAction(this.controller.exitButtonEventHandler);
        root.add(exitButton, 5, row, 3, 1);
    }

}
