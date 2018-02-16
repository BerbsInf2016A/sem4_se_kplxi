package kakuroui;


import javafx.application.Application;
import javafx.stage.Stage;
import kakuro.KakuroSolver;


public class Main extends Application {
    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {

        primaryStage.setResizable(false);
        primaryStage.setTitle("Kakuro Solver");


        KakuroSolver kakuroSolver = new KakuroSolver();
        kakuroSolver.generateField();

        // Create controller
        KakuroController controller = new KakuroController(kakuroSolver);
        primaryStage.setOnCloseRequest(controller.windowIsClosedEventHandler);

        // Create Models
        KakuroModel model = new KakuroModel(kakuroSolver.getField().getCells());

        // Create View
        KakuroView view = new KakuroView(controller, model);


        primaryStage.setScene(view.generateUIScene());
        primaryStage.show();
    }
}
