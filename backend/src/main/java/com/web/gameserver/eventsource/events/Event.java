package com.web.gameserver.eventsource.events;


import lombok.AllArgsConstructor;
import org.json.JSONObject;

import java.time.LocalDateTime;

@AllArgsConstructor
public abstract class Event { // can't be instantiated
    protected final EventType eventType;
    protected final JSONObject data; // so client can JSON.parse data
    protected final LocalDateTime id = LocalDateTime.now();

    public JSONObject getData() {
        return this.data;
    }

    public String getId() {
        return id.toString();
    }

    public EventType getEventType() {
        return this.eventType;
    }
}
