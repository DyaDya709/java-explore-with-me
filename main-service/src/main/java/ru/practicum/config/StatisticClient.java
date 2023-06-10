package ru.practicum.config;

import exolrerwithme.HttpClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Configuration
public class StatisticClient {
    @Value("${statistic-service-url}")
    private String serverUrl;

    @Bean
    public HttpClient statsClient() {
        return new HttpClient(serverUrl);
    }
}
