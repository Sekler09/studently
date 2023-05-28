package bsu.by.studently.service;

import bsu.by.studently.model.Role;
import bsu.by.studently.model.User;
import bsu.by.studently.repository.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Optional;
import java.util.Set;

@Service
public class RoleService {
    @Autowired
    private RoleRepository roleRepository;

    public void setUserRole(User user){
        setRole(user, "READER");
    }

    public void setAuthorRole(User user){
        setRole(user, "AUTHOR");
    }
    public void setAdminRole(User user){
        setRole(user, "ADMIN");
    }

    public void setRole(User user, String name){
        Role role = saveRole(name);

        if (user.getRoles().isEmpty()) {
            user.setRoles(Collections.singleton(role));
        } else {
            Set<Role> roles = user.getRoles();
            roles.add(role);
            user.setRoles(roles);
        }
    }

    public Role saveRole(String name) {
        Optional<Role> userRole = roleRepository.findByName(name);
        if (userRole.isEmpty()) {
            Role role = new Role(name);
            role = roleRepository.save(role);
            return role;
        }
        return userRole.get();
    }


}
