package example.micronaut;

import io.micronaut.http.MediaType;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;
import io.micronaut.http.annotation.Header;
import io.micronaut.http.annotation.Produces;
import io.micronaut.security.annotation.Secured;
import io.reactivex.Single;

@Controller("/user")
public class UserController {

    private final UsernameFetcher usernameFetcher;

    public UserController(UsernameFetcher usernameFetcher) {
        this.usernameFetcher = usernameFetcher;
    }

    @Secured("isAuthenticated()")
    @Produces(MediaType.TEXT_PLAIN)
    @Get
    Single<String> index() {
        return usernameFetcher.findUsername();
    }
}
