package webapp.pickme.petshop.api.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import webapp.pickme.petshop.api.view.UserView;
import webapp.pickme.petshop.data.model.user.MyUser;
import webapp.pickme.petshop.service.user.UserException;
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
        try {
            return new ResponseEntity<>(this.userService.create(myUser), HttpStatus.CREATED);
        } catch (UserException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.CONFLICT);
        }
    }

    @GetMapping("/login")
    public ResponseEntity<UserView> login(){
        return ResponseEntity.ok(userService.login());
    }
}
