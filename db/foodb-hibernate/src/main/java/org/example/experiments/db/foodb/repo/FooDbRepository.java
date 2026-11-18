package org.example.experiments.db.foodb.repo;

public interface FooDbRepository<PK, NK, T> {
    T findById(PK id);

    T findByNaturalKey(NK naturalKey);
}
