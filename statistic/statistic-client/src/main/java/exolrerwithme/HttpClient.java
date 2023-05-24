package exolrerwithme;

import explorewithme.dto.HitDto;
import explorewithme.dto.StatDto;
import lombok.RequiredArgsConstructor;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
@RequiredArgsConstructor
public class HttpClient {
    private static final String PATTERN_FORMAT = "yyyy-MM-dd HH:mm:ss";
    private static final String GET_STAT_URL = "http://localhost:9090/stats";
    private static final String POST_HIT_URL = "http://localhost:9090/hit";
    private static final Duration TIMEOUT = Duration.ofSeconds(10);
    private final WebClient webClient;

    private static String localDateTimeToString(LocalDateTime date) {
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern(PATTERN_FORMAT);
        return date.format(dateTimeFormatter);
    }

    public List<StatDto> getStats(LocalDateTime start, LocalDateTime end, List<String> uris, Boolean unique) {
        return webClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path(GET_STAT_URL)
                        .queryParam("start", localDateTimeToString(start))
                        .queryParam("end", localDateTimeToString(end))
                        .queryParam("uris", uris)
                        .queryParam("unique", unique)
                        .build())
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<List<StatDto>>() {
                })
                .timeout(TIMEOUT)
                .block();
    }

    public void createHit(HitDto hitDto) {
        webClient.post()
                .uri(POST_HIT_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(hitDto)
                .retrieve()
                .bodyToMono(Void.class)
                .timeout(TIMEOUT)
                .block();
    }
}
