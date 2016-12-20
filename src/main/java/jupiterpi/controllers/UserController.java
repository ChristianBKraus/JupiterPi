package jupiterpi.controllers;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;
import javax.inject.Inject;

import jupiterpi.models.*;

@RequestMapping(path = UserController.PATH)
@RestController
public class UserController {
	public static final String PATH ="hello/api";
	
	@Inject
	private UserRepository repo;

	@GetMapping(path = "")
	public Iterable<User> users() {
		return repo.findAll();
	}
	
    @GetMapping(path = "/{name}") 
    public User user(@PathVariable("name") String name) {
        return repo.findOne(name);
    }
    
    @PostMapping
    public ResponseEntity<User> create(@RequestBody User user,
                                       UriComponentsBuilder uriComponentsBuilder) {
      System.out.println("create user: " + user);

      User savedUser = repo.save(user);
      UriComponents uriComponents = uriComponentsBuilder.path(PATH + "/{user}")
                    .buildAndExpand(savedUser.getUser());
      HttpHeaders headers = new HttpHeaders();
      headers.setLocation(uriComponents.toUri());
      return new ResponseEntity<>(savedUser, headers, HttpStatus.CREATED);
    }
 }
