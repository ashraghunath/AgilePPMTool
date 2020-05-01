package io.ashwin.ppmtool.repositories;

import io.ashwin.ppmtool.domain.BackLog;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BackLogRepository extends CrudRepository<BackLog, Long> {

    BackLog findByProjectIdentifier(String identifier);

}
