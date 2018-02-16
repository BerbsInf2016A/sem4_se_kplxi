package kakuroui;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import kakuro.Configuration;
import kakuro.DoubleConstraintCell;
import kakuro.SingleConstraintCell;

/**
 * A class containing helper methods for the creation of ui elements.
 */
public class UIGenerationHelpers {
    /**
     * Generate a slider with default values.
     *
     * @return The created slider.
     */
    public static Slider generateDefaultSlider() {
        Slider slider = new Slider();

        slider.setMin(0);
        slider.setMax(1000);
        slider.setValue(Configuration.instance.stepDelayInMilliseconds);
        slider.setShowTickLabels(true);
        slider.setShowTickMarks(true);
        slider.setMajorTickUnit(50);
        slider.setMinorTickCount(5);
        slider.setBlockIncrement(100);
        slider.setMaxWidth(Double.MAX_VALUE);
        slider.setMaxHeight(Double.MAX_VALUE);

        return slider;
    }

    /**
     * Generate a button with default values.
     *
     * @param text The text on the button.
     * @return The created Button.
     */
    public static Button generateDefaultButton(String text) {
        Button button = new Button(text);
        button.setMaxWidth(Double.MAX_VALUE);
        button.setMaxHeight(Double.MAX_VALUE);

        return button;
    }

    /**
     * Generate a label for setable cell with default values and bind it to the model.
     *
     * @param model The model to bind the label to.
     * @return The created label.
     */
    private static Label generateAndBindDefaultSetableCellTextLabel(SetableCellModel model) {
        Label text = new Label();
        text.setFont(new Font("Arial", 30));
        text.textProperty().bind(model.valueProperty());
        return text;
    }

    /**
     * Generate the label for a model and bind it to the model-
     *
     * @param stack The stack to add the ui elements to.
     * @param rectangle The rectangle for the cell.
     * @param model The model to bind to.
     */
    public static void generateAndBindSetableCellRectangle(StackPane stack, Rectangle rectangle, SetableCellModel model) {
        Label setableCellTextLabel = UIGenerationHelpers.generateAndBindDefaultSetableCellTextLabel(model);
        rectangle.setFill(Color.WHITE);
        stack.getChildren().addAll(rectangle, setableCellTextLabel);
    }

    /**
     * Generates a constraint cell with default values.
     *
     * @param stack The stack to add the ui elements to.
     * @param rectangle The rectangle for the cell.
     */
    public static void generateDefaultConstraintCell(StackPane stack, Rectangle rectangle) {
        Line diagonale = new Line();
        stack.getChildren().add(diagonale);
        diagonale.setStroke(Color.WHITE);
        diagonale.startXProperty().bind(rectangle.xProperty().subtract(rectangle.widthProperty().divide(2)));
        diagonale.endXProperty().bind(rectangle.xProperty().add(rectangle.widthProperty().divide(2)));

        diagonale.startYProperty().bind(rectangle.yProperty().subtract(rectangle.heightProperty().divide(2)));
        diagonale.endYProperty().bind(rectangle.yProperty().add(rectangle.heightProperty().divide(2)));
        diagonale.setStrokeWidth(1);
    }

    /**
     * Generate a single constraint cell with default values.
     *
     * @param stack The stack to add the ui elements to.
     * @param cell The single cell
     */
    public static void generateDefaultSingleConstraintCell(StackPane stack, SingleConstraintCell cell) {
        switch (cell.getOrientation()) {
            case VERTICAL:
                Label vLimit = new Label(String.valueOf(cell.getMaxValue()));
                vLimit.setFont(new Font("Arial", 15));
                vLimit.setAlignment(Pos.BOTTOM_CENTER);
                vLimit.paddingProperty().setValue(new Insets(3, 3, 3, 3));
                stack.setAlignment(vLimit, Pos.BOTTOM_CENTER);
                vLimit.setTextFill(Color.WHITE);
                stack.getChildren().add(vLimit);
                break;
            case HORIZONTAL:
                Label hLimit = new Label(String.valueOf(cell.getMaxValue()));
                hLimit.setAlignment(Pos.CENTER_RIGHT);
                hLimit.paddingProperty().setValue(new Insets(3, 3, 3, 3));
                hLimit.setFont(new Font("Arial", 15));
                stack.setAlignment(hLimit, Pos.CENTER_RIGHT);
                hLimit.setTextFill(Color.WHITE);
                stack.getChildren().add(hLimit);
                break;
        }
    }

    /**
     * Generate a double constraint cell with default values.
     *
     * @param stack The stack to add the ui elements to.
     * @param cell The single cell
     */
    public static void generateDefaultDoubleConstraintCell(StackPane stack, DoubleConstraintCell cell) {
        Label vLimit = new Label(String.valueOf(cell.getVerticalMax()));

        vLimit.setFont(new Font("Arial", 15));
        vLimit.paddingProperty().setValue(new Insets(3, 3, 3, 3));
        vLimit.paddingProperty().setValue(new Insets(3, 3, 3, 3));
        vLimit.setAlignment(Pos.BOTTOM_CENTER);
        stack.setAlignment(vLimit, Pos.BOTTOM_CENTER);
        vLimit.setTextFill(Color.WHITE);
        stack.getChildren().add(vLimit);

        Label hLimit = new Label(String.valueOf(cell.getHorizontalMax()));

        hLimit.paddingProperty().setValue(new Insets(3, 3, 3, 3));
        hLimit.setFont(new Font("Arial", 15));
        hLimit.setAlignment(Pos.CENTER_RIGHT);
        stack.setAlignment(hLimit, Pos.CENTER_RIGHT);
        hLimit.setTextFill(Color.WHITE);

        stack.getChildren().add(hLimit);
    }
}
