import org.jboss.resteasy.client.jaxrs.ResteasyWebTarget;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;

public final class ProxyProvider {

    private static final ResteasyWebTarget TARGET;

    private ProxyProvider() {}

    static {
        Client client = ClientBuilder.newBuilder().build();
        TARGET = (ResteasyWebTarget) client.target(CashierTerminal.SERVER_URI);
    }

    public static <T> T getProxy(Class<T> proxy) {
        return TARGET.proxy(proxy);
    }
}
