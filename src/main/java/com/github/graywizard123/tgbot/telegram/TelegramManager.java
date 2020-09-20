package com.github.graywizard123.tgbot.telegram;

import com.github.graywizard123.tgbot.App;
import com.github.graywizard123.tgbot.telegram.command.CommandExecutor;
import com.github.graywizard123.tgbot.telegram.command.condition.ICondition;

import org.telegram.telegrambots.ApiContextInitializer;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.exceptions.TelegramApiRequestException;

import java.io.*;
import java.util.Properties;

public class TelegramManager {

    private static TelegramBot telegramBot;
    public static final String CONFIG_FILE_PATH = "./cfg/telegram.properties";

    private static void init(){
        ApiContextInitializer.init();

        Properties config = new Properties();
        try {
            config.load(new FileInputStream(CONFIG_FILE_PATH));
            telegramBot = new TelegramBot(config.getProperty("username"), config.getProperty("token"));
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(1);
        }
    }

    public static void start(){
        App.LOGGER.debug("Creating telegram bot");
        init();
        TelegramBotsApi telegramBotsApi = new TelegramBotsApi();
        try {
            telegramBotsApi.registerBot(telegramBot);
        } catch (TelegramApiRequestException e) {
            App.LOGGER.error("Cannot create telegram bot");
            e.printStackTrace();
            System.exit(1);
        }
        App.LOGGER.info("Telegram bot created");
    }

    public static void addCommand(ICondition condition, CommandExecutor command) {
        telegramBot.addCommand(condition, command);
    }

    public static TelegramUser getUser(long chatId){
        return telegramBot.getUser(chatId);
    }

    public static void updateUser(TelegramUser user) {
        telegramBot.updateUser(user);
    }

    public static <T extends Serializable, Method extends BotApiMethod<T>> void execute(Method action) {
        try {
            telegramBot.execute(action);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
