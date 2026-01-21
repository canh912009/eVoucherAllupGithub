package asia.castis.evoucherservicefe.config;

import org.elasticsearch.client.RestHighLevelClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.elasticsearch.client.ClientConfiguration;
import org.springframework.data.elasticsearch.client.RestClients;
import org.springframework.data.elasticsearch.config.AbstractElasticsearchConfiguration;
import org.springframework.data.elasticsearch.core.ElasticsearchRestTemplate;
import org.springframework.data.elasticsearch.repository.config.EnableElasticsearchRepositories;

@Configuration
@EnableElasticsearchRepositories(basePackages = "asia.castis.evoucherservicefe.publishrequest.repository")
public class ElasticSearchConfig extends AbstractElasticsearchConfiguration {
    @Value("${elastic.host}")
    public String elasticsearchHost;
    @Value("${elastic.port}")
    public int elasticsearchPort;
    @Value("${elastic.basicAuth}")
    public boolean allowBasicAuth;
    @Value("${elastic.username}")
    public String elasticsearchUsername;
    @Value("${elastic.password}")
    public String elasticsearchPassword;

    @Bean
    @Override
    public RestHighLevelClient elasticsearchClient() {
        ClientConfiguration clientConfiguration;
        if (allowBasicAuth) {
            clientConfiguration = ClientConfiguration.builder()
                    .connectedTo(String.format("%s:%d", elasticsearchHost, elasticsearchPort))
                    .withBasicAuth(elasticsearchUsername, elasticsearchPassword)
                    .build();
        } else {
            clientConfiguration = ClientConfiguration.builder()
                    .connectedTo(String.format("%s:%d", elasticsearchHost, elasticsearchPort))
                    .build();
        }
        return RestClients.create(clientConfiguration).rest();
    }

    @Bean
    public ElasticsearchRestTemplate elasticsearchRestTemplate() {
        return new ElasticsearchRestTemplate(elasticsearchClient());
    }
}
