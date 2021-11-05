package webapp.pickme.petshop.api.controller;

import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import webapp.pickme.petshop.data.model.user.MyUser;
import webapp.pickme.petshop.service.user.UserService;

@RestController
@RequestMapping("/user")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/create")
    public ResponseEntity<?> create(@RequestBody MyUser myUser){
        this.userService.create(myUser);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @GetMapping("/login")
    public ResponseEntity<?> login(@RequestBody MyUser myUser){
        if (this.userService.login(myUser))
            return ResponseEntity.ok().build();
        return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
    }
}
