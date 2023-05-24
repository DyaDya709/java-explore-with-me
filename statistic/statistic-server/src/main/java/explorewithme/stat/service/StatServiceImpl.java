package explorewithme.stat.service;

import explorewithme.dto.StatDto;
import explorewithme.exception.BadRequestException;
import explorewithme.hit.mapper.HitMapper;
import explorewithme.hit.storage.HitRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

@Service
@RequiredArgsConstructor
public class StatServiceImpl implements StatService {
    private final HitRepository hitRepository;

    @Override
    public List<StatDto> getStats(String start, String end, List<String> uris, boolean unique) {
        LocalDateTime newStart = HitMapper.stringToLocalDateTime(start);
        LocalDateTime newEnd = HitMapper.stringToLocalDateTime(end);
        validateParamsDate(newStart, newEnd);
        if (uris == null && !unique) {
            return hitRepository.findByDate(newStart, newEnd);
        }
        if (uris == null && unique) {
            return hitRepository.findByDateAndUniqueIp(newStart, newEnd);
        }
        if (!uris.isEmpty() && !unique) {
            return hitRepository.findByDateAndUris(newStart, newEnd, uris);
        }
        if (!uris.isEmpty() && unique) {
            return hitRepository.findByDateAndUrisWithUniqueIp(newStart, newEnd, uris);
        }
        return Collections.emptyList();
    }

    private void validateParamsDate(LocalDateTime start, LocalDateTime end) {
        if (end.isBefore(start) || start.isAfter(end)) {
            throw new BadRequestException("invalid date params");
        }
    }
}
