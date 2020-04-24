package com.web.gameserver.utils;

import com.web.gameserver.api.model.GameStatus;
import org.springframework.core.convert.converter.Converter;

public class GameStatusStringToEnumConverter implements Converter<String, GameStatus> {
    @Override
    public GameStatus convert(String source) {
        return GameStatus.valueOf(source.toUpperCase());
    }
}
