package io.ashwin.ppmtool.web;

import io.ashwin.ppmtool.domain.ProjectTask;
import io.ashwin.ppmtool.services.MapValidationErrorService;
import io.ashwin.ppmtool.services.ProjectTaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/backlog")
@CrossOrigin
public class BackLogController  {

    @Autowired
    private ProjectTaskService projectTaskService;

    @Autowired
    MapValidationErrorService mapValidationErrorService;

    //ALWAYS HAVE BindingResult RIGHT AFTER @Valid ARGUMENT
    @PostMapping("/{backlog_id}")
    public ResponseEntity<?> addProjectTaskToBackLog(@Valid @RequestBody ProjectTask projectTask, BindingResult bindingResult, @PathVariable String backlog_id)
    {
        ResponseEntity<?> errorMap = mapValidationErrorService.mapValidationService(bindingResult);
        if(errorMap!=null) return errorMap;

        ProjectTask projectTask1 = projectTaskService.addProjectTask(backlog_id, projectTask);

        return new ResponseEntity<ProjectTask>(projectTask1, HttpStatus.CREATED);
    }

    //patchmapping
    @PatchMapping("/{backlogId}/{pt_id}")
    public ResponseEntity<?> updateProjectTask(@Valid @RequestBody ProjectTask projectTask, BindingResult bindingResult, @PathVariable String backlogId, @PathVariable String pt_id)
    {

        ResponseEntity<?> errorMap = mapValidationErrorService.mapValidationService(bindingResult);
        if(errorMap!=null) return errorMap;


        ProjectTask updatedProjectTask = projectTaskService.updateByProjectSequence(projectTask,backlogId, pt_id);
        return new ResponseEntity<ProjectTask>(projectTask, HttpStatus.OK);
    }

    @GetMapping("/{backlog_id}")
    public List<ProjectTask> getProjectBacklog(@PathVariable String backlog_id)
    {
        return projectTaskService.findBacklogById(backlog_id);
    }

    @GetMapping("/{backlogId}/{pt_id}")
    public ResponseEntity<?> getProjectTask(@PathVariable String backlogId, @PathVariable String pt_id)
    {
        ProjectTask projectTask = projectTaskService.findPTByProjectSequence(backlogId, pt_id);
        return new ResponseEntity<ProjectTask>(projectTask, HttpStatus.OK);
    }



    @DeleteMapping("/{backlogId}/{pt_id}")
    public ResponseEntity<?> deleteProjectTask(@PathVariable String backlogId, @PathVariable String pt_id)
    {
        projectTaskService.deletePTByProjectSequence(backlogId, pt_id);
        return new ResponseEntity<String>("Project task "+pt_id+" was deleted successfully",HttpStatus.OK);
    }

}
