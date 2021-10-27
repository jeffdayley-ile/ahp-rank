package com.i13n.ahp.resource.criteria;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/criteria")
public class CriteriaController {
    public static final String CRITERIA_TYPE = "application/vnd.criteria.v1+json";

    public final CriteriaService criteriaService;

    public CriteriaController(CriteriaService criteriaService) {
        this.criteriaService = criteriaService;
    }

    @GetMapping(produces = CRITERIA_TYPE)
    public ResponseEntity<List<CriteriaResource>> getAll() {
        List<CriteriaResource> criteriaResources = CriteriaService.getAllCriteriaResources();
        return ResponseEntity.ok(criteriaResources);
    }

    @GetMapping(path = "/{id}", produces = CRITERIA_TYPE)
    public ResponseEntity<CriteriaResource> getById(@PathVariable UUID id) {
        return ResponseEntity.of(CriteriaService.getCriteriaResource(id));
    }

    // TODO: Add Validation
    // - Validate analysisId exists in Analysis Table and is not null (update sql)
    // - Validate name is not null
    // - Validate rank is unique with analysis criterion (ie matching analysisIds)
    // - ID is ignored, not sure if there is anything to do for this.
    @PostMapping(consumes = CRITERIA_TYPE, produces = CRITERIA_TYPE)
    public ResponseEntity<CriteriaResource> create(@RequestBody CriteriaResource criteriaResource) {

        criteriaResource = criteriaService.createCriteriaResource(criteriaResource);

        UriComponents location = UriComponentsBuilder.fromPath("criteria/{uuid}")
                .buildAndExpand(criteriaResource.getId());
        return ResponseEntity.created(location.toUri()).body(criteriaResource);
    }
}
