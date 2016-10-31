import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.util.Callback;
import org.codehaus.jackson.map.ObjectMapper;

import javax.websocket.*;
import java.io.IOException;
import java.util.Optional;


@ClientEndpoint
public class ScreenController {

    @FXML
    private TableView<Order> ID_orderTable;
    @FXML
    private TableColumn<Order, Integer> ID_orderNumber;
    @FXML
    private TableColumn<Order, String> ID_orderStatus;

    private ObservableList<Order> orders = FXCollections.observableArrayList();

    @FXML
    private void initialize() {
        ID_orderTable.setItems(orders);
        ID_orderTable.setSelectionModel(null);
        this.ID_orderNumber.setCellValueFactory(cellData -> cellData.getValue().idProperty().asObject());
        this.ID_orderStatus.setCellValueFactory(cellData -> cellData.getValue().statusProperty());

        this.ID_orderTable.setRowFactory(new Callback<TableView<Order>, TableRow<Order>>() {
            @Override
            public TableRow<Order> call(TableView<Order> param) {
                return new TableRow<Order>() {
                    @Override
                    public void updateItem(Order item, boolean empty) {
                        super.updateItem(item, empty);
                        this.setStyle("");
                        if (!empty) {
                            if (item.getStatus().equals("In Progress")) {
                                this.setStyle("-fx-background-color: lightcoral");
                            } else if (item.getStatus().equals("Collected")) {
                                this.setStyle("-fx-background-color: lightyellow");
                            } else if (item.getStatus().equals("Completed")) {
                                this.setStyle("-fx-background-color: lightgreen");
                            }
                        }
                    }
                };
            }
        });
        connectToWebSocket();
    }
    private void connectToWebSocket() {
        WebSocketContainer container = ContainerProvider.getWebSocketContainer();
        try {
            container.connectToServer(this, ScreenTerminal.SERVER_URI);
        } catch (DeploymentException | IOException ex) {
            System.exit(-1);
        }
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

        this.ID_orderTable.setRowFactory(new Callback<TableView<Order>, TableRow<Order>>() {
            @Override
            public TableRow<Order> call(TableView<Order> param) {
                return new TableRow<Order>() {
                    @Override
                    public void updateItem(Order item, boolean empty) {
                        super.updateItem(item, empty);
                        this.setStyle("");
                        if (!empty) {
                            if (item.getStatus().equals("In Progress")) {
                                this.setStyle("-fx-background-color: lightcoral");
                            } else if (item.getStatus().equals("Collected")) {
                                this.setStyle("-fx-background-color: lightyellow");
                            } else if (item.getStatus().equals("Completed")) {
                                this.setStyle("-fx-background-color: lightgreen");
                            }
                        }
                    }
                };
            }
        });

    }
}
