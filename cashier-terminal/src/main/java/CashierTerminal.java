import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

import java.net.URI;

public class CashierTerminal extends Application {

    public static final int WIDTH = 700;
    public static final int HEIGHT = 400;
    public static final String TITLE = "Dog Shop v1.0";
    public static final URI SERVER_URI = URI.create("http://localhost:8080/dog-shop/restful");
    public static final URI FXML = URI.create("cashierTerminal.fxml");

    @Override
    public void start(Stage primaryStage) throws Exception {

        FXMLLoader loader = new FXMLLoader(getClass().getResource(FXML.toString()));

        loader.setClassLoader(getClass().getClassLoader());
        GridPane root = loader.load();

        Scene scene = new Scene(root, WIDTH, HEIGHT);
        root.setPrefSize(scene.getWidth(), scene.getHeight());

        primaryStage.setTitle(TITLE);
        primaryStage.setScene(scene);
        primaryStage.setResizable(false);
        primaryStage.show();



    }

    public static void main(String[] args) {

        launch(args);
    }
}
