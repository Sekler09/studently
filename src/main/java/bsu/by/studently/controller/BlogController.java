package bsu.by.studently.controller;

import bsu.by.studently.dto.UserDto;
import bsu.by.studently.model.Post;
import bsu.by.studently.model.User;
import bsu.by.studently.repository.PostRepository;
import bsu.by.studently.repository.UserRepository;
import bsu.by.studently.util.FileUploadUtil;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Objects;
import java.util.Optional;

@Controller
public class BlogController {
    @Autowired
    PostRepository postRepository;

    @Autowired
    UserRepository userRepository;
    @GetMapping("/blog")
    public String blogMain(Model model){
        Iterable<Post> posts = postRepository.findAll();
        model.addAttribute("posts", posts);
        return "blog-main";
    }

    @GetMapping("/post-add")
    public String appPostPage(Model model){
        model.addAttribute("post", new Post());
        return "post-add";
    }

    @PostMapping("/post-add")
    public String addPost(@ModelAttribute("post") Post post, Model model, HttpServletRequest request){
        UserDto user = (UserDto) request.getSession().getAttribute("user");
        User currentUser = userRepository.findByEmail(user.getEmail());
        currentUser.addPost(post);
        postRepository.save(post);
        request.getSession().setAttribute("user", new UserDto(userRepository.findByEmail(user.getEmail())));
        return "redirect:/";
    }

    @GetMapping("/post/{id}")
    public String postDetails(@PathVariable("id") Long id, Model model){
        if(!postRepository.existsById(id)){
            return "redirect:/";
        }
        Optional<Post> post = postRepository.findById(id);
        model.addAttribute("post", post.get());
        return "postPage";
    }
    @GetMapping("/post/{id}/edit")
    public String postEdit(@PathVariable("id") Long id, Model model, HttpServletRequest request){
        if(!postRepository.existsById(id)){
            return "redirect:/home";
        }
        UserDto user = (UserDto) request.getSession().getAttribute("user");
        Optional<Post> post = postRepository.findById(id);
        if(user.getId() != post.get().getAuthor().getId()){
            return "redirect:/";
        }
        model.addAttribute("post", post.get());
        return "editPost";
    }

    @PostMapping("/post/{id}/edit")
    public String postEditPost(@PathVariable("id") Long id, @RequestParam("title") String title,
                               @RequestParam("text") String text,Model model, HttpServletRequest request){
        if(!postRepository.existsById(id)){
            return "redirect:/";
        }
        UserDto user = (UserDto) request.getSession().getAttribute("user");
        Post post = postRepository.findById(id).orElseThrow();
        if(user.getId() != post.getAuthor().getId()){
            return "redirect:/";
        }
        post.setTitle(title);
        post.setText(text);
        post.setEdited_at(LocalDateTime.now());
        postRepository.save(post);
        Optional<User> updatedUser = userRepository.findById(user.getId());
        request.getSession().setAttribute("user", new UserDto(updatedUser.get()));
        return "redirect:/post/" + id;
    }

    @PostMapping("/post/{id}/delete")
    public String blogPostDelete(@PathVariable("id")Long id, Model model, HttpServletRequest request){
        Post post = postRepository.findById(id).orElseThrow();
        if(!postRepository.existsById(id)){
            return "redirect:/home";
        }
        UserDto user = (UserDto) request.getSession().getAttribute("user");
        if(user.getId() != post.getAuthor().getId()){
            return "redirect:/";
        }
        Optional<User> updatedUser = userRepository.findById(user.getId());
        updatedUser.get().removePost(post);
        postRepository.deleteById(id);
        request.getSession().setAttribute("user", new UserDto(updatedUser.get()));
        return "redirect:/profile";
    }

    @ModelAttribute("loginedUser")
    public UserDto getLoginedUser(HttpServletRequest request){
        return (UserDto) request.getSession().getAttribute("user");
    }
}
