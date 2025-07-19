package demo.springboot.controller;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import demo.springboot.model.User;
import demo.springboot.service.UserService;
import demo.springboot.util.CustomErrorType;

@RestController
@RequestMapping("/api")
public class RestApiController {

	public static final Logger logger = LoggerFactory.getLogger(RestApiController.class);

	@Autowired
	UserService userService; //Service which will do all data retrieval/manipulation work

	// -------------------Retrieve All Users---------------------------------------------

	@GetMapping("/user")  
	public ResponseEntity<List<User>> listAllUsers() {
		List<User> users = userService.findAllUsers();
		if (users.isEmpty()) {
			return new ResponseEntity<>(HttpStatus.NO_CONTENT);
			// You many decide to return HttpStatus.NOT_FOUND
		}
		return new ResponseEntity<List<User>>(users, HttpStatus.OK);
	}

	
	 @GetMapping("/user/page")
	 public ResponseEntity<List<User>> listAllUsersPagination(
	            @RequestParam(defaultValue = "0") Integer pageNo,
	            @RequestParam(defaultValue = "10") Integer pageSize,
	            @RequestParam(defaultValue = "id") String sortBy,
	            @RequestParam(required = false) Double minSalary) {

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
	        int fromIndex = pageNo * pageSize;
	        int toIndex = Math.min(fromIndex + pageSize, users.size());

	        if (fromIndex > toIndex) {
	            return Collections.emptyList();
	        }

	        List<User> sortedUsers = sortUsers(users, sortBy);

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
	    
	// -------------------Retrieve Single User------------------------------------------

	    @GetMapping("/user/{id}")
	    public ResponseEntity<?> getUser(@PathVariable("id") long id) {
	        logger.info("Fetching User with id {}", id);
	        User user = userService.findById(id);
	        if (user == null) {
	            logger.error("User with id {} not found.", id);
	            throw new CustomerNotFoundException("User with id " + id + " not found");
	        }
	        return new ResponseEntity<User>(user, HttpStatus.OK);
	    }

	// -------------------Create a User-------------------------------------------
																		
	@PostMapping("/user")     
	public ResponseEntity<?> createUser(@RequestBody User user, UriComponentsBuilder ucBuilder) {
		logger.info("Creating User : {}", user);

		if (userService.isUserExist(user)) {
			logger.error("Unable to create. A User with name {} already exist", user.getName());
			return new ResponseEntity<>(new CustomErrorType("Unable to create. A User with name " + 
			user.getName() + " already exist."),HttpStatus.CONFLICT);
		}
		userService.saveUser(user);

		HttpHeaders headers = new HttpHeaders();
		headers.setLocation(ucBuilder.path("/api/user/{id}").buildAndExpand(user.getId()).toUri());
		return new ResponseEntity<String>(headers, HttpStatus.CREATED);
	}

	// ------------------- Update a User ------------------------------------------------

	@PutMapping("/user/{id}") 
	public ResponseEntity<?> updateUser(@PathVariable("id") long id, @RequestBody User user) {
		logger.info("Updating User with id {}", id);

		User currentUser = userService.findById(id);

		if (currentUser == null) {
			logger.error("Unable to update. User with id {} not found.", id);
			return new ResponseEntity<>(new CustomErrorType("Unable to upate. User with id " + id + " not found."),
					HttpStatus.NOT_FOUND);
		}

		currentUser.setName(user.getName());
		currentUser.setAge(user.getAge());
		currentUser.setSalary(user.getSalary());

		userService.updateUser(currentUser);
		return new ResponseEntity<User>(currentUser, HttpStatus.OK);
	}

	// ------------------- Delete a User-----------------------------------------

	@DeleteMapping("/user/{id}")
	public ResponseEntity<?> deleteUser(@PathVariable("id") long id) {
		logger.info("Fetching & Deleting User with id {}", id);

		User user = userService.findById(id);
		if (user == null) {
			logger.error("Unable to delete. User with id {} not found.", id);
			return new ResponseEntity<>(new CustomErrorType("Unable to delete. User with id " + id + " not found."),
					HttpStatus.NOT_FOUND);
		}
		userService.deleteUserById(id);
		return new ResponseEntity<User>(HttpStatus.NO_CONTENT);
	}

	// ------------------- Delete All Users-----------------------------

	@DeleteMapping("/user")
	public ResponseEntity<User> deleteAllUsers() {
		logger.info("Deleting All Users");

		userService.deleteAllUsers();
		return new ResponseEntity<User>(HttpStatus.NO_CONTENT);
	}

}