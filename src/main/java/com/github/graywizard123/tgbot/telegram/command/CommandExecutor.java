package com.github.graywizard123.tgbot.telegram.command;

import org.telegram.telegrambots.meta.api.objects.Update;

public interface CommandExecutor {

    void run(Update context);

}
