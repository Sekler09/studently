package bsu.by.studently.dto;

import bsu.by.studently.model.Role;
import java.util.Set;

public class UserDto {

    private Long id;

    private String name;

    private String email;
    private String photo;

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

    public UserDto(Long id, String name, String email, Set<Role> roles, String photo) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.roles = roles;
        this.photo = photo;
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
