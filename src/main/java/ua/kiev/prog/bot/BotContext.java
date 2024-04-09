package ua.kiev.prog.bot;

import ua.kiev.prog.model.CustomUser;

public class BotContext {
    private final ChatBot bot;
    private final CustomUser customUser;
    private final String input;

    public static BotContext of(ChatBot bot, CustomUser customUser, String text) {
        return new BotContext(bot, customUser, text);
    }

    private BotContext(ChatBot bot, CustomUser customUser, String input) {
        this.bot = bot;
        this.customUser = customUser;
        this.input = input;
    }

    public ChatBot getBot() {
        return bot;
    }

    public CustomUser getUser() {
        return customUser;
    }

    public String getInput() {
        return input;
    }
}
