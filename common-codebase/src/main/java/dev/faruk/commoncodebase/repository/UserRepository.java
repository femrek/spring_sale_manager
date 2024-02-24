package dev.faruk.commoncodebase.repository;

import dev.faruk.commoncodebase.entity.AppUser;
import dev.faruk.commoncodebase.entity.AppUserRole;
import jakarta.persistence.TypedQuery;

import java.util.List;

public interface UserRepository {
    /**
     * Fetches all the users in the database
     * @return List of users
     */
    List<AppUser> findAll();

    /**
     * Fetches all the users has cashier role in the database
     * @return List of cashiers
     */
    List<AppUser> findAllCashiers();

    /**
     * Fetches a user by username. fetches even if the user is deleted
     * @param username username of the user
     * @return User with the given username
     */
    AppUser findByUsername(String username);

    /**
     * Fetches a user by id
     * @param id id of the user
     * @return User with the given id
     */
    AppUser findById(int id);

    /**
     * Fetches a user by username. fetches only if the user is not deleted
     * @param username username of the user
     * @return User with the given username
     */
    AppUser findOnlyExistByUsername(String username);

    /**
     * Fetches a user by id. fetches only if the user is not deleted
     * @param id id of the user
     * @return User with the given id
     */
    AppUser findOnlyExistById(int id);

    /**
     * Saves a user to the database
     * @param user user to be saved
     * @return saved user
     */
    AppUser save(AppUser user);

    /**
     * Deletes a user by username permanently
     * @param username username of the user to be deleted
     */
    void deleteByUsername(String username);

    /**
     * Marks a user as deleted by username
     * @param username username of the user to be deleted
     */
    void deleteSoftByUsername(String username);

    /**
     * Fetches all the roles in the database
     * @return List of roles
     */
    List<AppUserRole> findRoles();

    /**
     * finds the role named 'CASHIER'
     * @return the {@link AppUserRole} object of cashier role
     */
    AppUserRole findRoleCashier();
}
