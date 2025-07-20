package demo.springboot.controller;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import demo.springboot.controller.CustomerNotFoundException;
import demo.springboot.model.User;
import demo.springboot.service.UserService;
import demo.springboot.util.CustomErrorType;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

@Tag(name = "User Management", description = "User CRUD operations and filtering")
@RestController
@RequestMapping("/api")
public class RestApiController {

    private static final Logger logger = LoggerFactory.getLogger(RestApiController.class);

    @Autowired
    private UserService userService;

    @Operation(summary = "Get all users")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Users found"),
        @ApiResponse(responseCode = "204", description = "No users found")
    })
    @GetMapping("/user")
    public ResponseEntity<List<User>> listAllUsers() {
        List<User> users = userService.findAllUsers();
        if (users.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(users, HttpStatus.OK);
    }

    @Operation(summary = "Get users with pagination, sorting, and salary filter")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Users found"),
        @ApiResponse(responseCode = "204", description = "No users match the filter")
    })
    @GetMapping("/user/page")
    public ResponseEntity<List<User>> listAllUsersPagination(
        @Parameter(description = "Page number (default is 0)") @RequestParam(defaultValue = "0") Integer pageNo,
        @Parameter(description = "Page size (default is 10)") @RequestParam(defaultValue = "10") Integer pageSize,
        @Parameter(description = "Sort by: name, age, or salary") @RequestParam(defaultValue = "id") String sortBy,
        @Parameter(description = "Minimum salary filter") @RequestParam(required = false) Double minSalary
    ) {
        List<User> users = userService.findAllUsers();
        if (minSalary != null) {
            users = userService.findUsersBySalaryAbove(minSalary);
        }
        List<User> paginatedUsers = paginate(users, pageNo, pageSize, sortBy);
        if (paginatedUsers.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(paginatedUsers, HttpStatus.OK);
    }

    private List<User> paginate(List<User> users, Integer pageNo, Integer pageSize, String sortBy) {
        List<User> sortedUsers = sortUsers(users, sortBy);
        int fromIndex = pageNo * pageSize;
        int toIndex = Math.min(fromIndex + pageSize, sortedUsers.size());
        if (fromIndex >= toIndex) {
            return Collections.emptyList();
        }
        return sortedUsers.subList(fromIndex, toIndex);
    }

    private List<User> sortUsers(List<User> users, String sortBy) {
        switch (sortBy) {
            case "name":
                users.sort(Comparator.comparing(User::getName));
                break;
            case "age":
                users.sort(Comparator.comparingInt(User::getAge));
                break;
            case "salary":
                users.sort(Comparator.comparingDouble(User::getSalary));
                break;
            default:
                break;
        }
        return users;
    }

    @Operation(summary = "Get a single user by ID")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "User found"),
        @ApiResponse(responseCode = "404", description = "User not found")
    })
    @GetMapping("/user/{id}")
    public ResponseEntity<?> getUser(@PathVariable("id") long id) {
        logger.info("Fetching User with id {}", id);
        User user = userService.findById(id);
        if (user == null) {
            logger.error("User with id {} not found.", id);
            throw new CustomerNotFoundException("User with id " + id + " not found");
        }
        return new ResponseEntity<>(user, HttpStatus.OK);
    }

    @Operation(summary = "Create a new user")
    @ApiResponses({
        @ApiResponse(responseCode = "201", description = "User created"),
        @ApiResponse(responseCode = "409", description = "User already exists")
    })
    @PostMapping("/user")
    public ResponseEntity<?> createUser(@RequestBody User user, UriComponentsBuilder ucBuilder) {
        logger.info("Creating User: {}", user);
        if (userService.isUserExist(user)) {
            logger.error("User with name {} already exists", user.getName());
            return new ResponseEntity<>(
                new CustomErrorType("Unable to create. A User with name " + user.getName() + " already exists."),
                HttpStatus.CONFLICT);
        }
        userService.saveUser(user);
        HttpHeaders headers = new HttpHeaders();
        headers.setLocation(ucBuilder.path("/api/user/{id}").buildAndExpand(user.getId()).toUri());
        return new ResponseEntity<>(headers, HttpStatus.CREATED);
    }

    @Operation(summary = "Update an existing user")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "User updated"),
        @ApiResponse(responseCode = "404", description = "User not found")
    })
    @PutMapping("/user/{id}")
    public ResponseEntity<?> updateUser(@PathVariable("id") long id, @RequestBody User user) {
        logger.info("Updating User with id {}", id);
        User currentUser = userService.findById(id);
        if (currentUser == null) {
            return new ResponseEntity<>(
                new CustomErrorType("Unable to update. User with id " + id + " not found."),
                HttpStatus.NOT_FOUND);
        }
        currentUser.setName(user.getName());
        currentUser.setAge(user.getAge());
        currentUser.setSalary(user.getSalary());
        userService.updateUser(currentUser);
        return new ResponseEntity<>(currentUser, HttpStatus.OK);
    }

    @Operation(summary = "Delete a user by ID")
    @ApiResponses({
        @ApiResponse(responseCode = "204", description = "User deleted"),
        @ApiResponse(responseCode = "404", description = "User not found")
    })
    @DeleteMapping("/user/{id}")
    public ResponseEntity<?> deleteUser(@PathVariable("id") long id) {
        logger.info("Deleting User with id {}", id);
        User user = userService.findById(id);
        if (user == null) {
            return new ResponseEntity<>(
                new CustomErrorType("Unable to delete. User with id " + id + " not found."),
                HttpStatus.NOT_FOUND);
        }
        userService.deleteUserById(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @Operation(summary = "Delete all users")
    @ApiResponses({
        @ApiResponse(responseCode = "204", description = "All users deleted")
    })
    @DeleteMapping("/user")
    public ResponseEntity<User> deleteAllUsers() {
        logger.info("Deleting All Users");
        userService.deleteAllUsers();
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
