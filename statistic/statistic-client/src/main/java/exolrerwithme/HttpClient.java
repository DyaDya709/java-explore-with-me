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
    private static final String patternFormat = "yyyy-MM-dd HH:mm:ss";
    private static final String getStatUrl = "http://localhost:9090/stats";
    private static final String postHitUrl = "http://localhost:9090/hit";
    private static final Duration timeout = Duration.ofSeconds(10);
    private final WebClient webClient;

    private static String localDateTimeToString(LocalDateTime date) {
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern(patternFormat);
        return date.format(dateTimeFormatter);
    }

    public List<StatDto> getStats(LocalDateTime start, LocalDateTime end, List<String> uris, Boolean unique) {
        return webClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path(getStatUrl)
                        .queryParam("start", localDateTimeToString(start))
                        .queryParam("end", localDateTimeToString(end))
                        .queryParam("uris", uris)
                        .queryParam("unique", unique)
                        .build())
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<List<StatDto>>() {
                })
                .timeout(timeout)
                .block();
    }

    public void createHit(HitDto hitDto) {
        webClient.post()
                .uri(postHitUrl)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(hitDto)
                .retrieve()
                .bodyToMono(Void.class)
                .timeout(timeout)
                .block();
    }
}
