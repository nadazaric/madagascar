package ftn.rbs.madagascar_hub.services;

import ftn.rbs.madagascar_hub.dtos.RegisterUserDTO;
import ftn.rbs.madagascar_hub.enums.UserRole;
import ftn.rbs.madagascar_hub.models.User;
import ftn.rbs.madagascar_hub.repositories.UserRepository;
import ftn.rbs.madagascar_hub.services.interfaces.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;

@Service
public class UserService implements IUserService, UserDetailsService {

    @Autowired
    private UserRepository allUsers;

    @Autowired
    private PasswordEncoder passwordEncoder;

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
    public void registerUser(RegisterUserDTO registerUser) {
        if (alreadyExist(registerUser.getUsername(), registerUser.getEmail())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, String.format(
                    "User with username %s or email %s already exist!",
                    registerUser.getUsername(), registerUser.getEmail()));
        }
        User user = new User(
                registerUser.getName(),
                registerUser.getSurname(),
                registerUser.getUsername(),
                registerUser.getEmail(),
                passwordEncoder.encode(registerUser.getPassword()),
                UserRole.USER
        );
        allUsers.save(user);
    }

    private boolean alreadyExist(String username, String email) {
        Optional<User> usernameCheck = allUsers.findByUsername(username);
        Optional<User> emailCheck = allUsers.findByEmail(email);
        return usernameCheck.isPresent() || emailCheck.isPresent();
    }
}
