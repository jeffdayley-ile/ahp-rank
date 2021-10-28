package com.i13n.ahp.resource.optionsCriterion;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/options_criterion")
public class OptionsCriterionController {
    public static final String OPTIONS_CRITERION_TYPE = "application/vnd.optionsCriterion.v1+json";

    public final OptionsCriterionService optionsCriterionService;

    public OptionsCriterionController(OptionsCriterionService optionsCriterionService) {
        this.optionsCriterionService = optionsCriterionService;
    }

    @GetMapping(produces = OPTIONS_CRITERION_TYPE)
    public ResponseEntity<List<OptionsCriterionResource>> getAll() {
        List<OptionsCriterionResource> optionsCriterionResources = OptionsCriterionService.getAllOptionsCriterionResources();
        return ResponseEntity.ok(optionsCriterionResources);
    }

    @GetMapping(path = "/{id}", produces = OPTIONS_CRITERION_TYPE)
    public ResponseEntity<OptionsCriterionResource> getById(@PathVariable UUID id) {
        return ResponseEntity.of(OptionsCriterionService.getOptionsCriterionResource(id));
    }

    // TODO: Add Validation
    // - Validate analysisId exists in Analysis Table and is not null (update sql)
    // - Validate name is not null
    // - Validate rank is unique with analysis criterion (ie matching analysisIds)
    // - ID is ignored, not sure if there is anything to do for this.
    @PostMapping(consumes = OPTIONS_CRITERION_TYPE, produces = OPTIONS_CRITERION_TYPE)
    public ResponseEntity<OptionsCriterionResource> create(@RequestBody OptionsCriterionResource optionsCriterionResource) {

        optionsCriterionResource = optionsCriterionService.createOptionsCriterionResource(optionsCriterionResource);

        UriComponents location = UriComponentsBuilder.fromPath("options_criterion/{uuid}")
                .buildAndExpand(optionsCriterionResource.getId());
        return ResponseEntity.created(location.toUri()).body(optionsCriterionResource);
    }
}
