package Demo_app_Rest.Demo_app_Rest.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class WebClientConfiguration {
    @Autowired
    private RestConfiguration configuration;

    @Bean
    public WebClient webClient() {
        return WebClient.builder()
                .defaultHeaders(HttpHeaders -> HttpHeaders.setBasicAuth(configuration.getUsername(), configuration.getPassword()))
                .build();
    }
}
