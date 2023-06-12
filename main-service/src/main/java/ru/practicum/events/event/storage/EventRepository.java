package ru.practicum.events.event.storage;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.practicum.category.model.Category;
import ru.practicum.events.event.model.Event;
import ru.practicum.events.event.model.EventState;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Repository
public interface EventRepository extends JpaRepository<Event, Long> {

    @Query("SELECT e FROM Event e " +
            "WHERE (e.initiator.id IN (:users) OR :users = NULL) " +
            "AND (e.state IN (:states) OR :states = NULL) " +
            "AND (e.category.id IN (:categories) OR :categories = NULL) " +
            "AND (e.eventDate>=:rangeStart OR CAST(:rangeStart AS date) = NULL) " +
            "AND (e.eventDate<=:rangeEnd OR CAST(:rangeEnd AS date) = NULL)")
    List<Event> findAllByAdminAndState(@Param("users") List<Long> users,
                                       @Param("states") List<EventState> states,
                                       @Param("categories") List<Long> categories,
                                       @Param("rangeStart") LocalDateTime rangeStart,
                                       @Param("rangeEnd") LocalDateTime rangeEnd,
                                       Pageable page);

    @Query(value = "SELECT e " +
            "FROM Event e " +
            "WHERE (e.initiator.id IN :users OR :users = NULL) " +
            "AND (e.category.id IN :categories  OR :categories = NULL) " +
            "AND (e.eventDate >= :rangeStart)  " +
            "OR CAST(:rangeStart AS date) = NULL " +
            "AND (e.eventDate <= :rangeEnd)   " +
            "OR CAST(:rangeEnd AS date) = NULL")
    List<Event> findAllByAdmin(@Param("users") List<Long> users,
                               @Param("categories") List<Long> categories,
                               @Param("rangeStart") LocalDateTime rangeStart,
                               @Param("rangeEnd") LocalDateTime rangeEnd,
                               Pageable page);


    @Query("SELECT e FROM Event e WHERE (e.state='PUBLISHED') AND " +
            "(LOWER(e.annotation) LIKE LOWER(CONCAT('%',:text,'%')) OR  " +
            "LOWER(e.description) LIKE LOWER(CONCAT('%',:text,'%')) OR :text=NULL) AND" +
            "(e.category.id IN (:categories) OR :categories = NULL) AND (e.paid=:paid OR :paid=NULL) AND" +
            "(e.eventDate>=CAST(:rangeStart AS date)) AND " +
            "(e.eventDate<=CAST(:rangeEnd AS date) OR CAST(:rangeEnd AS date) IS NULL)")
    List<Event> findAllByPublic(@Param("text") String text,
                                @Param("categories") List<Long> categories,
                                @Param("paid") Boolean paid,
                                @Param("rangeStart") LocalDateTime rangeStart,
                                @Param("rangeEnd") LocalDateTime rangeEnd,
                                Pageable page);

    Optional<Event> findEventByIdAndStateIs(Long id, EventState state);

    List<Event> findEventByCategoryIs(Category category);

    Set<Event> findAllByIdIsIn(List<Long> id);

    List<Event> findAllByInitiatorId(Long userId, Pageable pageable);
}
