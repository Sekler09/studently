package bsu.by.studently.service;

import bsu.by.studently.model.Post;
import bsu.by.studently.repository.PostRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PostService {
    @Autowired
    PostRepository postRepository;

    public List<Post> listAll(String keyword) {
        if(keyword != null){
            return postRepository.search(keyword);
        }
        return postRepository.findAll();
    }

}
