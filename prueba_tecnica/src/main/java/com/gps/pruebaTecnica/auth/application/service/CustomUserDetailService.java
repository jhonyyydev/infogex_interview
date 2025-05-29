package com.gps.pruebaTecnica.auth.application.service;

import com.gps.pruebaTecnica.auth.domain.CustomUserDetails;
import com.gps.pruebaTecnica.user.domain.User;
import com.gps.pruebaTecnica.user.infrastructure.repository.JpaUserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class CustomUserDetailService implements UserDetailsService {

    private final JpaUserRepository userRepository;


    public CustomUserDetailService(JpaUserRepository userRepository) {
        this.userRepository = userRepository;
    }


    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username);
        if (user == null) {
            throw new UsernameNotFoundException("User not found with username: " + username);
        }
        return new CustomUserDetails(user);
    }

}
