package com.github.graywizard123.tgbot.event;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class EventManager {

    private static final HashMap<Class<? extends ITGBotEvent>, List<TGBotEventListener>> listeners = new HashMap<>();

    public static void registerListener(Class<? extends ITGBotEvent> event, TGBotEventListener listener) {
        if (!listeners.containsKey(event)) {
            listeners.put(event, new ArrayList<>());
        }
        listeners.get(event).add(listener);
    }

    public static HashMap<Class<? extends ITGBotEvent>, List<TGBotEventListener>> getListeners() {
        return listeners;
    }

    public static void callEvent(ITGBotEvent event) {
        listeners.get(event.getType()).forEach(listener -> listener.onEvent(event));
    }

}
