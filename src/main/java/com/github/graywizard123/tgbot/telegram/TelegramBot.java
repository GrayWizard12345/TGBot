package com.github.graywizard123.tgbot.telegram;

import com.github.graywizard123.tgbot.telegram.command.CommandExecutor;
import com.github.graywizard123.tgbot.telegram.command.condition.ICondition;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.HashMap;

public class TelegramBot extends TelegramLongPollingBot {

    private final String token;
    private final String username;

    private final HashMap<Long, TelegramUser> users = new HashMap<>();
    private final HashMap<ICondition, CommandExecutor> commands = new HashMap<>();

    public static final String WELCOME_MESSAGE = "Hello";

    public TelegramBot(String username, String token) {
        this.username = username;
        this.token = token;
    }

    public void addCommand(ICondition condition, CommandExecutor command) {
        commands.put(condition, command);
    }

    public TelegramUser getUser(long chatId) {
        return users.get(chatId);
    }

    public void updateUser(TelegramUser user) {
        users.replace(user.getId(), user);
    }

    @Override
    public void onUpdateReceived(Update update) {
        Message message = update.getMessage();

        if(message.hasText()){
            String text = message.getText();

            if (text.equals("/start")) {
                SendMessage sendWelcomeMessage = new SendMessage(message.getChatId(), WELCOME_MESSAGE);
                try {
                    execute(sendWelcomeMessage);
                } catch (TelegramApiException e) {
                    e.printStackTrace();
                }
            } else {
                for (ICondition condition : commands.keySet()) {
                    if (condition.check(update)) {
                        commands.get(condition).run(update);
                    }
                }
            }
        }
    }

    @Override
    public String getBotUsername() {
        return username;
    }

    @Override
    public String getBotToken() {
        return token;
    }
}
