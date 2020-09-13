package com.github.graywizard123.tgbot.event;

public interface ITGBotEvent {

    Class<? extends ITGBotEvent> getType();

}
