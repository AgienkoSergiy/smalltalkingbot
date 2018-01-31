package ua.com.integrity.smalltalkingbot.repository;

import ua.com.integrity.smalltalkingbot.model.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import static ua.com.integrity.smalltalkingbot.util.DBUtil.getConnection;

public class UserRepository {

    /**
     * Queries
     * */
    private static final String ADD_USER_QUERY = "INSERT INTO SMALLTALKINGDB.PUBLIC.USER (" +
            "CHAT_ID, PHONE_NUMBER, CALORICITY_LIMIT) VALUES (?, ?, ?);";

    private static final String GET_USER_QUERY = "SELECT * FROM SMALLTALKINGDB.PUBLIC.USER WHERE CHAT_ID=?;";

    private static final String USER_EXISTS_QUERY = "SELECT EXISTS (SELECT * FROM SMALLTALKINGDB.PUBLIC.USER WHERE CHAT_ID=?);";

    private static final String GET_ALL_USERS_QUERY = "SELECT * FROM SMALLTALKINGDB.PUBLIC.USER;";

    private static final String GET_SUBSCRIBED_CHATS_QUERY = "SELECT USER.CHAT_ID FROM SMALLTALKINGDB.PUBLIC.USER WHERE SUBSCRIBED=TRUE;";




    private static class UserRepositoryHelper{
        private static final UserRepository INSTANCE = new UserRepository();
    }

    public static UserRepository getInstance(){
        return UserRepositoryHelper.INSTANCE;
    }



    public void addUser(User user){
        try (Connection conn = getConnection()) {
            try (PreparedStatement st = conn.prepareStatement(ADD_USER_QUERY)) {
                st.setLong(1, user.getChatId());
                st.setString(2, user.getProneNumber());
                st.setDouble(3,user.getCaloricityLimit());
                st.execute();
                conn.commit();
            } catch (SQLException e) {
                conn.rollback();
                throw new RuntimeException("Fail to add user ", e);
            }
        } catch (SQLException e1) {
            throw new RuntimeException("Connection problem occurred ", e1);
        }

    }

    public User getUserByChatId(long chatId) {
        User user = new User(chatId);
        try (Connection conn = getConnection()) {
            try (PreparedStatement st = conn.prepareStatement(GET_USER_QUERY)) {
                st.setLong(1,chatId);
                ResultSet rs = st.executeQuery();
                while(rs.next()){
                    user.setProneNumber(rs.getString("PHONE_NUMBER"));
                    user.setCaloricityLimit(rs.getDouble("CALORICITY_LIMIT"));
                }
            } catch (SQLException e) {
                conn.rollback();
                throw new RuntimeException("Fail to retrieve user ", e);
            }
        } catch (SQLException e1) {
            throw new RuntimeException("Connection problem occurred ", e1);
        }
        return user;
    }

    public Boolean userExists(long chatId){
        try (Connection conn = getConnection()) {
            try (PreparedStatement st = conn.prepareStatement(USER_EXISTS_QUERY)) {
                st.setLong(1,chatId);
                ResultSet rs = st.executeQuery();
                return rs.next();
            } catch (SQLException e) {
                conn.rollback();
                throw new RuntimeException("Fail to retrieve user ", e);
            }
        } catch (SQLException e1) {
            throw new RuntimeException("Connection problem occurred ", e1);
        }
    }

    public List<User> getAllUsers() {
        ArrayList<User> users = new ArrayList<>();
        try (Connection conn = getConnection()) {
            try (PreparedStatement st = conn.prepareStatement(GET_ALL_USERS_QUERY)) {
                ResultSet rs = st.executeQuery();
                while(rs.next()){
                    User user = new User(rs.getLong("CHAT_ID"),
                                         rs.getString("PHONE_NUMBER"),
                                         rs.getDouble("CALORICITY_LIMIT"));
                    users.add(user);
                }
            } catch (SQLException e) {
                conn.rollback();
                throw new RuntimeException("Fail to retrieve users ", e);
            }
        } catch (SQLException e1) {
            throw new RuntimeException("Connection problem occurred ", e1);
        }
        return users;
    }

    public List<Long> getSubscribedChats(){
        ArrayList<Long> chats = new ArrayList<>();
        try (Connection conn = getConnection()) {
            try (PreparedStatement st = conn.prepareStatement(GET_SUBSCRIBED_CHATS_QUERY)) {
                ResultSet rs = st.executeQuery();
                while(rs.next()){
                    chats.add(rs.getLong("CHAT_ID"));
                }
            } catch (SQLException e) {
                conn.rollback();
                throw new RuntimeException("Fail to retrieve users ", e);
            }
        } catch (SQLException e1) {
            throw new RuntimeException("Connection problem occurred ", e1);
        }
        return chats;
    }
}
