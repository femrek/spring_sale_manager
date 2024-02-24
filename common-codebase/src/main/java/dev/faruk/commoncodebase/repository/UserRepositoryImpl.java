package dev.faruk.commoncodebase.repository;

import dev.faruk.commoncodebase.entity.AppUser;
import dev.faruk.commoncodebase.entity.AppUserRole;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class UserRepositoryImpl implements UserRepository {
    private final EntityManager entityManager;

    @Autowired
    public UserRepositoryImpl(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Override
    public List<AppUser> findAll() {
        TypedQuery<AppUser> query = entityManager.createQuery(
                "SELECT u FROM AppUser as u", AppUser.class);

        return query.getResultList();
    }

    @Override
    public List<AppUser> findAllCashiers() {
        TypedQuery<AppUser> query = entityManager.createQuery(
                "SELECT u FROM AppUser as u JOIN u.roles as r WHERE r = :role", AppUser.class);

        return query.getResultList();
    }

    @Override
    public AppUser findByUsername(String username) {
        TypedQuery<AppUser> query = entityManager.createQuery(
                "SELECT u FROM AppUser as u WHERE u.username = :username", AppUser.class);
        query.setParameter("username", username);

        final List<AppUser> results = query.getResultList();
        if (results.isEmpty()) return null;
        return results.get(0);
    }

    @Override
    public AppUser findById(int id) {
        TypedQuery<AppUser> query = entityManager.createQuery(
                "SELECT u FROM AppUser as u WHERE u.id = :id", AppUser.class);
        query.setParameter("id", id);

        final List<AppUser> results = query.getResultList();
        if (results.isEmpty()) return null;
        return results.get(0);
    }

    @Override
    public AppUser findOnlyExistByUsername(String username) {
        TypedQuery<AppUser> query = entityManager.createQuery(
                "SELECT u FROM AppUser as u WHERE u.username = :username AND u.deleted = false", AppUser.class);
        query.setParameter("username", username);

        final List<AppUser> results = query.getResultList();
        if (results.isEmpty()) return null;
        return results.get(0);
    }

    @Override
    public AppUser findOnlyExistById(int id) {
        TypedQuery<AppUser> query = entityManager.createQuery(
                "SELECT u FROM AppUser as u WHERE u.id = :id AND u.deleted = false", AppUser.class);
        query.setParameter("id", id);

        final List<AppUser> results = query.getResultList();
        if (results.isEmpty()) return null;
        return results.get(0);
    }

    @Override
    @Transactional
    public AppUser save(AppUser user) {
        entityManager.persist(user);
        return user;
    }

    @Override
    @Transactional
    public void deleteByUsername(String username) {
        AppUser user = findByUsername(username);
        entityManager.remove(user);
    }

    @Override
    @Transactional
    public void deleteSoftByUsername(String username) {
        AppUser user = findOnlyExistByUsername(username);
        user.setDeleted(true);
        entityManager.merge(user);
    }

    @Override
    public List<AppUserRole> findRoles() {
        TypedQuery<AppUserRole> query = entityManager.createQuery(
                "SELECT r FROM AppUserRole as r", AppUserRole.class);

        return query.getResultList();
    }

    @Override
    public AppUserRole findRoleCashier() {
        TypedQuery<AppUserRole> query = entityManager.createQuery(
                "SELECT r FROM AppUserRole as r WHERE name = 'CASHIER'", AppUserRole.class);

        return query.getSingleResult();
    }
}
