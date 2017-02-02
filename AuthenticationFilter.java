package be.ndsmyter.api;

import javax.annotation.security.DenyAll;
import javax.annotation.security.PermitAll;
import javax.annotation.security.RolesAllowed;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.container.ResourceInfo;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import java.io.IOException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

/**
 * @author Nicolas De Smyter
 * @since 2017-01-24
 */
class AuthenticationFilter implements ContainerRequestFilter {

    @Context
    private ResourceInfo resourceInfo;

    private static final Response ACCESS_DENIED = Response.status(Response.Status.UNAUTHORIZED)
            .entity("You cannot access this resource").build();

    private static final Response ACCESS_FORBIDDEN = Response.status(Response.Status.FORBIDDEN)
            .entity("Access blocked for all users !!").build();

    @Override
    public void filter(ContainerRequestContext requestContext) throws IOException {

        Method method = resourceInfo.getResourceMethod();
        if (method.isAnnotationPresent(PermitAll.class)) {
            // Everybody can access this, no need to check something
            return;
        }
        if (method.isAnnotationPresent(DenyAll.class)) {
            // Nobody can access this, no need to check something
            requestContext.abortWith(ACCESS_FORBIDDEN);
            return;
        }

        //Verify user access
        if (method.isAnnotationPresent(RolesAllowed.class)) {
            RolesAllowed rolesAnnotation = method.getAnnotation(RolesAllowed.class);
            Set<String> requiredRoles = new HashSet<>(Arrays.asList(rolesAnnotation.value()));

            // Convenience method to get the logged in user from Spring security
            Collection<String> actualRoles = UserUtils.getUser().getRoles();

            if (Collections.disjoint(requiredRoles, actualRoles)) {
                // None of the required roles is available in the actual roles
                requestContext.abortWith(ACCESS_DENIED);
            }
        }
    }
}
