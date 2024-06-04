package dev.faruk.auth.service;

import dev.faruk.auth.dto.AppUserDetails;
import dev.faruk.commoncodebase.repository.base.UserRepository;
import dev.faruk.commoncodebase.entity.AppUser;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

@Log4j2
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
        if (user == null) {
            throw new UsernameNotFoundException("User %s not found".formatted(username));
        }
        return new AppUserDetails(user);
    }
}
