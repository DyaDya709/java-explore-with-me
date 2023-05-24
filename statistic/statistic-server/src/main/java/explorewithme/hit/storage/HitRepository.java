package explorewithme.hit.storage;

import explorewithme.dto.StatDto;
import explorewithme.hit.model.Hit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface HitRepository extends JpaRepository<Hit, Long> {
    @Query("SELECT NEW explorewithme.dto.StatDto(h.app, h.uri, COUNT (h.ip)) FROM Hit AS h " +
            "WHERE h.timestamp BETWEEN :start AND :end " +
            "GROUP BY h.app, h.uri " +
            "ORDER BY COUNT (h.ip) DESC")
    List<StatDto> findByDate(LocalDateTime start, LocalDateTime end);

    @Query("SELECT NEW explorewithme.dto.StatDto(h.app, h.uri, COUNT (DISTINCT h.ip)) FROM Hit AS h " +
            "WHERE h.timestamp BETWEEN :start AND :end " +
            "GROUP BY h.app, h.uri " +
            "ORDER BY COUNT (h.ip) DESC")
    List<StatDto> findByDateAndUniqueIp(LocalDateTime start, LocalDateTime end);

    @Query("SELECT NEW explorewithme.dto.StatDto(h.app, h.uri, COUNT (h.ip)) FROM Hit AS h " +
            "WHERE h.timestamp BETWEEN :start AND :end AND h.uri IN :uris " +
            "GROUP BY h.app, h.uri " +
            "ORDER BY COUNT (h.ip) DESC")
    List<StatDto> findByDateAndUris(LocalDateTime start, LocalDateTime end, @Param("uris") List<String> uris);

    @Query("SELECT NEW explorewithme.dto.StatDto(h.app, h.uri, COUNT (DISTINCT h.ip)) FROM Hit AS h " +
            "WHERE h.timestamp BETWEEN :start AND :end AND h.uri IN :uris " +
            "GROUP BY h.app, h.uri " +
            "ORDER BY COUNT (h.ip) DESC")
    List<StatDto> findByDateAndUrisWithUniqueIp(LocalDateTime start, LocalDateTime end, @Param("uris") List<String> uris);
}
