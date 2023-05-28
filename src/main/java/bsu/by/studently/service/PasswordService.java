package bsu.by.studently.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class PasswordService {

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;


    public String hashPassword(String password) {
        return passwordEncoder.encode(password);
    }
    public boolean checkPassword(String password, String passwordHash) {
        return passwordEncoder.matches(password, passwordHash);
    }
}
