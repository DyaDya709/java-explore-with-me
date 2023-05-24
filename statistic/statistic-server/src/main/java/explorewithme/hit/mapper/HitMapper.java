package explorewithme.hit.mapper;

import explorewithme.dto.HitDto;
import explorewithme.exception.BadRequestException;
import explorewithme.hit.model.Hit;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class HitMapper {
    private static final String PATTERN_FORMAT = "yyyy-MM-dd HH:mm:ss";

    public static Hit toEntity(HitDto hitDto) {
        Hit hit = new Hit();
        hit.setId(hitDto.getId());
        hit.setApp(hitDto.getApp());
        hit.setIp(hitDto.getIp());
        hit.setUri(hitDto.getUri());
        hit.setTimestamp(stringToLocalDateTime(hitDto.getTimestamp()));
        return hit;
    }

    public static LocalDateTime stringToLocalDateTime(String date) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(PATTERN_FORMAT);
        try {
            return LocalDateTime.parse(date, formatter);
        } catch (DateTimeParseException e) {
            throw new BadRequestException("invalid date");
        }

    }

}
