import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.cloud.client.circuitbreaker.EnableCircuitBreaker;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.AbstractEnvironment;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.ThreadLocalRandom;

@Configuration
@ComponentScan(basePackages={ "com.metasoft.controller", "com.metasoft.service"})
@EnableAutoConfiguration
@EnableEurekaClient
@EnableCircuitBreaker
@RestController
public class App {

    public static void main(String[] args) {
    	System.setProperty(AbstractEnvironment.ACTIVE_PROFILES_PROPERTY_NAME, "client");
        new SpringApplicationBuilder(App.class).web(true).run(args);
    }

}