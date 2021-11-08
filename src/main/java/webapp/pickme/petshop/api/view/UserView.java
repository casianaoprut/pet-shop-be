package webapp.pickme.petshop.api.view;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import webapp.pickme.petshop.data.model.user.Role;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserView {

    private String username;

    private Role role;

}
