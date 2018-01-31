package ua.com.integrity.smalltalkingbot.controller;

import ua.com.integrity.smalltalkingbot.model.User;
import ua.com.integrity.smalltalkingbot.repository.UserRepository;

import java.util.List;

public class UserController {


    public UserController() {
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

    public List<User> getAllUsers(){
        return UserRepository.getInstance().getAllUsers();
    }

    public List<Long> getSubscribedChats(){
        return UserRepository.getInstance().getSubscribedChats();
    }
}
