package be.ndsmyter.api;

import org.glassfish.jersey.server.ResourceConfig;

/**
 * @author Nicolas De Smyter
 * @since 2017-01-24
 */
public class ApiApplication extends ResourceConfig {

    /**
     * Start the REST API application and register the classes that can be used in the REST API.
     */
    public ApiApplication() {
        super(
                TestApi.class,
                AuthenticationFilter.class  // Add some security !
        );
    }
}