package ua.com.integrity.smalltalkingbot.repository;

import java.util.HashMap;

public class OrderRepository {

    private OrderRepository(){
    }

    private static class OrderRepositoryHelper{
        private static final OrderRepository INSTANCE = new OrderRepository();
    }

    public static OrderRepository getInstance(){
        return OrderRepository.OrderRepositoryHelper.INSTANCE;
    }
}
