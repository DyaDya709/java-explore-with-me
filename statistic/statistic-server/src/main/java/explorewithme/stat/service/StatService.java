package explorewithme.stat.service;

import explorewithme.dto.StatDto;

import java.util.List;

public interface StatService {
    List<StatDto> getStats(String start, String end, List<String> uris, boolean unique);
}
