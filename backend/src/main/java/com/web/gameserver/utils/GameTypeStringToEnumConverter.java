package com.web.gameserver.utils;


import com.web.gameserver.api.model.GameType;
import org.springframework.core.convert.converter.Converter;

public class GameTypeStringToEnumConverter implements Converter<String, GameType> {
    @Override
    public GameType convert(String source) {
        return GameType.valueOf(source.toUpperCase());
    }
}
