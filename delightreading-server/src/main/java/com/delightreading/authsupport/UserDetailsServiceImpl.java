package com.delightreading.authsupport;

import com.delightreading.user.UserAccountEntity;
import com.delightreading.user.UserService;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

import static java.util.Collections.emptyList;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {
    private UserService userService;

    public UserDetailsServiceImpl(UserService userService) {
        this.userService = userService;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<UserAccountEntity> userAccount = userService.findAccountByUsername(username);
        if (!userAccount.isPresent()) {
            throw new UsernameNotFoundException(username);
        }
        return new User(userAccount.get().getUsername(), "", emptyList());
    }
}
