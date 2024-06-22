package dev.faruk.auth;

import dev.faruk.commoncodebase.entity.AppUser;
import dev.faruk.commoncodebase.entity.AppUserRole;
import dev.faruk.commoncodebase.repository.base.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class AuthTestDataSource {
    public static final String ADMIN_USERNAME = "userAdmin";
    public static final String CASHIER_USERNAME = "userCashier";
    public static final String MANAGER_USERNAME = "userManager";
    public static final String DELETED_USERNAME = "userDeleted";
    public static final String PASSWORD = "password";

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public AuthTestDataSource(UserRepository userRepository,
                              PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public AppUserRole getAdminRole() {
        return userRepository.findRoles().stream().filter(role -> role.getName().equals("ADMIN")).findFirst().orElseThrow();
    }

    public AppUserRole getCashierRole() {
        return userRepository.findRoles().stream().filter(role -> role.getName().equals("CASHIER")).findFirst().orElseThrow();
    }

    public AppUserRole getManagerRole() {
        return userRepository.findRoles().stream().filter(role -> role.getName().equals("MANAGER")).findFirst().orElseThrow();
    }

    public void cleanDatabase() {
        List<Long> userIds = userRepository.findAll().stream().map(AppUser::getId).toList();
        for (Long id : userIds) {
            userRepository.deleteById(id);
        }
    }

    public AppUser getAdminUser() {
        return userRepository.findByUsername(ADMIN_USERNAME);
    }

    public AppUser getCashierUser() {
        return userRepository.findByUsername(CASHIER_USERNAME);
    }

    public AppUser getManagerUser() {
        return userRepository.findByUsername(MANAGER_USERNAME);
    }

    public AppUser getDeletedUser() {
        return userRepository.findByUsername(DELETED_USERNAME);
    }

    public void loadTestDataSetToDatabase() {
        final List<AppUser> testUsersSample = List.of(
                _getAdminUser(),
                _getCashierUser(),
                _getManagerUser(),
                _getDeletedUser()
        );

        for (AppUser user : testUsersSample) {
            userRepository.create(user);
        }
    }

    private AppUser _getAdminUser() {
        List<AppUserRole> userRoles = new ArrayList<>();
        userRoles.add(getAdminRole());
        return AppUser.builder()
                .username(ADMIN_USERNAME)
                .password(passwordEncoder.encode("password"))
                .name("Test User Admin")
                .deleted(false)
                .roles(userRoles)
                .build();
    }

    private AppUser _getCashierUser() {
        List<AppUserRole> userRoles = new ArrayList<>();
        userRoles.add(getCashierRole());
        return AppUser.builder()
                .username(CASHIER_USERNAME)
                .password(passwordEncoder.encode("password"))
                .name("Test User Cashier")
                .deleted(false)
                .roles(userRoles)
                .build();
    }

    private AppUser _getManagerUser() {
        List<AppUserRole> userRoles = new ArrayList<>();
        userRoles.add(getManagerRole());
        return AppUser.builder()
                .username(MANAGER_USERNAME)
                .password(passwordEncoder.encode("password"))
                .name("Test User Manager")
                .deleted(false)
                .roles(userRoles)
                .build();
    }

    private AppUser _getDeletedUser() {
        List<AppUserRole> userRoles = new ArrayList<>();
        userRoles.add(getAdminRole());
        userRoles.add(getCashierRole());
        userRoles.add(getManagerRole());
        return AppUser.builder()
                .username(DELETED_USERNAME)
                .password(passwordEncoder.encode("password"))
                .name("Test User Deleted")
                .deleted(true)
                .roles(userRoles)
                .build();
    }
}
