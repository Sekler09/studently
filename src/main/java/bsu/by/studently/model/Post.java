package bsu.by.studently.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity(name = "posts")
@Table(
        name = "posts"
)
public class Post {
    @Id
    @SequenceGenerator(
            name = "post_sequence",
            sequenceName = "post_sequence",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "post_sequence"
    )
    @Column(
            name = "post_id",
            updatable = false
    )
    private Long id;

    @Column(
            name = "post_title",
            nullable = false
    )
    private String title;

    @Column(
            name = "post_text",
            nullable = false
    )
    private String text;

    @Column(
            name = "created_at",
            nullable = false
    )
    private LocalDateTime created_at;

    @Column(
            name = "edited_at",
            nullable = false
    )
    private LocalDateTime edited_at;

    public User getAuthor() {
        return author;
    }

    public void setAuthor(User author) {
        this.author = author;
    }

    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name="user_id", nullable = false)
    @JsonIgnore
    private User author;

    public Post(){}

    public Post(String title, String text) {
        this.title = title;
        this.text = text;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public LocalDateTime getCreated_at() {
        return created_at;
    }

    public void setCreated_at(LocalDateTime created_at) {
        this.created_at = created_at;
    }

    @PrePersist
    protected void onCreate() {
        edited_at = LocalDateTime.now();
        created_at = LocalDateTime.now();
    }

    public LocalDateTime getEdited_at() {
        return edited_at;
    }

    public void setEdited_at(LocalDateTime edited_at) {
        this.edited_at = edited_at;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }
}
