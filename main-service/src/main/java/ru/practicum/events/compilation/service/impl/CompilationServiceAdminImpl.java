package ru.practicum.events.compilation.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.events.compilation.dto.CompilationDto;
import ru.practicum.events.compilation.dto.NewCompilationDto;
import ru.practicum.events.compilation.dto.UpdateCompilationRequest;
import ru.practicum.events.compilation.mapper.CompilationMapper;
import ru.practicum.events.compilation.model.Compilation;
import ru.practicum.events.compilation.service.CompilationServiceAdmin;
import ru.practicum.events.compilation.storage.CompilationStorage;
import ru.practicum.events.event.model.Event;
import ru.practicum.events.event.storage.EventRepository;
import ru.practicum.exception.type.ResourceNotFoundException;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class CompilationServiceAdminImpl implements CompilationServiceAdmin {
    private final CompilationStorage compilationStorage;
    private final EventRepository eventRepository;

    @Transactional
    @Override
    public CompilationDto createCompilation(NewCompilationDto newCompilationDto) {
        Set<Event> events = new HashSet<>();
        if (newCompilationDto.getEvents() != null && !newCompilationDto.getEvents().isEmpty()) {
            events = addEvents(newCompilationDto.getEvents());
        }
        Compilation compilation = CompilationMapper.newCompilationDtoToCompilationAndEvents(newCompilationDto, events);
        return CompilationMapper.toDto(compilationStorage.save(compilation));
    }

    @Transactional
    @Override
    public void deleteCompilationById(Long compId) {
        if (!compilationStorage.existsById(compId)) {
            throw new ResourceNotFoundException("Подборка событий c id = " + compId + " не найдена");
        }
        compilationStorage.deleteById(compId);
    }

    @Transactional
    @Override
    public CompilationDto updateCompilationById(Long compId, UpdateCompilationRequest updateCompilationRequest) {
        Compilation newCompilation = getCompilationById(compId);
        Set<Event> events;
        if (updateCompilationRequest.getEvents() != null) {
            events = addEvents(updateCompilationRequest.getEvents());
            newCompilation.setEvents(events);
        }
        if (updateCompilationRequest.getPinned() != null) {
            newCompilation.setPinned(updateCompilationRequest.getPinned());
        }
        if (updateCompilationRequest.getTitle() != null && updateCompilationRequest.getTitle().isBlank()) {
            newCompilation.setTitle(updateCompilationRequest.getTitle());
        }
        return CompilationMapper.toDto(compilationStorage.save(newCompilation));
    }

    private Set<Event> addEvents(List<Long> eventsIds) {
        return eventRepository.findAllByIdIsIn(eventsIds);
    }

    private Compilation getCompilationById(Long id) {
        return compilationStorage.findById(id).orElseThrow(()
                -> new ResourceNotFoundException("Подборка событий c id = " + id + " не найдена"));
    }
}
