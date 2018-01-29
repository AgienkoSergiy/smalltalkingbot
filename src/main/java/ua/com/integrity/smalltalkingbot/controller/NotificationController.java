package ua.com.integrity.smalltalkingbot.controller;

import ua.com.integrity.smalltalkingbot.bot.SmallTalkingBot;
import ua.com.integrity.smalltalkingbot.message.MessageBuilder;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.HashSet;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import static java.time.temporal.ChronoUnit.MINUTES;

public class NotificationController {

    private SmallTalkingBot smallTalkingBot;
    private HashSet<Long> subscribedChats;

    public NotificationController(SmallTalkingBot smallTalkingBot) {
        this.smallTalkingBot = smallTalkingBot;
        subscribedChats = new HashSet<>();
        subscribedChats.add(410245108L);
        sendDailyNotification(11,30);
    }

    public void sendDailyNotification(int atHour, int atMinute){
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime next = getStartPoint(atHour, atMinute);
        ScheduledExecutorService executor = Executors.newSingleThreadScheduledExecutor();
        Runnable task = () -> {
            for (Long chatId :
                    subscribedChats) {
                smallTalkingBot.sendDefaultMessage(chatId, MessageBuilder.getGastroPrognosis());
            }
        };

        executor.scheduleAtFixedRate(task,now.until(next,MINUTES),1440,TimeUnit.MINUTES);
    }


    private LocalDateTime getStartPoint(int hour, int minute){
        LocalDateTime currentDateTime = LocalDateTime.now();
        LocalDateTime todayNotificationTime = LocalDateTime.of(LocalDate.now(), LocalTime.of(hour,minute));

        if(currentDateTime.isBefore(todayNotificationTime)){
            return LocalDateTime.of(LocalDate.now(), LocalTime.of(hour,minute));
        }
        else {
            return LocalDateTime.of(LocalDate.now().plusDays(1), LocalTime.of(hour,minute));
        }
    }

    public void sendUnpaidOrdersNotification(int atHour, int atMinute){
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime next = getStartPoint(atHour, atMinute);
        ScheduledExecutorService executor = Executors.newSingleThreadScheduledExecutor();
        Runnable task = () -> {
            for (Long chatId :
                    subscribedChats) {
                //smallTalkingBot.sendDefaultMessage(chatId, MessageBuilder.getGastroPrognosis());
            }
        };

        executor.scheduleAtFixedRate(task,now.until(next,MINUTES),1440,TimeUnit.MINUTES);
    }
}
