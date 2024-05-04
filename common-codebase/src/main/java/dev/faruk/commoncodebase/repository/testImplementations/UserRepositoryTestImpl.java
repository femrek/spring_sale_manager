package dev.faruk.commoncodebase.repository.testImplementations;

import dev.faruk.commoncodebase.entity.AppUser;
import dev.faruk.commoncodebase.entity.AppUserRole;
import dev.faruk.commoncodebase.repository.base.UserRepository;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
public class UserRepositoryTestImpl implements UserRepository {
    private final List<AppUser> cache = new ArrayList<>();
    private long idCounter = 1;

    List<AppUserRole> roles = List.of(
            AppUserRole.builder()
                    .id(1L)
                    .name("ADMIN")
                    .build(),
            AppUserRole.builder()
                    .id(2L)
                    .name("CASHIER")
                    .build(),
            AppUserRole.builder()
                    .id(3L)
                    .name("MANAGER")
                    .build()
    );

    @Override
    public List<AppUser> findAll() {
        return cache;
    }

    @Override
    public List<AppUser> findAllOnlyExist() {
        return cache.stream().filter(user -> !user.getDeleted()).toList();
    }

    @Override
    public List<AppUser> findAllCashiers() {
        return cache.stream().filter(user -> user.getRoles().stream()
                .anyMatch(role -> role.getName().equals("CASHIER"))).toList();
    }

    @Override
    public AppUser findByUsername(String username) {
        return cache.stream().filter(user -> user.getUsername().equals(username)).findFirst().orElse(null);
    }

    @Override
    public AppUser findById(Long id) {
        return cache.stream().filter(user -> user.getId().equals(id)).findFirst().orElse(null);
    }

    @Override
    public AppUser findOnlyExistByUsername(String username) {
        return cache.stream().filter(user -> user.getUsername().equals(username) && !user.getDeleted())
                .findFirst().orElse(null);
    }

    @Override
    public AppUser findOnlyExistById(Long id) {
        return cache.stream().filter(user -> user.getId().equals(id) && !user.getDeleted())
                .findFirst().orElse(null);
    }

    @Override
    public AppUser create(AppUser user) {
        user.setId(idCounter++);
        if (user.getDeleted() == null) user.setDeleted(false);
        cache.add(user);
        return user;
    }

    @Override
    public AppUser update(AppUser user) {
        int index = cache.stream().map(AppUser::getId).toList().indexOf(user.getId());
        if (index != -1) {
            cache.set(index, user);
            return user;
        }
        throw new IllegalArgumentException("User not found");
    }

    @Override
    public void deleteByUsername(String username) {
        cache.stream().filter(user -> user.getUsername().equals(username)).findFirst().ifPresentOrElse(
                cache::remove,
                () -> {
                    throw new IllegalArgumentException("User not found");
                }
        );
    }

    @Override
    public void deleteSoftByUsername(String username) {
        cache.stream().filter(user -> user.getUsername().equals(username)).findFirst().ifPresentOrElse(user -> {
            user.setDeleted(true);
            update(user);
        }, () -> {
            throw new IllegalArgumentException("User not found");
        });
    }

    @Override
    public void deleteById(Long id) {
        cache.stream().filter(user -> user.getId().equals(id)).findFirst().ifPresentOrElse(
                cache::remove, () -> {
                    throw new IllegalArgumentException("User not found");
                }
        );
    }

    @Override
    public void deleteSoftById(AppUser user) {
        user.setDeleted(true);
        update(user);
    }

    @Override
    public List<AppUserRole> findRoles() {
        return roles;
    }

    @Override
    public AppUserRole findRoleById(Long id) {
        return roles.stream().filter(role -> role.getId().equals(id)).findFirst().orElse(null);
    }

    @Override
    public AppUserRole findRoleCashier() {
        return roles.stream().filter(role -> role.getName().equals("CASHIER")).findFirst().orElse(null);
    }
}
