package bsu.by.studently.repository;

import bsu.by.studently.model.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PostRepository extends JpaRepository<Post, Long> {
    @Query("SELECT p FROM posts p WHERE p.title LIKE %?1%")
    public List<Post> search(String keyword);
}
