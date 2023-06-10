package ru.practicum.util;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import ru.practicum.exception.CustomValidationDateException;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class DateFormatter {
    private static final String DATE_FORMAT = "yyyy-MM-dd HH:mm:ss";
    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern(DATE_FORMAT);

    public static LocalDateTime formatDate(String date) {
        if (date == null) {
            throw new CustomValidationDateException("Дата должна быть задана");
        }
        LocalDateTime newDate;
        try {
            newDate = LocalDateTime.parse(date, formatter);
        } catch (DateTimeParseException e) {
            throw new CustomValidationDateException("Неверный формат даты");
        }
        return newDate;
    }
}
