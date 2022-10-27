package com.godlife.goalservice.utils.config;

import com.godlife.goalservice.utils.converter.LocalDateConverter;
import com.godlife.goalservice.utils.converter.YearMonthConverter;
import org.springframework.context.annotation.Configuration;
import org.springframework.format.FormatterRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    @Override
    public void addFormatters(FormatterRegistry registry) {
        registry.addConverter(new LocalDateConverter());
        registry.addConverter(new YearMonthConverter());
    }
}
