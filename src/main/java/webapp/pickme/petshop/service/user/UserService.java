package webapp.pickme.petshop.service.user;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.provisioning.JdbcUserDetailsManager;
import org.springframework.stereotype.Service;
import webapp.pickme.petshop.api.view.UserView;
import webapp.pickme.petshop.data.model.user.MyUser;
import webapp.pickme.petshop.data.model.user.Role;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
public class UserService {

    private final JdbcUserDetailsManager jdbcUserDetailsManager;

    private final BCryptPasswordEncoder passwordEncoder;

    public UserService(JdbcUserDetailsManager jdbcUserDetailsManager, BCryptPasswordEncoder passwordEncoder) {
        this.jdbcUserDetailsManager = jdbcUserDetailsManager;
        this.passwordEncoder = passwordEncoder;
    }

    public UserView create(MyUser myUser) {
        if(!checkIfUsernameAlreadyExists(myUser.getUserName())) {
            var encodedPassword = passwordEncoder.encode(myUser.getPassword());
            var user = new User(myUser.getUserName(), encodedPassword, getAuthorities(myUser.getRole()));
            jdbcUserDetailsManager.createUser(user);
            return new UserView(user.getUsername(), getRole(user));
        }
        throw new IllegalArgumentException("Username already exists!");
    }

    private List<GrantedAuthority> getAuthorities(Role role){
        List<GrantedAuthority> authorities = new ArrayList<>(List.of());
        authorities.add(new SimpleGrantedAuthority(Objects.requireNonNullElse(role, Role.USER).name()));
        return authorities;
    }

    private Role getRole(UserDetails user){
        var authorities = user.getAuthorities();
        return authorities.stream()
                    .map(elem -> Role.valueOf(elem.toString())).findFirst().orElse(null);
    }

    public UserView login(MyUser myUser) throws IllegalAccessException {
        if(checkCredentials(myUser)){
            return new UserView(myUser.getUserName(), myUser.getRole());
        }
        throw new IllegalAccessException("Incorrect credentials!");
    }

    private boolean checkCredentials(MyUser myUser) {
        var user = jdbcUserDetailsManager.loadUserByUsername(myUser.getUserName());
        var encodedPassword = passwordEncoder.encode(myUser.getPassword());
        return user.getPassword().equals(encodedPassword);
    }

    private boolean checkIfUsernameAlreadyExists(String username){
        try {
            this.jdbcUserDetailsManager.loadUserByUsername(username);
        }catch (UsernameNotFoundException e){
            return false;
        }
        return true;
    }
}
