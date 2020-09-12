package com.github.graywizard123.tgbot.telegram.command.condition;

import org.telegram.telegrambots.meta.api.objects.Update;

public interface ICondition {

    boolean check(Update context);

}
