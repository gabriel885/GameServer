package com.web.gameserver.eventsource.events;

import com.web.gameserver.api.dto.ChessMoveDTO;
import org.json.JSONObject;

public class ChessGameMoveEvent extends Event {
    public ChessGameMoveEvent(ChessMoveDTO chessMoveDTO) {
        super(EventType.ChessGameMove, new JSONObject().put("gameMove", chessMoveDTO.toJson().toString()));
    }
}
