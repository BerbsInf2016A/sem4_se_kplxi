package kakuroui;


import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.StrokeType;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import kakuro.BlockingCell;
import kakuro.Cell;
import kakuro.Configuration;
import kakuro.ConstraintCell;
import kakuro.DoubleConstraintCell;
import kakuro.PuzzleField;
import kakuro.SetableCell;
import kakuro.SingleConstraintCell;
import sun.awt.ConstrainableGraphics;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


public class Main extends Application {
    GridPane root = new GridPane();
    final int playFieldSize = Configuration.instance.rowAndColumnSize;
    final int totalSize = Configuration.instance.rowAndColumnSize + 1;
    List<SetableCellModel> models = new ArrayList<>();
    private Thread solverThread;

    public SetableCellModel getModelCell(int row, int column){
        if (row > Configuration.instance.rowAndColumnSize || column > Configuration.instance.rowAndColumnSize || row < 0 || column < 0) {
            throw new RuntimeException("Invalid row column combination. row: " + row + " column: " + column);
        }
        Optional<SetableCellModel> cell = this.models.stream().filter(c -> c.getColumn() == column && c.getRow() == row).findFirst();
        if (cell.isPresent()) return cell.get();
        return null;
    }

    @Override
    public void start(Stage primaryStage) throws Exception{

        primaryStage.setResizable(false);

        kakuro.Application kakuroSolver = new kakuro.Application();
        PuzzleField field = kakuroSolver.generateField();
        field.getCells().stream().filter(cell -> cell instanceof SetableCell)
                .forEach(cell -> models.add(new SetableCellModel((SetableCell) cell)));




        for (int row = 0; row < playFieldSize; row++) {
            for (int col = 0; col < playFieldSize; col++) {
                Rectangle rectangle = new Rectangle();

                StackPane stack = this.bindKakuroCellToRectangle(rectangle, row, col, field);


                root.add(stack, col, row);
                rectangle.widthProperty().setValue(65);
                rectangle.heightProperty().setValue(65);
                rectangle.setStroke(Color.WHITE);
                rectangle.setStrokeType(StrokeType.INSIDE);





            }
        }

        this.addButtonControlsToThePane(root, Configuration.instance.rowAndColumnSize + 1);
        this.addSimulationSliderToThePane(root, Configuration.instance.rowAndColumnSize + 2);

        root.setPadding(new Insets(10,10,10,10));
        primaryStage.setScene(new Scene(root, 610, 700));
        primaryStage.show();


        this.solverThread = new Thread() {
            public void run() {
                kakuroSolver.solveField(field);
            }
        };

    }


    private void addSimulationSliderToThePane(GridPane root, int row) {

        Label descriptionLabel = new Label("Simulation-Delay:");

        Label valueLabel = new Label();
        valueLabel.textProperty().setValue(String.valueOf(Configuration.instance.stepDelayInMilliseconds + " ms"));

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

        slider.valueProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                Configuration.instance.stepDelayInMilliseconds = newValue.intValue();
                valueLabel.textProperty().setValue(String.valueOf(newValue.intValue() + " ms"));
            }
        });

        root.add(descriptionLabel, 1, row, 4,1);
        root.add(valueLabel, 6, row, 1,1);
        root.add(slider, 1, (row + 1), Configuration.instance.rowAndColumnSize - 2, 1 );
    }

    private void addButtonControlsToThePane(GridPane root, int row) {
        Button startButton = new Button("Start");
        startButton.setMaxWidth(Double.MAX_VALUE);
        startButton.setMaxHeight(Double.MAX_VALUE);

        startButton.setOnAction(new EventHandler() {
            @Override
            public void handle(Event event) {
                startSolvingThread();
            }
        });
        
        root.add(startButton, 1, row, 3, 1 );
        

        Button exitButton = new Button("Close");
        exitButton.setMaxWidth(Double.MAX_VALUE);
        exitButton.setMaxHeight(Double.MAX_VALUE);

        exitButton.setOnAction(new EventHandler() {
            @Override
            public void handle(Event event) {
                stopSolvingThread();
            }
        });

        root.add(exitButton, 5, row, 3, 1 );
    }

    private void stopSolvingThread() {
        if (this.solverThread.isAlive())
            this.solverThread.interrupt();
        System.exit(0);
    }

    private void startSolvingThread()
    { if (!this.solverThread.isAlive())
        this.solverThread.start();
    }

    private StackPane bindKakuroCellToRectangle(Rectangle rectangle, int row, int col, PuzzleField field) {
        Cell fieldCell = field.getFieldCell(row, col);
        StackPane stack = new StackPane();

        if (fieldCell instanceof ConstraintCell || fieldCell instanceof BlockingCell){
            rectangle.setFill(Color.BLACK);
            stack.getChildren().add(rectangle);
            if (fieldCell instanceof ConstraintCell) {
                Line diagonale = new Line();
                stack.getChildren().add(diagonale);
                diagonale.setStroke(Color.WHITE);
                diagonale.startXProperty().bind(rectangle.xProperty().subtract( rectangle.widthProperty().divide(2)));
                diagonale.endXProperty().bind(rectangle.xProperty().add(rectangle.widthProperty().divide(2)));

                diagonale.startYProperty().bind(rectangle.yProperty().subtract(rectangle.heightProperty().divide(2)));
                diagonale.endYProperty().bind(rectangle.yProperty().add(rectangle.heightProperty().divide(2)));
                diagonale.setStrokeWidth(1);

                if (fieldCell instanceof SingleConstraintCell){
                    SingleConstraintCell single = (SingleConstraintCell) fieldCell;
                    switch (single.getOrientation()) {
                        case VERTICAL:
                            Label vLimit = new Label(String.valueOf(single.getMaxValue()));
                            vLimit.setFont(new Font("Arial", 15));
                            vLimit.setAlignment(Pos.BOTTOM_CENTER);
                            stack.setAlignment(vLimit, Pos.BOTTOM_CENTER);
                            vLimit.setTextFill(Color.WHITE);
                            stack.getChildren().add(vLimit);
                            break;
                        case HORIZONTAL:
                            Label hLimit = new Label(String.valueOf(single.getMaxValue()));
                            hLimit.setAlignment(Pos.CENTER_RIGHT);
                            hLimit.setFont(new Font("Arial", 15));
                            stack.setAlignment(hLimit, Pos.CENTER_RIGHT);
                            hLimit.setTextFill(Color.WHITE);
                            stack.getChildren().add(hLimit);
                            break;
                    }
                }
                if (fieldCell instanceof DoubleConstraintCell){
                    DoubleConstraintCell doubleConstraint = (DoubleConstraintCell) fieldCell;
                            Label vLimit = new Label(String.valueOf(doubleConstraint.getVerticalMax()));
                            vLimit.setFont(new Font("Arial", 15));
                            vLimit.setAlignment(Pos.BOTTOM_CENTER);
                            stack.setAlignment(vLimit, Pos.BOTTOM_CENTER);
                            vLimit.setTextFill(Color.WHITE);
                            stack.getChildren().add(vLimit);
                            Label hLimit = new Label(String.valueOf(doubleConstraint.getHorizontalMax()));
                            hLimit.setFont(new Font("Arial", 15));
                            hLimit.setAlignment(Pos.CENTER_RIGHT);
                            stack.setAlignment(hLimit, Pos.CENTER_RIGHT);
                            hLimit.setTextFill(Color.WHITE);
                            stack.getChildren().add(hLimit);
                    
                }
                
                
            }

        } else {
            SetableCellModel model = this.getModelCell(row, col);
            rectangle.setFill(Color.WHITE);
            Label text = new Label();
            text.setFont(new Font("Arial", 30));
            text.textProperty().bind(model.valueProperty());
            stack.getChildren().addAll(rectangle, text);
        }
        return stack;
    }




    public static void main(String[] args) {
        launch(args);
    }
}
