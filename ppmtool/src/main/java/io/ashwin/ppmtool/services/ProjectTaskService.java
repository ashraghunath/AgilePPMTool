package io.ashwin.ppmtool.services;

import io.ashwin.ppmtool.domain.BackLog;
import io.ashwin.ppmtool.domain.ProjectTask;
import io.ashwin.ppmtool.exceptions.ProjectNotFoundException;
import io.ashwin.ppmtool.repositories.BackLogRepository;
import io.ashwin.ppmtool.repositories.ProjectTaskRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProjectTaskService {

    @Autowired
    BackLogRepository backLogRepository;

    @Autowired
    ProjectTaskRepository projectTaskRepository;

    @Autowired
    private ProjectService projectService;

    public ProjectTask addProjectTask(String projectIdentifier, ProjectTask projectTask, String username){

            //make sure project!= null i.e backlog exists
            BackLog backLog = projectService.findProjectByIdentifier(projectIdentifier,username).getBackLog();//backLogRepository.findByProjectIdentifier(projectIdentifier);
            //set backlog to pt
            projectTask.setBackLog(backLog);
            //project sequence will be like IDPRO-1, IDPRO-2...
            Integer backLogSequence = backLog.getPTSequence();
            //update BL sequence
            backLogSequence++;
            backLog.setPTSequence(backLogSequence);
            //add sequence to projectTask
            projectTask.setProjectSequence(projectIdentifier + "-" + backLogSequence);
            projectTask.setProjectIdentifier(projectIdentifier);
            //initial priority when priority null
            if (projectTask.getPriority() == null || projectTask.getPriority()==0 ) {
                projectTask.setPriority(3);
            }
            //initial status when status null
            if (projectTask.getStatus() == "" || projectTask.getStatus() == null) {
                projectTask.setStatus("TO_DO");
            }
            return projectTaskRepository.save(projectTask);
    }

    public List<ProjectTask> findBacklogById(String backlog_id, String username) {

        projectService.findProjectByIdentifier(backlog_id,username);

        List<ProjectTask> project = projectTaskRepository.findByProjectIdentifier(backlog_id);
        if(project.size()==0)
        {
            throw new ProjectNotFoundException("Project with ID "+backlog_id+ " does not exist");
        }

        return project;
    }

    public ProjectTask findPTByProjectSequence(String backlog_id, String sequence, String username){


        //ensure backlog exists
        projectService.findProjectByIdentifier(backlog_id,username);

        //ensure task exists
        ProjectTask projectTask = projectTaskRepository.findByProjectSequence(sequence);
        if(projectTask==null)
        {
            throw new ProjectNotFoundException("Project Task "+sequence+ " does not exist");
        }

        if(!projectTask.getProjectIdentifier().equals(backlog_id))
        {
            throw new ProjectNotFoundException("Project Task "+sequence+ " does not exist in project "+backlog_id);
        }

        return projectTask;
    }

    public ProjectTask updateByProjectSequence(ProjectTask updatedProjectTask, String backlog_id, String pt_id, String username)
    {
        ProjectTask projectTask = findPTByProjectSequence(backlog_id,pt_id,username);

        projectTask = updatedProjectTask;

        return projectTaskRepository.save(projectTask);
    }

    public void deletePTByProjectSequence(String backlog_id,String pt_id, String username)
    {
        ProjectTask projectTask = findPTByProjectSequence(backlog_id,pt_id, username);
        projectTaskRepository.delete(projectTask);
    }

}
