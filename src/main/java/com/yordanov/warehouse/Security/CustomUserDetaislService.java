package com.yordanov.warehouse.Security;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import com.yordanov.warehouse.User.Model.User;
import com.yordanov.warehouse.User.Repository.UserRepository;

public class CustomUserDetaislService implements UserDetailsService {

    private final UserRepository userRepository;

    public CustomUserDetaislService(UserRepository userRepository){
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found: " + username));

        return new CustomUserDetails(user);
    }
    
}
