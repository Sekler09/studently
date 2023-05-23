package bsu.by.studently.controller;

import bsu.by.studently.dto.UserDto;
import bsu.by.studently.model.Role;
import bsu.by.studently.model.User;
import bsu.by.studently.service.UserService;
import bsu.by.studently.util.FileUploadUtil;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.nio.file.Files;
import java.nio.file.Paths;

@RequestMapping
@Controller
public class MainController {
    private final UserService userService;

    public MainController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/")
    public String getHomePage(){
        return "home";
    }

    @GetMapping("/home")
    public String getHomePage2(){
        return "home";
    }

    @GetMapping("/register")
    public String getRegistrationPage(Model model) {
        model.addAttribute("suspect", new User());
        return "register";
    }

    @PostMapping("/register")
    public String postUser(@ModelAttribute("suspect") User suspect, @RequestParam("image") MultipartFile multipartFile, HttpServletRequest request, Model model) {
        try {
            String fileName = StringUtils.cleanPath(multipartFile.getOriginalFilename());
            suspect.setPhoto(fileName);
            User savedUser = userService.saveUser(suspect);
            String uploadDir = "user-photos/" +savedUser.getId();
            if(!fileName.equals("")){
                FileUploadUtil.saveFile(uploadDir, fileName, multipartFile);
            } else {
                Files.createDirectories(Paths.get(uploadDir));
            }
        } catch (Exception e) {
            model.addAttribute("registerError", e.getMessage());
            return "register";
        }

        return "redirect:/";
    }

    @GetMapping("/login")
    public String getLoginPage(HttpServletRequest request ,Model model){
        UserDto user = (UserDto) request.getSession().getAttribute("user");
        if(user != null){
            return "redirect:/profile";
        }
        model.addAttribute("suspect", new User());
        return "login";
    }

    @PostMapping("/login")
    public String doLogin(@ModelAttribute("suspect") User suspect, HttpServletRequest request, RedirectAttributes redirectAttrs) {
        try {
            UserDto user = userService.authenticate(suspect.getEmail(), suspect.getPassword());
            request.getSession().setAttribute("user", user);
            return "redirect:/profile";
        } catch (Exception e) {
            redirectAttrs.addAttribute("error", e.getMessage());
            return "redirect:/login";
        }
    }

    @GetMapping("/profile")
    public String getProfile(HttpServletRequest request, Model model) {
        UserDto user = (UserDto) request.getSession().getAttribute("user");
        if(user == null){
            return "redirect:/login";
        }
        model.addAttribute("profiler", user);
        return "profile";
    }

    @GetMapping("/logout")
    public String logout(HttpServletRequest request){
        request.getSession().removeAttribute("user");
        return "redirect:/";
    }

    @PostMapping("/profile/setAuthor")
    public String setAdmin(HttpServletRequest request) {
        try {
            UserDto user = (UserDto) request.getSession().getAttribute("user");
            user = userService.setAuthorRole(user.getId());
            request.getSession().setAttribute("user", user);

            return "redirect:/profile";
        } catch (Exception e) {
            return "redirect:/profile";
        }
    }


    @ModelAttribute("profileLink")
    public String profileLink(HttpServletRequest request) {
        UserDto user = (UserDto) request.getSession().getAttribute("user");
        return user != null ? "/profile" : null;
    }



}
