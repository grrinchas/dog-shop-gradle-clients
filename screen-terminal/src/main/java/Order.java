import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

/**
 * Created by dg467 on 27/10/2016.
 */
public class Order {

    private IntegerProperty idProperty;
    private StringProperty statusProperty;

    public Order() {
        this.idProperty = new SimpleIntegerProperty();
        this.statusProperty = new SimpleStringProperty();
    }
    public Order(int orderID, String orderStatus) {
        this();
        this.setId(orderID);
        this.setStatus(orderStatus);
    }
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Order order = (Order) o;

        return idProperty.equals(order.idProperty);

    }

    @Override
    public int hashCode() {
        return idProperty.hashCode();
    }

    public int getId() {
        return this.idProperty.get();
    }

    public void setId(int id) {
        this.idProperty.set(id);
    }

    public String getStatus() {
        return this.statusProperty.get();
    }

    public void setStatus(String description) {
        this.statusProperty.set(description);
    }

    public IntegerProperty idProperty() {
        return this.idProperty;
    }

    public StringProperty statusProperty() {
        return this.statusProperty;
    }
}
