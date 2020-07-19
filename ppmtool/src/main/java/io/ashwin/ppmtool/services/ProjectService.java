package io.ashwin.ppmtool.services;

import io.ashwin.ppmtool.domain.BackLog;
import io.ashwin.ppmtool.domain.Project;
import io.ashwin.ppmtool.domain.User;
import io.ashwin.ppmtool.exceptions.ProjectIdException;
import io.ashwin.ppmtool.exceptions.ProjectNotFoundException;
import io.ashwin.ppmtool.repositories.BackLogRepository;
import io.ashwin.ppmtool.repositories.ProjectRepository;
import io.ashwin.ppmtool.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ProjectService {

    @Autowired
    ProjectRepository projectRepository;

    @Autowired
    BackLogRepository backLogRepository;

    @Autowired
    UserRepository userRepository;

    public Project saveOrUpdateProject(Project project, String username)
    {

        if(project.getId()!=null)
        {
            Project existingProject = projectRepository.findByProjectIdentifier(project.getProjectIdentifier());
            if(existingProject!=null && !existingProject.getProjectLeader().equals(username)){
                throw new ProjectNotFoundException("Project not found in your account");
            }
            else if(existingProject == null )
            {
                //for when user tried to update something that doesnt exit(has passed id) , instead of creating new throw exception
                throw  new ProjectNotFoundException("Project with id "+project.getProjectIdentifier()+" cannot be updated as it does not exist");
            }
        }

        try {
            User user = userRepository.findByUsername(username);
            project .setUser(user); //setting the one to many relationship
            project.setProjectLeader(user.getUsername());


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

    public Project findProjectByIdentifier(String projectId, String username)
    {
        Project project = projectRepository.findByProjectIdentifier(projectId.toUpperCase());
        if(project==null)
        {
            throw new ProjectIdException("Project ID "+projectId+ " does not exist");
        }

        if(!project.getProjectLeader().equals(username))
        {
            throw new ProjectNotFoundException("Project not found in yout account");
        }

        return project;
    }

    public Iterable<Project> findAllProjects(String username)
    {
        Iterable<Project> allProjects = projectRepository.findAllByProjectLeader(username);
        return allProjects;
    }

    public void deleteProjectById(String projectId, String username)
    {
        projectRepository.delete(findProjectByIdentifier(projectId,username));
    }
}
