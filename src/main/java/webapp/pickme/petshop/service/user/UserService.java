package webapp.pickme.petshop.service.user;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
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
import java.util.Optional;

import static webapp.pickme.petshop.data.model.user.Role.*;

@Service
public class UserService {

    private final JdbcUserDetailsManager jdbcUserDetailsManager;

    private final BCryptPasswordEncoder passwordEncoder;

    public UserService(JdbcUserDetailsManager jdbcUserDetailsManager, BCryptPasswordEncoder passwordEncoder) {
        this.jdbcUserDetailsManager = jdbcUserDetailsManager;
        this.passwordEncoder = passwordEncoder;
    }

    public UserView create(MyUser myUser) throws UserException {
        if(!checkIfUsernameAlreadyExists(myUser.getUserName())) {
            var encodedPassword = passwordEncoder.encode(myUser.getPassword());
            if( createAdminAccountCondition(myUser)){
                throw new UserException("Unauthorized to make an admin account!");
            }
            var user = new User(myUser.getUserName(), encodedPassword, getAuthorities(myUser.getRole()));
            jdbcUserDetailsManager.createUser(user);
            return new UserView(user.getUsername(), getRoleFromAuthorities(user));
        }
        throw new UserException("Username already exists!");
    }

    public UserView login(){
        var username = getAuthenticatedUserName();
        var role = getRoleFromAuthorities(jdbcUserDetailsManager.loadUserByUsername(username));
        return new UserView(username, role);
    }

    private boolean createAdminAccountCondition(MyUser myUser) throws UserException {
        return myUser.getRole() != null &&
               myUser.getRole().equals(ADMIN) &&
               !getRoleFromAuthorities(getAuthenticatedUser()).equals(ADMIN);
    }

    private List<GrantedAuthority> getAuthorities(Role role){
        List<GrantedAuthority> authorities = new ArrayList<>(List.of());
        authorities.add(new SimpleGrantedAuthority(Optional.ofNullable(role).orElse(USER).name()));
        return authorities;
    }

    private Role getRoleFromAuthorities(UserDetails user){
        var authorities = user.getAuthorities();
        return authorities.stream()
                    .map(elem -> valueOf(elem.toString())).findFirst().orElse(null);
    }

    private boolean checkIfUsernameAlreadyExists(String username){
        try {
            this.jdbcUserDetailsManager.loadUserByUsername(username);
        }catch (UsernameNotFoundException e){
            return false;
        }
        return true;
    }

    public String getAuthenticatedUserName(){
        var principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if(principal instanceof UserDetails)
            return ((UserDetails) principal).getUsername();
        return principal.toString();
    }

    public UserDetails getAuthenticatedUser() throws UserException {
        var username = getAuthenticatedUserName();
        if(username.equals("anonymousUser")){
            throw new UserException("Admin not authenticated!");
        }
        return jdbcUserDetailsManager.loadUserByUsername(username);
    }

}
