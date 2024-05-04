package dev.faruk.usermanagement;

import dev.faruk.commoncodebase.entity.AppUser;
import dev.faruk.commoncodebase.entity.AppUserRole;
import dev.faruk.commoncodebase.repository.base.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class UserTestDataSource {
    public static final String PASSWORD = "password";

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserTestDataSource(UserRepository userRepository,
                              PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public void cleanDatabase() {
        List<Long> userIds = userRepository.findAll().stream().map(AppUser::getId).toList();
        for (Long userId : userIds) {
            userRepository.deleteById(userId);
        }
    }

    public void loadDatabase() {
        List<AppUser> users = _generateUsers();
        for (AppUser user : users) {
            userRepository.create(user);
        }
    }

    private List<AppUser> _generateUsers() {
        List<AppUser> users = new ArrayList<>();
        List<List<AppUserRole>> usersRoles = _generateRoleLists();
        for (int i = 0; i < 7; i++) {
            users.add(AppUser.builder()
                    .username("user" + (i + 1))
                    .password(passwordEncoder.encode(PASSWORD))
                    .name("User " + (i + 1))
                    .deleted(false)
                    .roles(usersRoles.get(i))
                    .build());
        }

        return users;
    }

    private List<List<AppUserRole>> _generateRoleLists() {
        List<List<AppUserRole>> usersRoles = new ArrayList<>();
        List<AppUserRole> roles = userRepository.findRoles();

        for (int i = 0; i < 7; i++) {
            List<AppUserRole> userRoles = new ArrayList<>();

            int roleCode = (i + 1) % 8;
            boolean has0 = (roleCode & 1) == 1;
            boolean has1 = (roleCode & 2) == 2;
            boolean has2 = (roleCode & 4) == 4;

            if (has0) userRoles.add(roles.get(0));
            if (has1) userRoles.add(roles.get(1));
            if (has2) userRoles.add(roles.get(2));

            usersRoles.add(userRoles);
        }

        return usersRoles;
    }
}
