package example.micronaut

import io.micronaut.context.ApplicationContext
import io.micronaut.http.HttpRequest
import io.micronaut.http.HttpResponse
import io.micronaut.http.HttpStatus
import io.micronaut.http.MediaType
import io.micronaut.http.client.BlockingHttpClient
import io.micronaut.http.client.HttpClient
import io.micronaut.http.client.RxHttpClient
import io.micronaut.http.client.exceptions.HttpClientException
import io.micronaut.security.authentication.UsernamePasswordCredentials
import io.micronaut.security.token.jwt.render.BearerAccessRefreshToken
import org.junit.Assume
import spock.lang.AutoCleanup
import spock.lang.Requires
import spock.lang.Shared
import spock.lang.Specification

class AcceptanceSpec extends Specification {

    static final String GATEWAY_URL = 'http://localhost:8080'
    static final String USERECHO_URL = 'http://localhost:8081'

    @Shared
    HttpClient httpClient = HttpClient.create(new URL(GATEWAY_URL))

    @Shared
    BlockingHttpClient client = httpClient.toBlocking()

    @Requires( {
        Closure isUp = { client, url ->
            String microservicesUrl = url.endsWith('/health') ? url : "${url}/health"
            try {
                StatusResponse statusResponse = client.retrieve(HttpRequest.GET(microservicesUrl), StatusResponse)
                if ( statusResponse.status == 'UP' ) {
                    return true
                }
            } catch (HttpClientException e) {
                println "HTTP Client exception for $microservicesUrl $e.message"
            }
            return false
        }
        BlockingHttpClient gatewayClient = HttpClient.create(new URL(GATEWAY_URL)).toBlocking()
        BlockingHttpClient userEchoClient = HttpClient.create(new URL(USERECHO_URL)).toBlocking()
        return isUp(gatewayClient, GATEWAY_URL) && isUp(userEchoClient, USERECHO_URL)
    })
    void "verifies token is propagated"() {
        when:
        UsernamePasswordCredentials credentials = new UsernamePasswordCredentials("sherlock", "password")
        HttpRequest request = HttpRequest.POST("/login", credentials)
        HttpResponse<BearerAccessRefreshToken> rsp = client.exchange(request, BearerAccessRefreshToken)

        then:
        noExceptionThrown()
        rsp.status() == HttpStatus.OK
        rsp.body().accessToken
        rsp.body().username

        when:
        String username = client.retrieve(HttpRequest.GET("/user")
                .header("Authorization", "Bearer ${rsp.body().accessToken}".toString())
                .accept(MediaType.TEXT_PLAIN), String.class)

        then:
        noExceptionThrown()
        username == 'sherlock'
    }
}
