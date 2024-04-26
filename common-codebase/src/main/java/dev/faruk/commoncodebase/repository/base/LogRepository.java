package dev.faruk.commoncodebase.repository.base;

import dev.faruk.commoncodebase.entity.AppLog;

import java.util.List;

public interface LogRepository {
    /**
     * Saves the ${@link AppLog} object to the database.
     * @param appLog AppLog object to save
     */
    void save(AppLog appLog);

    /**
     * Returns all the logs in the database.
     * @param page Page number. starts from 1
     * @param size Number of logs in a page
     * @return List of AppLog objects
     */
    List<AppLog> findAll(Integer page, Integer size);

    /**
     * Returns the log with the given id.
     * @param id Id of the log
     * @return AppLog object
     */
    AppLog findById(Long id);
}
