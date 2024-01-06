package StoreApp.StoreApp.service.impl;

import StoreApp.StoreApp.entity.User;
import StoreApp.StoreApp.model.MyUserDetails;
import StoreApp.StoreApp.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class MyUserDetailService implements UserDetailsService {

    @Autowired
    private UserService userService;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userService.GetUserById(username);
        // Check If User Is Not Null:
        if(user == null){
            throw new UsernameNotFoundException("Unable To Load User");
        }
        return new MyUserDetails(user);
    }
    // END OF LOAD USER BY USERNAME METHOD.
}
