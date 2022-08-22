package Demo_app_Rest.Demo_app_Rest;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients(basePackages = "Demo_app_Rest.Demo_app_Rest.clients")
@ConfigurationPropertiesScan
public class DemoAppRestApplication {

	public static void main(String[] args) {
		SpringApplication.run(DemoAppRestApplication.class, args);
	}

}
