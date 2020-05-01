package io.ashwin.ppmtool.web;

import io.ashwin.ppmtool.domain.Project;
import io.ashwin.ppmtool.services.MapValidationErrorService;
import io.ashwin.ppmtool.services.ProjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/project")
@CrossOrigin
public class ProjectController {

    @Autowired
    ProjectService projectService;

    @Autowired
    MapValidationErrorService mapValidationErrorService;

    @PostMapping("")
    public ResponseEntity<?> createNewProject(@Valid @RequestBody Project project, BindingResult bindingResult)
    {
        ResponseEntity<?> errorMap = mapValidationErrorService.mapValidationService(bindingResult);
        if (errorMap!=null) return errorMap;

        Project project1 = projectService.saveOrUpdateProject(project);
        return new ResponseEntity<Project>(project1, HttpStatus.CREATED);
    }

    @GetMapping("/{projectId}")
    public ResponseEntity<?> getProjectById(@PathVariable String projectId)
    {
        Project project = projectService.findProjectByIdentifier(projectId);
        return new ResponseEntity<>(project,HttpStatus.OK);
    }

    @GetMapping("/allProjects")
    public Iterable<Project> getAllProjects()
    {
        return projectService.findAllProjects();
    }

    @DeleteMapping("/{projectId}")
    public ResponseEntity<?> deleteProject(@PathVariable String projectId)
    {
        projectService.deleteProjectById(projectId);
        return new ResponseEntity<String>("Project "+projectId+" deleted",HttpStatus.OK);
    }
}
