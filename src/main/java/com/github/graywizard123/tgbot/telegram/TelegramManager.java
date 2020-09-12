package com.github.graywizard123.tgbot.telegram;

import com.fasterxml.jackson.core.JsonParser;
import com.github.graywizard123.tgbot.App;
import org.json.JSONObject;
import org.telegram.telegrambots.ApiContextInitializer;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.exceptions.TelegramApiRequestException;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.Serializable;

public class TelegramManager {

    private static TelegramBot telegramBot;

    public static void init(){
        ApiContextInitializer.init();

        Reader tgBotDataReader = new InputStreamReader(TelegramManager.class.getResourceAsStream("/telegram/data.json"));
        StringBuilder tgBotDataRaw = new StringBuilder();
        try {
            while (tgBotDataReader.ready())
                tgBotDataRaw.append((char) tgBotDataReader.read());
        } catch (IOException e) {
            e.printStackTrace();
        }
        JSONObject object = new JSONObject(tgBotDataRaw.toString());
        telegramBot = new TelegramBot(object.getString("username"), object.getString("token"));
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

    public static <T extends Serializable, Method extends BotApiMethod<T>> void execute(Method action) {
        try {
            telegramBot.execute(action);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
