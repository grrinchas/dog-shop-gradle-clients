import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.util.Callback;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class CashierController {

    @FXML
    private TextField ID_teProductID;
    @FXML
    private Label ID_confirmation;

    @FXML
    private Label ID_totalPrice;
    @FXML
    private TableView<Product> ID_productTable;

    @FXML
    private TableColumn<Product, Integer> ID_productID;
    @FXML
    private TableColumn<Product, Integer> ID_productStock;
    @FXML
    private TableColumn<Product, String> ID_productDescription;
    @FXML
    private TableColumn<Product, Double> ID_productPrice;
    @FXML
    private TableColumn<Product, Product> ID_productQuantity;
    @FXML
    private Button ID_cancelOrder;
    @FXML
    private Button ID_addOrder;

    private ObservableList<Product> products;
    private static Map<Integer, Integer> boxValues = new HashMap<>();

    private void initTable() {
        this.ID_productTable.setItems(this.products);
        this.ID_productTable.setRowFactory(new Callback<TableView<Product>, TableRow<Product>>() {
            @Override
            public TableRow<Product> call(TableView<Product> param) {
                return new TableRow<Product>() {
                    @Override
                    public void updateItem(Product item, boolean empty) {
                        super.updateItem(item, empty);
                        this.setDisable(false);
                        if (boxValues.isEmpty()) {
                            this.setStyle("");
                        }
                        if (!empty) {
                            if (item.getStock() < 1) {
                                this.setDisable(true);
                            }
                        }
                    }
                };
            }
        });
    }

    private void initCancelOrder() {
        this.ID_cancelOrder.setOnAction(event -> {
            this.ID_productTable.getItems().clear();
            this.ID_cancelOrder.setDisable(true);
            this.ID_addOrder.setDisable(true);
            this.ID_confirmation.setTextFill(Color.RED);
            this.ID_confirmation.setText("Order has been canceled");
            boxValues.clear();
            ID_totalPrice.setText("0.0");
        });
    }

    private void initAddOrder() {
        this.ID_addOrder.setOnAction(event -> {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Confirmation");
            alert.setHeaderText("Are you sure you want to order these items?");
            StringBuilder builder = new StringBuilder(100);

            boxValues.keySet().forEach(key -> {
                Product product = products.stream().filter(p -> p.getId() == key).findFirst().get();
                builder.append("(");
                builder.append(boxValues.get(key));
                builder.append(") ");
                builder.append(product.getDescription());
                builder.append(System.lineSeparator());

            });
            alert.setContentText(builder.toString());

            Optional<ButtonType> result = alert.showAndWait();
            if (result.get() == ButtonType.OK) {
                Response response = ProxyProvider.getProxy(OrderProxy.class).putOrder(boxValues);
                if (response.getStatus() == 200) {
                    this.ID_productTable.getItems().clear();
                    this.ID_cancelOrder.setDisable(true);
                    this.ID_addOrder.setDisable(true);
                    this.ID_confirmation.setTextFill(Color.GREEN);
                    this.ID_confirmation.setText("Order was successful");
                    boxValues.clear();
                    ID_totalPrice.setText("0.0");
                } else {
                    this.ID_confirmation.setTextFill(Color.RED);
                    this.ID_confirmation.setText("Order has been declined");
                }
            } else {
                alert.close();
            }
        });
    }

    @FXML
    private void initialize() {
        this.products = FXCollections.observableArrayList();
        this.initTable();
        this.initCancelOrder();
        this.initAddOrder();
        this.ID_productID.setCellValueFactory(cellData -> cellData.getValue().idProperty().asObject());
        this.ID_productDescription.setCellValueFactory(cellData -> cellData.getValue().descriptionProperty());
        this.ID_productStock.setCellValueFactory(cellData -> cellData.getValue().stockProperty().asObject());
        this.ID_productPrice.setCellValueFactory(cellData -> cellData.getValue().priceProperty().asObject());
        this.setCellEditable();


    }

    @FXML
    private void onProductIDSubmission() {
        String id = this.ID_teProductID.getText().trim();
        if (!id.matches("[\\d]+")) {
            this.ID_confirmation.setTextFill(Color.RED);
            this.ID_confirmation.setText("Product ID must consist only of digits");
        } else {
            try {
                Product product = ProxyProvider.getProxy(ProductProxy.class).getByID(Integer.valueOf(id));
                if (this.products.stream().filter(p -> p.getId() == product.getId()).findFirst().isPresent()) {
                    this.ID_confirmation.setTextFill(Color.RED);
                    this.ID_confirmation.setText("Product is already in the list ");
                    this.products.remove(product);
                } else {
                    this.products.add(product);
                    this.ID_confirmation.setText("");
                    this.ID_cancelOrder.setDisable(false);

                }

            } catch (WebApplicationException e) {
                this.ID_confirmation.setTextFill(Color.RED);
                this.ID_confirmation.setText("Product not found");
            }
        }
    }

    private void setCellEditable() {
        this.ID_productQuantity.setSortable(false);
        this.ID_productQuantity.setCellValueFactory(cellData -> cellData.getValue().getObjectProperty());
        this.ID_productQuantity.setCellFactory(column -> new AddPersonCell());
    }


    private class AddPersonCell extends TableCell<Product, Product> {
        private final StackPane paddedButton = new StackPane();
        private final ChoiceBox<Integer> choiceBox = new ChoiceBox<>();
        private final ObservableList<Integer> integers = FXCollections.observableArrayList();

        private AddPersonCell() {
            this.choiceBox.setItems(this.integers);
            this.choiceBox.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {

                TableRow<Product> row = this.getTableRow();
                row.setStyle("");
                if (newValue != null) {
                    if (newValue > 0) {
                        row.setStyle("-fx-background-color: lightgreen");
                        boxValues.put(this.getItem().getId(), newValue);
                    } else {
                        boxValues.remove(this.getItem().getId());
                    }
                    if (boxValues.values().stream().filter(value -> value > 0).findFirst().isPresent()) {
                        CashierController.this.ID_addOrder.setDisable(false);
                    } else {
                        CashierController.this.ID_addOrder.setDisable(true);
                    }

                    double price = 0;
                    for (Integer id : boxValues.keySet()) {
                        Optional<Product> op = products.stream().filter(p -> p.getId() == id).findFirst();
                        if (op.isPresent())
                            price += op.get().getPrice() * boxValues.get(id);
                    }
                    ID_totalPrice.setText(String.valueOf(price));
                }
            });
            this.paddedButton.getChildren().add(this.choiceBox);
        }

        @Override
        protected void updateItem(Product item, boolean empty) {
            super.updateItem(item, empty);
            if (empty || item == null) {
                this.setText(null);
                this.setGraphic(null);
            } else {
                this.setContentDisplay(ContentDisplay.GRAPHIC_ONLY);
                this.setGraphic(this.paddedButton);
                this.integers.setAll(IntStream.rangeClosed(0, item.getStock()).boxed().collect(Collectors.toList()));
                if (boxValues.containsKey(item.getId())) {
                    this.choiceBox.getSelectionModel().select(boxValues.get(item.getId()));
                } else {
                    this.choiceBox.getSelectionModel().selectFirst();
                }
            }
        }
    }
}
