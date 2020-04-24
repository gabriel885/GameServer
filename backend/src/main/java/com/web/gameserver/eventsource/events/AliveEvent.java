package com.web.gameserver.eventsource.events;

import org.json.JSONObject;

public class AliveEvent extends Event {
    public AliveEvent(String username) {
        super(EventType.ALIVE, new JSONObject().put("username", username));
    }
}
