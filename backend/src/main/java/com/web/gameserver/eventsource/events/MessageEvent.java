package com.web.gameserver.eventsource.events;

import org.json.JSONObject;

public class MessageEvent extends Event {
    public MessageEvent(String message) {
        super(EventType.Message, new JSONObject().put("message", message));
    }
}
