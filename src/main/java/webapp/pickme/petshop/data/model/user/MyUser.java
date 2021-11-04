package webapp.pickme.petshop.data.model.user;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MyUser {

    private String userName;

    private String password;

    private Role role;

}
