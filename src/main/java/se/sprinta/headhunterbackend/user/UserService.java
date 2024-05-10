package se.sprinta.headhunterbackend.user;

import jakarta.transaction.Transactional;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import se.sprinta.headhunterbackend.system.exception.EmailAlreadyExistsException;
import se.sprinta.headhunterbackend.system.exception.ObjectNotFoundException;

import java.util.List;
import java.util.Optional;

/**
 * This class is responsible for handling business logic related to User.
 * It uses Spring's @Service annotation to mark it as a service that holds business logic.
 * It uses @Transactional annotation to ensure that all methods within this class are wrapped with a transaction.
 * It implements UserDetailsService interface which is a core interface in Spring Security framework, used to retrieve the user’s authentication and authorization information.
 */
@Service
@Transactional
public class UserService implements UserDetailsService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    /**
     * Constructor for UserService.
     * It initializes userRepository and passwordEncoder.
     */
    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    /**
     * This method is used to fetch all users.
     */
    public List<User> findAll() {
        return this.userRepository.findAll();
    }

    /**
     * This method is used to fetch a user by email.
     */
    public User findByUserEmail(String email) {
        return this.userRepository.findByEmail(email)
                .orElseThrow(() -> new ObjectNotFoundException("user", email));
    }

    /**
     * This method is used to save a new user.
     * It encodes the password before saving the user.
     */
    public User save(User newUser) {
        newUser.setPassword(this.passwordEncoder.encode(newUser.getPassword()));
        return this.userRepository.save(newUser);
    }

    /**
     * This method is used to update a user.
     * It finds the user by email and updates the username (and roles NOT YET).
     */
    public User update(String email, User update) {
        User foundUser = this.userRepository.findByEmail(email)
                .orElseThrow(() -> new ObjectNotFoundException("user", email));

        // Update the email if provided and not empty
        if (update.getEmail() != null && !update.getEmail().isEmpty()) {
            // Check if the new email is not already taken
            if (!update.getEmail().equals(email)) {
                Optional<User> existingUserWithEmail = this.userRepository.findByEmail(update.getEmail());
                if (existingUserWithEmail.isPresent()) {
                    throw new EmailAlreadyExistsException("Email is already in use: " + update.getEmail());
                }
                foundUser.setEmail(update.getEmail());
            }
        }

        // Update the username if provided and not empty
        if (update.getUsername() != null && !update.getUsername().isEmpty()) {
            foundUser.setUsername(update.getUsername());
        }

        // Update the roles if provided and not empty
        if (update.getRoles() != null && !update.getRoles().isEmpty()) {
            foundUser.setRoles(update.getRoles());
        }

        // Save the updated user
        return this.userRepository.save(foundUser);
    }


    /**
     * This method is used to delete a user.
     * It finds the user by email and deletes it.
     */
    public void delete(String email) {
        User foundUser = this.userRepository.findByEmail(email)
                .orElseThrow(() -> new ObjectNotFoundException("user", email));
        this.userRepository.delete(foundUser);
    }

    /**
     * This method is used to fetch UserDetails for Spring Security to check authentication.
     * It finds the user by email and wraps the returned user instance in a MyUserPrincipal instance.
     * If the User object doesn't exist, UsernameNotFoundException is thrown.
     */
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        UserDetails userDetails = this.userRepository.findByEmail(email)
                .map(MyUserPrincipal::new)
                .orElseThrow(() -> new UsernameNotFoundException("email " + email + " is not found"));
        return userDetails;
    }
}