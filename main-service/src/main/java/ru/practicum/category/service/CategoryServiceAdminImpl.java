package ru.practicum.category.service;

import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import ru.practicum.category.dto.CategoryDto;
import ru.practicum.category.dto.NewCategoryDto;
import ru.practicum.category.mapper.CategoryMapper;
import ru.practicum.category.model.Category;
import ru.practicum.category.storage.CategoryRepository;
import ru.practicum.events.event.model.Event;
import ru.practicum.events.event.storage.EventRepository;
import ru.practicum.exception.CustomBadRequestException;
import ru.practicum.exception.CustomConflictDeleteException;
import ru.practicum.exception.CustomConflictNameCategoryException;
import ru.practicum.exception.CustomResourceNotFoundException;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CategoryServiceAdminImpl implements CategoryServiceAdmin {
    private final CategoryRepository categoryRepository;
    private final EventRepository eventRepository;

    @Override
    public CategoryDto addCategory(NewCategoryDto newCategoryDto) {
        Category category = CategoryMapper.newCategoryDtoToEntity(newCategoryDto);
        return getCategoryDto(category, newCategoryDto.getName());
    }

    @Override
    public void deleteCategoryById(Long catId) {
        Category category = getCategoryById(catId);
        if (isRelatedEvent(category)) {
            throw new CustomConflictDeleteException("Существуют события, связанные с категорией " + category.getName());
        }
        categoryRepository.delete(category);
    }

    @Override
    public CategoryDto updateCategory(Long catId, CategoryDto categoryDto) {
        Category category = getCategoryById(catId);
        category.setId(catId);
        category.setName(categoryDto.getName());
        return getCategoryDto(category, category.getName());
    }

    private CategoryDto getCategoryDto(Category category, String name) {
        try {
            return CategoryMapper.toDto(categoryRepository.save(category));
        } catch (DataIntegrityViolationException e) {
            throw new CustomConflictNameCategoryException("Имя категории должно быть уникальным, "
                    + name + " уже используется");
        } catch (Exception e) {
            throw new CustomBadRequestException("Запрос на добавлении категории " + name + " составлен не корректно ");
        }
    }

    private Category getCategoryById(Long id) {
        return categoryRepository.findById(id).orElseThrow(()
                -> new CustomResourceNotFoundException("Категория c id = " + id + " не найдена"));
    }

    private boolean isRelatedEvent(Category category) {
        List<Event> findEventByCategory = eventRepository.findEventByCategoryIs(category);
        return !findEventByCategory.isEmpty();
    }
}
