package bsu.by.studently.service;

import bsu.by.studently.dto.UserDto;
import bsu.by.studently.model.Role;
import bsu.by.studently.model.User;
import bsu.by.studently.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final PasswordService passwordService;
    private final RoleService roleService;

    public UserService(UserRepository userRepository,
                       PasswordService passwordService, RoleService roleService) {
        this.userRepository = userRepository;
        this.passwordService = passwordService;
        this.roleService = roleService;
    }

    public User saveUser(User user) throws Exception {
        try {
            if (userRepository.findByEmail(user.getEmail()) != null) {
                throw new Exception("User with this email already exists!");
            }
            String encodedPassword = this.passwordService.hashPassword(user.getPassword());
            user.setPassword(encodedPassword);
            roleService.setUserRole(user);
            return userRepository.save(user);

        } catch (Exception e) {
//            throw new Exception("Unable to register user. Please try again later.");
            throw new Exception(e.getMessage());
        }
    }

    public UserDto authenticate(String email, String password) throws Exception {
        User user = userRepository.findByEmail(email);

        if (user == null) {
            throw new Exception("Invalid email or password");
        }

        if (!this.passwordService.checkPassword(password, user.getPassword())) {
            throw new Exception("Invalid email or password");
        }


        return new UserDto(user);
    }

    public UserDto setAuthorRole(Long id) throws Exception {
        Optional<User> optionalUser = userRepository.findById(id);

        if (optionalUser.isEmpty()) {
            throw new Exception("Unable to change user's role. Please try again later.");
        }

        User user = optionalUser.get();
        roleService.setAuthorRole(user);
        User savedUser = userRepository.save(user);

        return new UserDto(savedUser);
    }

    public UserDto setAdminRole(Long id) throws Exception {
        Optional<User> optionalUser = userRepository.findById(id);

        if (optionalUser.isEmpty()) {
            throw new Exception("Unable to change user's role. Please try again later.");
        }

        User user = optionalUser.get();
        roleService.setAdminRole(user);
        User savedUser = userRepository.save(user);

        return new UserDto(savedUser);
    }
}
