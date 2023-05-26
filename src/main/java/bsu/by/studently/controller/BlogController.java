package bsu.by.studently.controller;

import bsu.by.studently.model.Post;
import bsu.by.studently.model.User;
import bsu.by.studently.repository.PostRepository;
import bsu.by.studently.util.FileUploadUtil;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Files;
import java.nio.file.Paths;

@Controller
public class BlogController {
    @Autowired
    private PostRepository postRepository;
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
    public String addPost(@ModelAttribute("post") Post post, Model model){
        postRepository.save(post);
        return "redirect:/";
    }
}
