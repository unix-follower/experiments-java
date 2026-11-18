package org.example.experiments.db.foodb.repo;

import jakarta.persistence.EntityManager;
import org.example.experiments.db.foodb.model.AccessionNumber;

import java.time.OffsetDateTime;
import java.util.List;

public final class AccessionNumberRepositoryImpl implements AccessionNumberRepository {
    private final EntityManager em;

    public AccessionNumberRepositoryImpl(EntityManager em) {
        this.em = em;
    }

    @Override
    public List<AccessionNumber> findForPeriod(OffsetDateTime start, OffsetDateTime end, int limit, int offset) {
        final var query = em.createNamedQuery(AccessionNumber.FIND_FOR_PERIOD_NQ, AccessionNumber.class)
                .setParameter("start", start)
                .setParameter("end", end)
                .setFirstResult(offset)
                .setMaxResults(limit);
        return query.getResultList();
    }

    @Override
    public AccessionNumber findById(Integer id) {
        final var query = em.createNamedQuery(AccessionNumber.FIND_BY_ID_NQ, AccessionNumber.class)
                .setParameter("id", id);
        return query.getSingleResult();
    }

    @Override
    public AccessionNumber findByNaturalKey(String naturalKey) {
        final var query = em.createNamedQuery(AccessionNumber.FIND_BY_NATURAL_NQ, AccessionNumber.class)
                .setParameter("number", naturalKey);
        return query.getSingleResult();
    }
}
