package um.feri.resource;

import io.smallrye.common.annotation.NonBlocking;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;

@ApplicationScoped
@Path("/threads")
public class ThreadResource {

    @GET
    @Path("/worker")
    public String workerThread() {
        return "This was handled on the worker thread";
    }

    @NonBlocking
    @GET
    @Path("/nonblocking")
    public String eventLoopThread() {
        return "This was handled on the event loop thread";
    }

}
