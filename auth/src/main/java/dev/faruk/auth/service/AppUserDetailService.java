package dev.faruk.auth.service;

import dev.faruk.auth.dto.AppUserDetails;
import dev.faruk.commoncodebase.repository.base.UserRepository;
import dev.faruk.commoncodebase.entity.AppUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

@Component
public class AppUserDetailService implements UserDetailsService {
    private final UserRepository userRepository;

    @Autowired
    public AppUserDetailService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public AppUserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        AppUser user = userRepository.findByUsername(username);
        if (user == null) throw new UsernameNotFoundException("User not found");
        return new AppUserDetails(user);
    }
}
