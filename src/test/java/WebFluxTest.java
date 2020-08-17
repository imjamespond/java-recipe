import org.junit.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.FluxExchangeResult;
import org.springframework.test.web.reactive.server.WebTestClient;

public class WebFluxTest {

    @Test
    public void test() {
        WebTestClient client = WebTestClient.bindToServer().baseUrl("http://localhost:8888/").build();
        FluxExchangeResult<String> rs = client.get().uri("/hello")
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk().returnResult(String.class);
                //.expectHeader().contentType(MediaType.APPLICATION_JSON);
        System.out.println(rs.toString());
    }
}
