import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import org.codehaus.jackson.map.ObjectMapper;

import javax.websocket.*;
import java.io.IOException;
import java.net.URI;

@ClientEndpoint
public class ScreenTerminal extends Application {

    public static final int WIDTH = 500;
    public static final int HEIGHT = 250;
    public static final String TITLE = "Dog Shop v1.0";
    public static final URI SERVER_URI = URI.create("ws://localhost:8080/dog-shop/orders");
    public static final URI FXML = URI.create("screenTerminal.fxml");

    private Session session;
    public static ObservableList<Order> orders = FXCollections.observableArrayList();

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
        connectToWebSocket();
    }

    private void connectToWebSocket() {
        WebSocketContainer container = ContainerProvider.getWebSocketContainer();
        try {
            container.connectToServer(this, SERVER_URI);
        } catch (DeploymentException | IOException ex) {
            System.exit(-1);
        }
    }

    @OnOpen
    public void onOpen(Session session) {
        this.session = session;
    }

    @OnMessage
    public void onMessage(String jsonOrder) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        Order order = mapper.readValue(jsonOrder, Order.class);
        if (orders.contains(order))
            orders.stream().filter(o -> o.getId() == order.getId()).findFirst().get();
        else
            orders.add(order);
    }

    public static void main(String[] args) {
        launch(args);
    }
}
