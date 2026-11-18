package org.example.experiments.db.foodb.repo;

import org.example.experiments.db.foodb.model.AccessionNumber;

import java.time.OffsetDateTime;
import java.util.List;

public sealed interface AccessionNumberRepository extends FooDbRepository<Integer, String, AccessionNumber>
        permits AccessionNumberRepositoryImpl {
    List<AccessionNumber> findForPeriod(OffsetDateTime start, OffsetDateTime end, int limit, int offset);
}
