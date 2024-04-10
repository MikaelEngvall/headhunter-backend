package se.sprinta.headhunterbackend.user;

import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;
import se.sprinta.headhunterbackend.system.Result;
import se.sprinta.headhunterbackend.system.StatusCode;
import se.sprinta.headhunterbackend.user.converter.UserDtoFormToUserConverter;
import se.sprinta.headhunterbackend.user.converter.UserToUserDtoViewConverter;
import se.sprinta.headhunterbackend.user.dto.UserDtoForm;
import se.sprinta.headhunterbackend.user.dto.UserDtoView;

import java.util.List;

/**
 * This class is responsible for handling HTTP requests related to User.
 * It uses Spring's @RestController annotation to mark it as a controller that handles HTTP requests.
 * It uses @RequestMapping to map the base URL for all endpoints in this controller.
 * It uses @CrossOrigin to enable Cross-Origin Resource Sharing (CORS) from the specified origin.
 */
@RestController
@RequestMapping("${api.endpoint.base-url-users}")
@CrossOrigin("http://localhost:3000")
public class UserController {

    private final UserService userService;
    private final UserToUserDtoViewConverter userToUserDtoViewConverter;
    private final UserDtoFormToUserConverter userDtoFormToUserConverter;

    /**
     * Constructor for UserController.
     * It initializes userService, userToUserDtoViewConverter, and userDtoFormToUserConverter.
     */
    public UserController(UserService userService, UserToUserDtoViewConverter userToUserDtoViewConverter, UserDtoFormToUserConverter userDtoFormToUserConverter) {
        this.userService = userService;
        this.userToUserDtoViewConverter = userToUserDtoViewConverter;
        this.userDtoFormToUserConverter = userDtoFormToUserConverter;
    }

    /**
     * This method handles the GET request to fetch all users.
     * It uses @GetMapping to map the HTTP GET requests onto this method.
     */
    @GetMapping("/findAll")
    public Result findAllUsers() {
        List<User> foundUsers = this.userService.findAll();
        List<UserDtoView> foundUserDtos = foundUsers.stream()
                .map(this.userToUserDtoViewConverter::convert)
                .toList();
        return new Result(true, StatusCode.SUCCESS, "Find All User Success", foundUserDtos);
    }

    /**
     * This method handles the GET request to fetch a user by email.
     * It uses @GetMapping to map the HTTP GET requests onto this method.
     * It uses @PathVariable to bind the method parameter to a path variable.
     */
    @GetMapping("/findUser/{email}")
    public Result findUserByEmail(@PathVariable String email) {
        User foundUser = this.userService.findByUserEmail(email);
        UserDtoView foundUserDto = this.userToUserDtoViewConverter.convert(foundUser);
        return new Result(true, StatusCode.SUCCESS, "Find One User Success", foundUserDto);
    }

    /**
     * This method handles the POST request to register a user.
     * It uses @PostMapping to map the HTTP POST requests onto this method.
     * It uses @RequestBody to bind the method parameter to the body of the HTTP request.
     * It uses @Valid to validate the request body.
     */
    @PostMapping("/register")
    public Result registerUser(@Valid @RequestBody User user) {
        user.setRoles("user");
        User addedUser = this.userService.save(user);
        UserDtoView addedUserDto = this.userToUserDtoViewConverter.convert(addedUser);
        return new Result(true, StatusCode.SUCCESS, "Add User Success", addedUserDto);
    }

    /**
     * This method handles the POST request to add a user.
     * It uses @PostMapping to map the HTTP POST requests onto this method.
     * It uses @RequestBody to bind the method parameter to the body of the HTTP request.
     * It uses @Valid to validate the request body.
     */
    @PostMapping("/addUser")
    public Result addUser(@Valid @RequestBody User user) {
        User addedUser = this.userService.save(user);
        UserDtoView addedUserDto = this.userToUserDtoViewConverter.convert(addedUser);
        return new Result(true, StatusCode.SUCCESS, "Add Success", addedUserDto);
    }

    /**
     * This method handles the PUT request to update a user.
     * It uses @PutMapping to map the HTTP PUT requests onto this method.
     * It uses @PathVariable to bind the method parameter to a path variable.
     * It uses @RequestBody to bind the method parameter to the body of the HTTP request.
     */
    @PutMapping("/update/{email}")
    public Result updateUser(@PathVariable String email, @RequestBody UserDtoForm userDtoForm) {
        User update = this.userDtoFormToUserConverter.convert(userDtoForm);
        User user = this.userService.update(email, update);
        UserDtoView updatedUserDto = this.userToUserDtoViewConverter.convert(user);
        return new Result(true, StatusCode.SUCCESS, "Update User Success", updatedUserDto);
    }

    /**
     * This method handles the DELETE request to delete a user.
     * It uses @DeleteMapping to map the HTTP DELETE requests onto this method.
     * It uses @PathVariable to bind the method parameter to a path variable.
     */
    @DeleteMapping("/delete/{email}")
    public Result deleteUser(@PathVariable String email) {
        this.userService.delete(email);
        return new Result(true, StatusCode.SUCCESS, "Delete User Success");
    }
}