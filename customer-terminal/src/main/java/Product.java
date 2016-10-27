import javafx.beans.property.*;

public final class Product {

    private IntegerProperty idProperty;
    private StringProperty descriptionProperty;
    private StringProperty imageProperty;
    private IntegerProperty stockProperty;
    private DoubleProperty priceProperty;

    public Product() {
        this.idProperty = new SimpleIntegerProperty();
        this.descriptionProperty = new SimpleStringProperty();
        this.imageProperty = new SimpleStringProperty();
        this.stockProperty = new SimpleIntegerProperty();
        this.priceProperty = new SimpleDoubleProperty();
    }

    public Product(int id, String description, String image, int stock, double price) {
        this();
        this.setId(id);
        this.setDescription(description);
        this.setImage(image);
        this.setStock(stock);
        this.setPrice(price);
    }

    public int getId() {
        return this.idProperty.get();
    }

    public void setId(int id) {
        this.idProperty.set(id);
    }

    public String getDescription() {
        return this.descriptionProperty.get();
    }

    public void setDescription(String description) {
        this.descriptionProperty.set(description);
    }

    public String getImage() {
        return this.imageProperty.get();
    }

    public void setImage(String image) {
        this.imageProperty.set(image);
    }

    public int getStock() {
        return this.stockProperty.get();
    }

    public void setStock(int stock) {
        this.stockProperty.set(stock);
    }

    public double getPrice() {
        return this.priceProperty.get();
    }

    public void setPrice(double price) {
        this.priceProperty.set(price);
    }

    public IntegerProperty idProperty() {
        return this.idProperty;
    }

    public StringProperty descriptionProperty() {
        return this.descriptionProperty;
    }

    public StringProperty imageProperty() {
        return this.imageProperty;
    }

    public IntegerProperty stockProperty() {
        return this.stockProperty;
    }

    public DoubleProperty priceProperty() {
        return this.priceProperty;
    }

}
