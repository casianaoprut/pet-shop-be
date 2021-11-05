package webapp.pickme.petshop.service.user;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.provisioning.JdbcUserDetailsManager;
import org.springframework.stereotype.Service;
import webapp.pickme.petshop.data.model.user.MyUser;

import java.util.ArrayList;
import java.util.List;

@Service
public class UserService {

    private final JdbcUserDetailsManager jdbcUserDetailsManager;

    private final BCryptPasswordEncoder passwordEncoder;

    public UserService(JdbcUserDetailsManager jdbcUserDetailsManager, BCryptPasswordEncoder passwordEncoder) {
        this.jdbcUserDetailsManager = jdbcUserDetailsManager;
        this.passwordEncoder = passwordEncoder;
    }

    public void create(MyUser myUser) {
        List<GrantedAuthority> authorities = new ArrayList<>(List.of());
        authorities.add(new SimpleGrantedAuthority(myUser.getRole().name()));
        var encodedPassword = passwordEncoder.encode(myUser.getPassword());
        var user = new User(myUser.getUserName(), encodedPassword, authorities);
        jdbcUserDetailsManager.createUser(user);
    }

    public boolean login(MyUser myUser) {
        var user = jdbcUserDetailsManager.loadUserByUsername(myUser.getUserName());
        var encodedPassword = passwordEncoder.encode(myUser.getPassword());
        return user.getPassword().equals(encodedPassword);
    }
}
