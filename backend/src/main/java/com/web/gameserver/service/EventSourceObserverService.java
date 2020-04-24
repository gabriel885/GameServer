package com.web.gameserver.service;

import com.web.gameserver.eventsource.events.Event;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Service
public class EventSourceObserverService {

    private static final Logger logger = LoggerFactory.getLogger(EventSourceObserverService.class);

    // thread pool to emit sse events
    private static final ExecutorService sseMvcExecutor = Executors.newSingleThreadExecutor(); // sequent executor, unbounded queue

    // emitters per game ID
    private static final ConcurrentHashMap<Long, CopyOnWriteArrayList<SseEmitter>> emitters = new ConcurrentHashMap<>();

    /**
     * delete all emitters per game
     *
     * @param gameId
     */
    public static void deleteEmitters(Long gameId) {
        if (emitters.containsKey(gameId)) {
            emitters.remove(gameId);
            logger.info(String.format("deleted all SSE emitters for game %d", gameId));
        }
    }

    /**
     * send event to opponent in gameId
     *
     * @param gameId
     * @param event
     */
    public static void send(Long gameId, Event event) {
        List<SseEmitter> deadEmitters = new ArrayList<>();

        // if no emitters exist -> don't send event
        if (!emitters.containsKey(gameId)) {
            return;
        }
        // get all emitters per game
        List<SseEmitter> gameEmitters = emitters.get(gameId);

        // check the emitters exist
        if (gameEmitters == null) {
            return;
        }

        // send event to opponent
        gameEmitters.forEach(sseEmitter -> {
            // spawn new thread to emit message
            sseMvcExecutor.execute(() -> {
                // run()
                String eventId = event.getId();
                String eventData = event.getData().toString();
                String eventName = event.getEventType().toString();

                try {
                    logger.info(String.format("Sending event %s", event.getData().toString()));
                    sseEmitter.send(SseEmitter.event()
                            .id(eventId)
                            .name(eventName)
                            .data(eventData));

                } catch (Exception e) {
                    sseEmitter.complete();
                    deadEmitters.add(sseEmitter);
                }
                // remove dead emitters
                gameEmitters.removeAll(deadEmitters);
            });

        });


    }

    /**
     * register new emitter for game Id
     *
     * @param gameId
     * @return
     */
    public SseEmitter register(Long gameId) {
        if (!emitters.containsKey(gameId)) {
            // add list of emitters
            emitters.put(gameId, new CopyOnWriteArrayList<>());
        }

        // only opponents should acquire emittor
        // each opponent has his own emittor

        SseEmitter emitter = new SseEmitter(86400000L); // keep connection open for 180 seconds
        emitter.onCompletion(() -> emitters.remove(emitter));
        emitter.onTimeout(() -> emitters.remove(emitter));

        emitters.get(gameId).add(emitter);

        return emitter;
    }


}
