package example.micronaut;

import io.micronaut.http.MediaType;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;
import io.micronaut.http.annotation.Produces;
import io.micronaut.security.annotation.Secured;

import java.security.Principal;

@Controller("/user")
public class UserController {

    @Secured("isAuthenticated()")
    @Produces(MediaType.TEXT_PLAIN)
    @Get
    String index(Principal principal) {
        return principal.getName();
    }
}
