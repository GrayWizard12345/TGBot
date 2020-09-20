package com.github.graywizard123.tgbot.telegram;

import com.github.graywizard123.tgbot.App;
import com.github.graywizard123.tgbot.db.models.CategoryModel;
import com.github.graywizard123.tgbot.db.models.MealModel;
import com.github.graywizard123.tgbot.db.models.UserModel;
import com.github.graywizard123.tgbot.db.repositories.CategoryRepository;
import com.github.graywizard123.tgbot.db.repositories.MealRepository;
import com.github.graywizard123.tgbot.db.repositories.UserRepository;
import com.github.graywizard123.tgbot.telegram.command.CommandExecutor;
import com.github.graywizard123.tgbot.telegram.command.condition.ICondition;

import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.*;

public class TelegramBot extends TelegramLongPollingBot {

    private final String token;
    private final String username;

    private final HashMap<Long, TelegramUser> users = new HashMap<>();
    private final HashMap<ICondition, CommandExecutor> commands = new HashMap<>();

    public static final String WELCOME_MESSAGE = "Hello";

    public TelegramBot(String username, String token) {
        this.username = username;
        this.token = token;

        addCommand(context -> context.getMessage().hasText() && context.getMessage().getText().startsWith("/menu"), this::openMainMenu);
        addCommand(context -> {
            TelegramUser user = users.get(context.getMessage().getChatId());
            return user.getStage() == ClientStage.IN_MAIN_MENU && !context.getMessage().getText().equals("Order");
        }, this::openCategory);
        addCommand(context -> {
            TelegramUser user = users.get(context.getMessage().getChatId());
            return user.getStage() == ClientStage.IN_CATEGORY_MENU;
        }, this::addMeal);

        //Add welcome message
        addCommand(
                ctx -> ctx.hasMessage() && ctx.getMessage().hasText() && ctx.getMessage().getText().equals("/start"),
                context -> {
                    SendMessage sendWelcomeMessage = new SendMessage(context.getMessage().getChatId(), WELCOME_MESSAGE);

                    try {
                        execute(sendWelcomeMessage);
                    } catch (TelegramApiException e) {
                        e.printStackTrace();
                    }

                    this.openMainMenu(context);
                });

        //Debug command #TODO remove
        addCommand(
                context -> context.hasMessage() && context.getMessage().hasText() && context.getMessage().getText().equals("/get_chat_id"),
                context -> {
                    try {
                        execute(new SendMessage(context.getMessage().getChatId(), String.valueOf(context.getMessage().getChatId())));
                    } catch (TelegramApiException e) {
                        e.printStackTrace();
                    }
                }
        );
    }

    private void addMeal(Update update) {
        MealModel meal = MealRepository.getByName(update.getMessage().getText());
        TelegramUser user = users.get(update.getMessage().getChatId());
        user.setSelectedMealId(meal.getId());

        ReplyKeyboardMarkup numpadMarkup = new ReplyKeyboardMarkup();
        List<KeyboardRow> numpadRows = new ArrayList<>();

        for (int y = 0;y < 3;y++){
            KeyboardRow row = new KeyboardRow();
            for (int x = 1;x <= 3;x++){
                row.add(new KeyboardButton(String.valueOf(y*3+x)));
            }
            numpadRows.add(row);
        }

        numpadMarkup.setKeyboard(numpadRows);

        try {
            execute(new SendMessage().setText(meal.getName()).setChatId(update.getMessage().getChatId()).setReplyMarkup(numpadMarkup));
            user.setStage(ClientStage.SET_MEALS_COUNT);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    private void openCategory(Update context) {
        CategoryModel category = CategoryRepository.getByTitle(context.getMessage().getText());
        List<MealModel> meals = category.getMeals();

        ReplyKeyboardMarkup categoryMenu = new ReplyKeyboardMarkup();
        List<KeyboardRow> categoryMenuRows = new ArrayList<>();

        for (MealModel meal : meals) {
            KeyboardRow row = new KeyboardRow();
            row.add(new KeyboardButton(meal.getName()));
            categoryMenuRows.add(row);
        }

        categoryMenu.setKeyboard(categoryMenuRows);

        try {
            execute(new SendMessage().setText(category.getTitle()).setReplyMarkup(categoryMenu).setChatId(context.getMessage().getChatId()));
            users.get(context.getMessage().getChatId()).setStage(ClientStage.IN_CATEGORY_MENU);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    private void openMainMenu(Update context) {
        ReplyKeyboardMarkup mainMenu = new ReplyKeyboardMarkup();
        List<KeyboardRow> mainMenuRows = new ArrayList<>();
        List<CategoryModel> categories = CategoryRepository.getAll();

        for (CategoryModel category : categories) {
            KeyboardRow row = new KeyboardRow();
            row.add(new KeyboardButton(category.getTitle()));
            mainMenuRows.add(row);
        }

        KeyboardRow row = new KeyboardRow();
        row.add(new KeyboardButton("Order"));
        mainMenuRows.add(row);

        mainMenu.setKeyboard(mainMenuRows);

        try {
            execute(new SendMessage().setText("Menu").setReplyMarkup(mainMenu).setChatId(context.getMessage().getChatId()));
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    /*
    * Use this to add action for bot
    *
    * @param condition - receive ICondition
    * @param command - receive CommandExecutor
    */
    public void addCommand(ICondition condition, CommandExecutor command) {
        commands.put(condition, command);
        App.LOGGER.info("Added command in telegram bot");
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

        if (UserRepository.getByTelegramId(message.getChatId()) == null) {
            UserModel newUserModel = new UserModel(0, message.getChatId(), null, null);
            UserRepository.add(newUserModel);
        }

        if (!users.containsKey(message.getChatId())) {
            users.put(message.getChatId(), new TelegramUser(message.getChatId(), ClientStage.IN_MAIN_MENU));
            openMainMenu(update);
        }

        if(message.hasText()){
            for (ICondition condition : commands.keySet()) {
                if (condition.check(update)) {
                    commands.get(condition).run(update);
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
