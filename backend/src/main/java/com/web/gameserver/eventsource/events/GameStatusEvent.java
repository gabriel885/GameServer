package com.web.gameserver.eventsource.events;

import com.web.gameserver.api.model.GameStatus;
import org.json.JSONObject;

public class GameStatusEvent extends Event {
    public GameStatusEvent(GameStatus gameStatus) {
        super(EventType.GameStatus, new JSONObject().put("gameStatus", gameStatus.toString()));
    }
}
