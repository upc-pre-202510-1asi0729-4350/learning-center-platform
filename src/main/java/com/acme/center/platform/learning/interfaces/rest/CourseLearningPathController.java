package com.acme.center.platform.learning.interfaces.rest;

import com.acme.center.platform.learning.domain.model.commands.AddTutorialToCourseLearningPathCommand;
import com.acme.center.platform.learning.domain.model.queries.GetLearningPathItemByCourseIdAndTutorialIdQuery;
import com.acme.center.platform.learning.domain.model.valueobjects.TutorialId;
import com.acme.center.platform.learning.domain.services.CourseCommandService;
import com.acme.center.platform.learning.domain.services.CourseQueryService;
import com.acme.center.platform.learning.interfaces.rest.resources.LearningPathItemResource;
import com.acme.center.platform.learning.interfaces.rest.transform.LearningPathItemResourceFromEntityAssembler;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
@RequestMapping(value = "/api/v1/courses/{courseId}/learning-path-items", produces = APPLICATION_JSON_VALUE)
@Tag(name = "Courses", description = "Available Course Endpoints")
public class CourseLearningPathController {
    private final CourseCommandService courseCommandService;
    private final CourseQueryService courseQueryService;

    public CourseLearningPathController(CourseCommandService courseCommandService, CourseQueryService queryService, CourseQueryService courseQueryService) {
        this.courseCommandService = courseCommandService;
        this.courseQueryService = courseQueryService;
    }

    @PostMapping("/{tutorialId}")
    @Operation(summary = "Add a tutorial to the course learning path")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Tutorial added to course learning path successfully"),
            @ApiResponse(responseCode = "404", description = "Course or tutorial not found"),
    })
    public ResponseEntity<LearningPathItemResource> addTutorialToCourseLearningPath(@PathVariable Long courseId, @PathVariable Long tutorialId) {
        var tutorialIdentifier = new TutorialId(tutorialId);
        courseCommandService.handle(new AddTutorialToCourseLearningPathCommand(tutorialIdentifier, courseId));
        var getLearningPathItemByCourseIdAndTutorialId = new GetLearningPathItemByCourseIdAndTutorialIdQuery(courseId, tutorialIdentifier);
        var learningPathItem = courseQueryService.handle(getLearningPathItemByCourseIdAndTutorialId);
        if(learningPathItem.isEmpty()) return ResponseEntity.notFound().build();
        var learningPathItemEntity = learningPathItem.get();
        var learningPathItemResource = LearningPathItemResourceFromEntityAssembler.toResourceFromEntity(learningPathItemEntity);
        return new ResponseEntity<>(learningPathItemResource, HttpStatus.CREATED);
    }

}
