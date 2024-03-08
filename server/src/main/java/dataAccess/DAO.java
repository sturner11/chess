package dataAccess;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.sql.SQLException;

public interface DAO{
    BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
    final String[] createStatements = {};
    void clear();

    default String encryptUserPassword(String password) {
        return encoder.encode(password);
    }

    default Boolean passwordMatch(String password, String encodedPassword) {
        return encoder.matches(password, encodedPassword);
    }
}
