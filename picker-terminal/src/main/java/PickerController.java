import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import org.codehaus.jackson.map.ObjectMapper;

import javax.websocket.*;
import java.io.IOException;
import java.util.Optional;

@ClientEndpoint
public class PickerController {

    @FXML
    private TableView<Order> ID_orderTable;
    @FXML
    private TableColumn<Order, Integer> ID_orderNumber;
    @FXML
    private TableColumn<Order, String> ID_orderStatus;
    @FXML
    private Button ID_collected;
    @FXML
    private Button ID_completed;
    @FXML
    private Button ID_clear;

    private ObservableList<Order> orders = FXCollections.observableArrayList();
    private Session session;

    @FXML
    private void initialize() {

        this.ID_collected.setOnAction(event -> {
            TableView.TableViewSelectionModel<Order> selection = this.ID_orderTable.getSelectionModel();
            if (!selection.isEmpty() && selection.getSelectedItem().getStatus().equals("In Progress"))
                session.getAsyncRemote().sendText(String
                        .valueOf(ID_orderTable.getSelectionModel().getSelectedItem().getId() + "Collected"));
            this.ID_orderTable.refresh();

        });

        this.ID_completed.setOnAction(event -> {
            TableView.TableViewSelectionModel<Order> selection = this.ID_orderTable.getSelectionModel();
            if (!selection.isEmpty() && selection.getSelectedItem().getStatus().equals("Collected"))
                session.getAsyncRemote().sendText(String
                        .valueOf(ID_orderTable.getSelectionModel().getSelectedItem().getId() + "Completed"));
            this.ID_orderTable.refresh();

        });

        this.ID_clear.setOnAction(event -> {
            this.orders.clear();
        });

        ID_orderTable.setItems(orders);
        this.ID_orderNumber.setCellValueFactory(cellData -> cellData.getValue().idProperty().asObject());
        this.ID_orderStatus.setCellValueFactory(cellData -> cellData.getValue().statusProperty());

        this.connectToWebSocket();
    }


    private void connectToWebSocket() {
        WebSocketContainer container = ContainerProvider.getWebSocketContainer();
        try {
            container.connectToServer(this, PickerTerminal.SERVER_URI);
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

        Optional<Order> old = orders.stream().filter(o -> o.getId() == order.getId()).findFirst();
        if (old.isPresent()) {
            old.get().setStatus(order.getStatus());
        } else
            orders.add(order);

    }
}
