package ua.com.integrity.smalltalkingbot.controller;

import ua.com.integrity.smalltalkingbot.bot.SmallTalkingBot;
import ua.com.integrity.smalltalkingbot.model.User;
import ua.com.integrity.smalltalkingbot.repository.UserRepository;

public class UserController {
    private SmallTalkingBot smallTalkingBot;

    public UserController(SmallTalkingBot smallTalkingBot) {
        this.smallTalkingBot = smallTalkingBot;
    }

    public void addUser(long chatId){
        UserRepository.getInstance().addUser(new User(chatId));
    }

    public User getUserByChatId(long chatId){
        return UserRepository.getInstance().getUserByChatId(chatId);

    }

    public Boolean userExists(long chatId){
        return UserRepository.getInstance().userExists(chatId);
    }
}
