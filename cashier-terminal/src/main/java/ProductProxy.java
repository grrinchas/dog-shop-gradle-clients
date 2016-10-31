import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import java.util.List;

@Path("/products")
public interface ProductProxy {

    @GET
    @Produces("application/json")
    List<Product> getProducts();

    @GET
    @Path("{id}")
    @Produces
    Product getByID(@PathParam("id") int id);
}
