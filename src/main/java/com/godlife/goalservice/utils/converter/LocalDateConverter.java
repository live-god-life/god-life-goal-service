package com.godlife.goalservice.utils.converter;

import org.springframework.core.convert.converter.Converter;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class LocalDateConverter implements Converter<String, LocalDate> {
    @Override
    public LocalDate convert(String text) {
        return LocalDate.parse(text, DateTimeFormatter.ofPattern("yyyyMMdd"));
    }
}
