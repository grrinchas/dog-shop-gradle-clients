import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;

import java.util.List;

public final class CustomerController {

    @FXML
    private ListView<Product> ID_listView;
    @FXML
    private Label ID_productID;
    @FXML
    private Label ID_productDescription;
    @FXML
    private Label ID_productPrice;
    @FXML
    private Label ID_productStock;

    @FXML
    private void initialize() {

        this.initListView();
        this.onProductSelected();

    }

    private void initListView() {
        List<Product> beans = ProxyProvider.getProxy(ProductProxy.class).getProducts();
        ObservableList<Product> list = FXCollections.observableList(beans);
        this.ID_listView.setItems(list);
        this.ID_listView.setCellFactory(param -> new ListCell<Product>() {
            @Override
            public void updateItem(Product name, boolean empty) {
                super.updateItem(name, empty);
                if (!empty) {
                    this.setText(list.get(this.getIndex()).getDescription());
                    this.setGraphic(
                            new ImageView(new Image("http://localhost:8080" + list.get(this.getIndex()).getImage())));
                }
            }
        });

    }

    private void onProductSelected() {
        this.ID_listView.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            Product product = ProxyProvider.getProxy(ProductProxy.class).getByID(newValue.getId());
            this.ID_productID.setText(String.valueOf(product.getId()));
            this.ID_productDescription.setText(product.getDescription());
            this.ID_productPrice.setText(String.valueOf(product.getPrice()));
            int stock = product.getStock();
            if (stock < 1) {
                this.ID_productStock.setTextFill(Color.RED);
                this.ID_productStock.setText("Out of Stock (" + stock + ")");
            } else {
                this.ID_productStock.setTextFill(Color.GREEN);
                this.ID_productStock.setText("In Stock (" + stock + ")");
            }
        });
    }
}
