package bsu.by.studently.dto;

import bsu.by.studently.model.Post;
import bsu.by.studently.model.Role;
import bsu.by.studently.model.User;

import java.util.List;
import java.util.Set;

public class UserDto {

    private Long id;

    private String name;

    private String email;
    private String photo;

    private List<Post> posts;

    public List<Post> getPosts() {
        return posts;
    }

    public void setPosts(List<Post> posts) {
        this.posts = posts;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public Set<Role> getRoles() {
        return roles;
    }

    public void setRoles(Set<Role> roles) {
        this.roles = roles;
    }

    private Set<Role> roles;

    public UserDto() {}

    public UserDto(User user) {
        this.id = user.getId();
        this.name = user.getName();
        this.email = user.getEmail();
        this.roles = user.getRoles();
        this.photo = user.getPhoto();
        this.posts = user.getPosts();
    }

    public boolean hasRole(String roleName){
        return roles.stream().anyMatch(role -> role.getName().equals(roleName));
    }


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Override
    public String toString() {
        return "UserDto{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", email='" + email + '\''+
                '}';
    }
}
