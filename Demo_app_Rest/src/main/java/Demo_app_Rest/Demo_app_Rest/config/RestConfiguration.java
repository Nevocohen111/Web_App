package Demo_app_Rest.Demo_app_Rest.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "rest")
@Data
public class RestConfiguration {
    private String username;
    private String baseUrl;
    private String password;
}
