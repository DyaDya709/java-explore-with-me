package ru.practicum.category.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.category.dto.CategoryDto;
import ru.practicum.category.dto.NewCategoryDto;
import ru.practicum.category.mapper.CategoryMapper;
import ru.practicum.category.model.Category;
import ru.practicum.category.service.CategoryServiceAdmin;
import ru.practicum.category.storage.CategoryRepository;
import ru.practicum.events.event.model.Event;
import ru.practicum.events.event.storage.EventRepository;
import ru.practicum.exception.type.ConflictDeleteException;
import ru.practicum.exception.type.ResourceNotFoundException;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CategoryServiceAdminImpl implements CategoryServiceAdmin {
    private final CategoryRepository categoryRepository;
    private final EventRepository eventRepository;

    @Transactional
    @Override
    public CategoryDto createCategory(NewCategoryDto newCategoryDto) {
        Category category = CategoryMapper.newCategoryDtoToCategory(newCategoryDto);
        return CategoryMapper.toDto(categoryRepository.save(category));
    }

    @Transactional
    @Override
    public void deleteCategoryById(Long catId) {
        Category category = getCategoryById(catId);
        if (isRelatedEvent(category)) {
            throw new ConflictDeleteException("Существуют события, связанные с категорией " + category.getName());
        }
        categoryRepository.delete(category);
    }

    @Transactional
    @Override
    public CategoryDto updateCategory(Long catId, CategoryDto categoryDto) {
        Category category = getCategoryById(catId);
        category.setId(catId);
        category.setName(categoryDto.getName());
        return CategoryMapper.toDto(categoryRepository.save(category));
    }


    private Category getCategoryById(Long id) {
        return categoryRepository.findById(id).orElseThrow(()
                -> new ResourceNotFoundException("Категория c id = " + id + " не найдена"));
    }

    private boolean isRelatedEvent(Category category) {
        List<Event> findEventByCategory = eventRepository.findEventByCategoryIs(category);
        return !findEventByCategory.isEmpty();
    }
}