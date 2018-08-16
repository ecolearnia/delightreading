package com.delightreading.authsupport;

import com.delightreading.user.UserAccountEntity;
import com.delightreading.user.UserAccountRepository;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

import static java.util.Collections.emptyList;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {
    private UserAccountRepository userAccountRepository;

    public UserDetailsServiceImpl(UserAccountRepository userAccountRepository) {
        this.userAccountRepository = userAccountRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<UserAccountEntity> userAccount = userAccountRepository.findByUsername(username);
        if (!userAccount.isPresent()) {
            throw new UsernameNotFoundException(username);
        }
        return new User(userAccount.get().getUsername(), "", emptyList());
    }
}
