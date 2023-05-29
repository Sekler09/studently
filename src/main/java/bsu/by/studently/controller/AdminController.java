package bsu.by.studently.controller;

import bsu.by.studently.dto.UserDto;
import bsu.by.studently.model.User;
import bsu.by.studently.repository.UserRepository;
import bsu.by.studently.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;


@Controller
public class AdminController {

    @Autowired
    UserRepository userRepository;

    @Autowired
    UserService userService;
    @GetMapping("/users")
    public  String getUsers(Model model, HttpServletRequest request){
        UserDto user = (UserDto) request.getSession().getAttribute("user");
        if(user!= null && user.hasRole("ADMIN")){
            model.addAttribute("users", userRepository.findAll());
            return "users";
        }
        return "redirect:/";

    }

    @PostMapping("/profile/setAdmin")
    public String becomeAdmin(HttpServletRequest request){
        try {
            UserDto user = (UserDto) request.getSession().getAttribute("user");
            user = userService.setAdminRole(user.getId());
            request.getSession().setAttribute("user", user);

            return "redirect:/profile";
        } catch (Exception e) {
            return "redirect:/profile";
        }
    }

    @PostMapping("/user/{id}/delete")
    public String deleteUser(@PathVariable("id") Long id, HttpServletRequest request, Model model){
        UserDto userDto = (UserDto) request.getSession().getAttribute("user");
        if(userDto!= null && userDto.hasRole("ADMIN")){
            User user = userRepository.findById(id).orElse(null);
            if(user != null){
                userRepository.deleteById(id);
            }
            return "redirect:/users";
        }
        return "redirect:/";
    }
    @ModelAttribute("loginedUser")
    public UserDto getLoginedUser(HttpServletRequest request){
        return (UserDto) request.getSession().getAttribute("user");
    }
}
