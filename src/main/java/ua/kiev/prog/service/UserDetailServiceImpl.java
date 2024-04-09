package ua.kiev.prog.service;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import ua.kiev.prog.model.CustomAdmin;

import java.util.Arrays;
import java.util.List;

@Service
public class UserDetailServiceImpl implements UserDetailsService {
    private final UserService userService;

    public UserDetailServiceImpl(UserService userService) {
        this.userService = userService;
    }

    @Override
    public UserDetails loadUserByUsername(String login) throws UsernameNotFoundException {
        CustomAdmin admin = userService.findByLogin(login);
        if (admin == null)
            throw new UsernameNotFoundException("Login: " + login);
        List<GrantedAuthority> roles = Arrays.asList(
                new SimpleGrantedAuthority(admin.getRole().toString()));
        return new User(admin.getLogin(), admin.getPassword(), roles);
    }
}
