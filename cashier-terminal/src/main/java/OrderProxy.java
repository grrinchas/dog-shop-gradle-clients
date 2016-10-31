import javax.ws.rs.Consumes;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;
import java.util.Map;

@Path("/orders")
public interface OrderProxy {

    @PUT
    @Consumes("application/json")
    @Produces("text/plain")
    Response putOrder(Map<Integer, Integer> orders);

}
