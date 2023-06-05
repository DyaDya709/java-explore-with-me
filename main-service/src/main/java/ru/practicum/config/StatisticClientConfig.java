package ru.practicum.config;

import exolrerwithme.HttpClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class StatisticClientConfig {
    @Value("${statistic-service-url}")
    private String url;

    @Bean
    public HttpClient getHttpClient() {
        return new HttpClient(url);
    }
}
