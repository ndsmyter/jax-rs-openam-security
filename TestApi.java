package be.ndsmyter.api;

import org.springframework.stereotype.Service;

import javax.annotation.security.RolesAllowed;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;

/**
 * @author Nicolas De Smyter
 * @since 2017-01-24
 */
@Service
@Path("/test")
public class TestApi {

    /**
     * Execute a GET call.
     *
     * @return the result of the GET call
     */
    @GET
    @Path("/admin")
    @Produces(MediaType.TEXT_PLAIN)
    @RolesAllowed("admin_role")
    public Response doGet() {
        return Response.ok().entity("We're in, and you're an admin").build();
    }

    /**
     * Execute a GET call.
     *
     * @return the result of the GET call
     */
    @GET
    @Path("/user")
    @Produces(MediaType.TEXT_PLAIN)
    public Response doGet() {
        return Response.ok().entity("We're in, and you're no admin").build();
    }
}