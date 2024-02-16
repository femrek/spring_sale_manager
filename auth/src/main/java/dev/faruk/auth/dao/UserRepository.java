package dev.faruk.auth.dao;

import dev.faruk.auth.entity.AppUser;

import java.util.List;

public interface UserRepository {
    /**
     * Fetches all the users in the database
     * @return List of users
     */
    List<AppUser> findAll();

    /**
     * Fetches a user by username. fetches even if the user is deleted
     * @param username username of the user
     * @return User with the given username
     */
    AppUser findByUsername(String username);

    /**
     * Fetches a user by username. fetches only if the user is not deleted
     * @param username username of the user
     * @return User with the given username
     */
    AppUser findOnlyExistByUsername(String username);

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
}
