package ftn.rbs.madagascar_hub.services;

import ftn.rbs.madagascar_hub.models.User;
import ftn.rbs.madagascar_hub.repositories.UserRepository;
import ftn.rbs.madagascar_hub.services.interfaces.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;

@Service
public class UserService implements IUserService, UserDetailsService {

    @Autowired
    private UserRepository allUsers;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Optional<User> ret = allUsers.findByEmail(email);
        if (!ret.isEmpty()) {
//            System.out.println(ret.get().getEmail());
            return org.springframework.security.core.userdetails.User.withUsername(email).password(ret.get().getPassword()).roles(ret.get().getRole().toString()).build();
        }
        throw new UsernameNotFoundException("User not found with this email: " + email);
    }

    @Override
    public User getUserByEmail(String email) {
        User user = allUsers.findByEmail(email).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User does not exist!"));
        return user;
    }

    @Override
    public User getCurrentUser() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        return allUsers.findByEmail(auth.getName()).orElse(null);
    }
}
