package io.ashwin.ppmtool.services;

import io.ashwin.ppmtool.domain.BackLog;
import io.ashwin.ppmtool.domain.Project;
import io.ashwin.ppmtool.exceptions.ProjectIdException;
import io.ashwin.ppmtool.repositories.BackLogRepository;
import io.ashwin.ppmtool.repositories.ProjectRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ProjectService {

    @Autowired
    ProjectRepository projectRepository;

    @Autowired
    BackLogRepository backLogRepository;

    public Project saveOrUpdateProject(Project project)
    {
        try {
            project.setProjectIdentifier(project.getProjectIdentifier().toUpperCase());

            //check if upate or save operation - no ID in save operation

            if(project.getId()==null)
            {
                BackLog backLog = new BackLog();
                project.setBackLog(backLog);
                backLog.setProject(project);
                backLog.setProjectIdentifier(project.getProjectIdentifier().toUpperCase());
            }

            if(project.getId()!=null)
            {
                project.setBackLog(backLogRepository.findByProjectIdentifier(project.getProjectIdentifier().toUpperCase()));
            }

            return projectRepository.save(project);
        }
        catch (Exception e)
        {
            throw new ProjectIdException("Project ID "+project.getProjectIdentifier() + " already exists");
        }
    }

    public Project findProjectByIdentifier(String projectId)
    {
        Project project = projectRepository.findByProjectIdentifier(projectId.toUpperCase());

        if(project==null)
        {
            throw new ProjectIdException("Project ID "+projectId+ " does not exist");
        }

        return project;
    }

    public Iterable<Project> findAllProjects()
    {
        Iterable<Project> allProjects = projectRepository.findAll();

        return allProjects;
    }

    public void deleteProjectById(String projectId)
    {
        Project project = projectRepository.findByProjectIdentifier(projectId.toUpperCase());
        if(project==null)
        {
            throw new ProjectIdException("Project ID "+projectId+ " does not exist");
        }

        projectRepository.delete(project);
    }
}
