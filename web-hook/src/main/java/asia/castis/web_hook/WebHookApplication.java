package asia.castis.web_hook;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableJpaAuditing
@EnableFeignClients
@SpringBootApplication
@EnableScheduling
public class WebHookApplication {
	public static void main(String[] args) {
		SpringApplication.run(WebHookApplication.class, args);
	}

}
