package ua.com.integrity.smalltalkingbot.repository;

import ua.com.integrity.smalltalkingbot.model.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import static ua.com.integrity.smalltalkingbot.util.DBUtil.getConnection;

public class UserRepository {
    private static final String ADD_USER_QUERY = "INSERT INTO SMALLTALKINGDB.PUBLIC.USER (" +
            "CHAT_ID, PHONE_NUMBER, CALORICITY_LIMIT) VALUES (?, ?, ?);";

    private static final String GET_USER_QUERY = "SELECT * FROM SMALLTALKINGDB.PUBLIC.USER WHERE CHAT_ID=?;";

    private static final String USER_EXISTS_QUERY = "SELECT EXISTS (SELECT * FROM SMALLTALKINGDB.PUBLIC.USER WHERE CHAT_ID=?);";




    private static class UserRepositoryHelper{
        private static final UserRepository INSTANCE = new UserRepository();
    }

    public static UserRepository getInstance(){
        return UserRepositoryHelper.INSTANCE;
    }

    public void addUser(User user){
        try (Connection conn = getConnection()) {
            try (PreparedStatement st = conn.prepareStatement(ADD_USER_QUERY)) {
                st.setInt(1, user.getChatId().intValue());
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
                st.setInt(1,(int)chatId);
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
                st.setInt(1,(int)chatId);
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


}
