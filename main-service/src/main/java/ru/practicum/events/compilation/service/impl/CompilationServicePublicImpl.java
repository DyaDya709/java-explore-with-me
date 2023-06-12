package ru.practicum.events.compilation.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ru.practicum.events.compilation.dto.CompilationDto;
import ru.practicum.events.compilation.mapper.CompilationMapper;
import ru.practicum.events.compilation.model.Compilation;
import ru.practicum.events.compilation.service.CompilationServicePublic;
import ru.practicum.events.compilation.storage.CompilationStorage;
import ru.practicum.exception.type.ResourceNotFoundException;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CompilationServicePublicImpl implements CompilationServicePublic {
    private final CompilationStorage compilationStorage;

    @Override
    public List<CompilationDto> getAllCompilations(Boolean pinned, int from, int size) {
        Pageable pageable = PageRequest.of(from, size);
        return compilationStorage.findAllByPinnedIs(pinned, pageable).stream()
                .map(CompilationMapper::toDto).collect(Collectors.toList());
    }

    @Override
    public CompilationDto getCompilationById(Long compId) {
        return CompilationMapper.toDto(getCompilationByIdInRepository(compId));
    }

    private Compilation getCompilationByIdInRepository(Long id) {
        return compilationStorage.findById(id).orElseThrow(()
                -> new ResourceNotFoundException("Подборка событий c id = " + id + " не найдена"));
    }
}
