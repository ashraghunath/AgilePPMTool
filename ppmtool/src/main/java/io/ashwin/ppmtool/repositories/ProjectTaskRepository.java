package io.ashwin.ppmtool.repositories;

import io.ashwin.ppmtool.domain.ProjectTask;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProjectTaskRepository extends CrudRepository<ProjectTask, Long> {


    List<ProjectTask> findByProjectIdentifier(String id);

    ProjectTask findByProjectSequence(String id);
}
