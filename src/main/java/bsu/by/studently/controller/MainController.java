package bsu.by.studently.controller;

import bsu.by.studently.dto.UserDto;
import bsu.by.studently.model.Post;
import bsu.by.studently.model.Role;
import bsu.by.studently.model.User;
import bsu.by.studently.repository.PostRepository;
import bsu.by.studently.service.PostService;
import bsu.by.studently.service.UserService;
import bsu.by.studently.util.FileUploadUtil;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

@RequestMapping
@Controller
public class MainController {
    @Autowired
    UserService userService;
    @Autowired
    PostRepository postRepository;

    @Autowired
    PostService postService;

    @GetMapping("/")
    public String getHomePage(Model model, @Param("keyword") String keyword){
        List<Post> posts = postService.listAll(keyword);
        model.addAttribute("posts", posts);
        model.addAttribute("keyword", keyword);
        return "home";
    }

    @GetMapping("/home")
    public String getHomePage2(Model model, @Param("keyword") String keyword){
        List<Post> posts = postService.listAll(keyword);
        model.addAttribute("posts", posts);
        model.addAttribute("keyword", keyword);
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
            request.getSession().setAttribute("user", new UserDto(savedUser));
            return "redirect:/profile";
        } catch (Exception e) {
            model.addAttribute("registerError", e.getMessage());
            return "register";
        }
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


    @ModelAttribute("loginedUser")
    public UserDto getLoginedUser(HttpServletRequest request){
        return (UserDto) request.getSession().getAttribute("user");
    }


}
