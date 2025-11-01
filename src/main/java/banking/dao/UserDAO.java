package banking.dao;

import banking.model.User;
import java.util.List;

public interface UserDAO {
    void saveUser(User user);
    User findUserByUsername(String username);
    List<User> findAllUsers();
    void updateUser(User user);
    boolean validateCredentials(String username, String password);
}