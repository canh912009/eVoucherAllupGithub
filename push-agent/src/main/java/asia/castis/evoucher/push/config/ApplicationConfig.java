package asia.castis.evoucher.push.config;

import asia.castis.evoucher.push.components.PushCipher;
import lombok.extern.slf4j.Slf4j;
import net.javacrumbs.shedlock.provider.elasticsearch.ElasticsearchLockProvider;
import net.javacrumbs.shedlock.spring.annotation.EnableSchedulerLock;
import org.elasticsearch.client.RestHighLevelClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
@EnableScheduling
@EnableSchedulerLock(defaultLockAtMostFor = "${schedule.shedlock:59m}", defaultLockAtLeastFor = "${schedule.shedlock:59m}")
@Slf4j
public class ApplicationConfig {
    @Value("${smsFPT.uri}")
    private String smsBaseUrl;
    @Value("${fnsFPT.base-url}")
    private String zaloBaseUrl;
    @Value("${cipher.initVector:TotalRandomVector}")
    private String initVector;

    @Value("${cipher.key:XaYbCz3579CzXaYb0246813579aBcDeF}")
    private String key;

   @Bean
    public WebClient webClientSMS(WebClient.Builder webClientBuilder) {
        return webClientBuilder
                //.filter(WebClientUtils.smsErrorHandlingFilter())
                .baseUrl(smsBaseUrl)
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .build();
    }
    @Bean
    public WebClient webClientZalo(WebClient.Builder webClientBuilder) {
        return webClientBuilder
               // .filter(WebClientUtils.zaloErrorHandlingFilter())
            .baseUrl(zaloBaseUrl)
            .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
            .build();
    }
    @Bean
    public ElasticsearchLockProvider lockProvider(RestHighLevelClient client) {
        return new ElasticsearchLockProvider(client);
    }
    @Bean
    public PushCipher pushCipher() {
       return new PushCipher(this.initVector, this.key);
    }
}
