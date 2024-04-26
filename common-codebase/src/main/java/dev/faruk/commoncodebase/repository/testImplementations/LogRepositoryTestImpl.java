package dev.faruk.commoncodebase.repository.testImplementations;

import dev.faruk.commoncodebase.entity.AppLog;
import dev.faruk.commoncodebase.repository.base.LogRepository;

import java.util.ArrayList;
import java.util.List;

public class LogRepositoryTestImpl implements LogRepository {
    private final List<AppLog> cache = new ArrayList<>();
    private long idCounter = 1;

    @Override
    public void save(AppLog appLog) {
        appLog.setId(idCounter++);
        cache.add(appLog);
    }

    @Override
    public List<AppLog> findAll(
            Integer page,
            Integer size
    ) {
        if (page == null || size == null) {
            throw new IllegalArgumentException("Page and size are required");
        }

        int start = (page - 1) * size;
        int end = Math.min(start + size, cache.size());
        if (start < 0 || start >= cache.size()) return List.of();
        return cache.subList(start, end);
    }

    @Override
    public AppLog findById(Long id) {
        return cache.stream().filter(appLog -> appLog.getId().equals(id)).findFirst().orElse(null);
    }
}
