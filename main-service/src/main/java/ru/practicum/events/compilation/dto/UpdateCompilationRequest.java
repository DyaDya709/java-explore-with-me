package ru.practicum.events.compilation.dto;

import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.Size;
import java.util.List;

@Builder
@Data
public class UpdateCompilationRequest {
    List<Long> events; //Список id событий подборки для полной замены текущего списка
    Boolean pinned; //Закреплена ли подборка на главной странице сайта
    @Size(max = 500, message = "Максимальное кол-во символов для описания: 500")
    String title; // Заголовок подборки
}
