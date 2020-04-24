package com.web.gameserver.config;

import com.web.gameserver.utils.GameStatusStringToEnumConverter;
import com.web.gameserver.utils.GameTypeStringToEnumConverter;
import org.springframework.context.annotation.Configuration;
import org.springframework.format.FormatterRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {
    /**
     * String to ENUM converters
     *
     * @param registry
     */
    @Override
    public void addFormatters(FormatterRegistry registry) {
        registry.addConverter(new GameStatusStringToEnumConverter());
        registry.addConverter(new GameTypeStringToEnumConverter());
    }
}