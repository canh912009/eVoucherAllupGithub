package asia.castis.evoucher.api;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

@EnableWebMvc
@SpringBootApplication
public class EVoucherApiApplication {

    @Value("${rest.template.timeout.sec:60}")
    private int defaultTimeout;

    private static final Logger logger = LoggerFactory.getLogger(EVoucherApiApplication.class);

    public static void main(String[] args) {
        logger.debug("==================== START EVoucherApiApplication =======================");
        SpringApplication.run(EVoucherApiApplication.class, args);
    }

    @Bean
    public ClientHttpRequestFactory simpleClientHttpRequestFactory() {
        SimpleClientHttpRequestFactory factory = new SimpleClientHttpRequestFactory();
        factory.setConnectTimeout(defaultTimeout * 1000);
        factory.setReadTimeout(defaultTimeout * 1000);
        return factory;
    }

    @Bean
    public RestTemplate restTemplate(ClientHttpRequestFactory factory) {
        return new RestTemplate(factory);
    }
}
