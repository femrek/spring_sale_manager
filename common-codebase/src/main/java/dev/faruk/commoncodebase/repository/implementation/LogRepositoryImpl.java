package dev.faruk.commoncodebase.repository.implementation;

import dev.faruk.commoncodebase.entity.AppLog;
import dev.faruk.commoncodebase.repository.base.LogRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class LogRepositoryImpl implements LogRepository {
    private final EntityManager entityManager;

    @Autowired
    public LogRepositoryImpl(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Override
    @Transactional
    public void save(AppLog appLog) {
        entityManager.persist(appLog);
        entityManager.flush();
    }

    @Override
    public List<AppLog> findAll(Integer page, Integer size) {
        TypedQuery<AppLog> query = entityManager.createQuery("SELECT a FROM AppLog a", AppLog.class);

        // pagination
        query.setFirstResult((page - 1) * size);
        query.setMaxResults(size);

        return query.getResultList();
    }

    @Override
    public AppLog findById(Long id) {
        return entityManager.find(AppLog.class, id);
    }
}
