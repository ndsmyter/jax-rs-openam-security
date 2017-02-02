**Remark**: The code in this repository is for example use only.
The code will not function as-is, and should be integrated in a project.

When you're using JAX-RS and want to implement Spring Security method based security will not always work.
In this repository, I'll try to explain how you can get method base security anyway.


Normally you will have some kind of security in your security-ctx.xml (or other name), that will look like this:

    <sec:http use-expressions="true">
        <sec:intercept-url pattern="/**" access="hasAnyAuthority('admin_role','other_role')"/>
        <!-- .... -->
    </sec:http>

This is security that is based on the URL.
So we can limit the access for some sites to people with specific roles.

Sometimes you want even better control, and want to set security based on the methods that are being called.
In normal Spring security managed applications, you can use the `@PreAuthorize` and `PostAuthorize` annotations for this.
But apparently this isn't working in the JAX-RS applications.
The reason is actually fairly simple.
The classes aren't managed by Spring, but by the JAX-RS application.
For that reason, the annotations are just ignored.

So even if you use `@PreAuthorize("hasAuthority('a_role_that_doesnt_exist')"`, you will still be able to execute the method.
So the goal now is to make sure that the filter is actually used and not ignored.

For simplicity, I will work with the `@RolesAllowed('admin_role')` annotation.
The only reason, is because this was used the most in our application.

The first thing you have to do, is make sure you have an implementation of `javax.ws.rs.container.ContainerRequestFilter`.
It's this filter that will do the checking of the roles and other permissions.
The actual implementation of this filter, can be found in the [AuthenticationFilter](AuthenticationFilter.java).
As you can see in the code, support for `@PermitAll` and `@DenyAll` annotations were added too.

Now that we have the correct filter, we can use this filter in our application, so it is actually used.
To do this, we have to register this class in the `ResourceConfig`.
You can see how this is done, in the [ApiApplication](ApiApplication.java) class.

If we run this, we should have a REST API with 2 different endpoints:

 * `/test/admin`: Allowed only if you have the administrator role
 * `/test/user`: Always allowed