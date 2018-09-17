package com.delightreading.authsupport;

import com.delightreading.user.model.UserAuthenticationEntity;
import com.delightreading.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * This UserDetailsService is meant for Authenticated Token.
 * It does not care about the password.
 * NOTE: The token contains uid, instead of username.
 */
@Service
public class TokenBasedUserDetailsService implements UserDetailsService {
    private UserService userService;

    @Autowired
    PasswordEncoder passwordEncoder;

    public TokenBasedUserDetailsService(UserService userService) {
        this.userService = userService;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<UserAuthenticationEntity> userAuth = userService.findAuthenticationByUid(username);
        if (!userAuth.isPresent()) {
            throw new UsernameNotFoundException(username);
        }
        // return new User(userAuth.get(), passwordEncoder.encode("pwd"), emptyList());
        // Override password with default
        userAuth.get().setPassword(passwordEncoder.encode("pwd"));
        return userAuth.get();
    }
}
