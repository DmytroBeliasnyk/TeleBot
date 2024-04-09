package ua.kiev.prog.bot;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.objects.InputFile;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import ua.kiev.prog.model.CustomUser;
import ua.kiev.prog.service.UserService;

import java.io.InputStream;
import java.util.List;

@Component
@PropertySource("classpath:telegram.properties")
public class ChatBot extends TelegramLongPollingBot {

    private static final Logger LOGGER = LogManager.getLogger(ChatBot.class); //log4j
    private static final String BROADCAST = "broadcast ";
    private static final String LIST_USERS = "users";

    @Value("${bot.name}")
    private String botName;

    @Value("${bot.token}")
    private String botToken;

    private final UserService userService;

    public ChatBot(UserService userService) {
        this.userService = userService;
    }

    @Override
    public String getBotUsername() {
        return botName;
    }

    @Override
    public String getBotToken() {
        return botToken;
    }

    @Override
    public void onUpdateReceived(Update update) {
        if (!update.hasMessage() || !update.getMessage().hasText())
            return;

        final String text = update.getMessage().getText();
        final long chatId = update.getMessage().getChatId();

        CustomUser customUser = userService.findByChatId(chatId);

        if (checkIfAdminCommand(customUser, text))
            return;

        BotContext context;
        BotState state;

        // H -> Ph -> Em -> Th
        // 1 -> 2! -> 3! -> 4

        if (customUser == null) {
            state = BotState.getInitialState();

            customUser = new CustomUser(chatId, state.ordinal());
            userService.addUser(customUser);

            context = BotContext.of(this, customUser, text);
            state.enter(context);

            LOGGER.info("New user registered: " + chatId);
        } else {
            context = BotContext.of(this, customUser, text);
            state = BotState.byId(customUser.getStateId());

            LOGGER.info("Update received for user in state: " + state);
        }

        state.handleInput(context);

        // 1 -> 2 -> 3!
        do {
            state = state.nextState();
            state.enter(context);
        } while (!state.isInputNeeded());

        customUser.setStateId(state.ordinal());
        userService.updateUser(customUser);
    }

    private boolean checkIfAdminCommand(CustomUser customUser, String text) {
        if (customUser == null || !customUser.getAdmin())
            return false;

        if (text.startsWith(BROADCAST)) {
            LOGGER.info("Admin command received: " + BROADCAST);

            text = text.substring(BROADCAST.length());
            broadcast(text);

            return true;
        } else if (text.equals(LIST_USERS)) {
            LOGGER.info("Admin command received: " + LIST_USERS);

            listUsers(customUser);
            return true;
        }

        return false;
    }

    private void sendMessage(long chatId, String text) {
        SendMessage message = new SendMessage();
        message.setChatId(Long.toString(chatId));
        message.setText(text);
        try {
            execute(message);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    private void sendPhoto(long chatId) {
        InputStream is = getClass().getClassLoader()
                .getResourceAsStream("test.png");

        SendPhoto message = new SendPhoto();
        message.setChatId(Long.toString(chatId));
        message.setPhoto(new InputFile(is, "test"));
        try {
            execute(message);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    private void listUsers(CustomUser admin) {
        StringBuilder sb = new StringBuilder("All users list:\r\n");
        List<CustomUser> customUsers = userService.findAll();

        customUsers.forEach(user ->
                sb.append(user.getId())
                        .append(' ')
                        .append(user.getPhone())
                        .append(' ')
                        .append(user.getEmail())
                        .append("\r\n")
        );

        sendPhoto(admin.getChatId());
        sendMessage(admin.getChatId(), sb.toString());
    }

    private void broadcast(String text) {
        int page = 0;
        Page<CustomUser> users = null;
        do {
            users = userService.find(PageRequest.of(page, 1));
            users.forEach(user -> sendMessage(user.getChatId(), text));
            page++;
        } while (users.hasNext());
    }
}
