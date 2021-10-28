package com.i13n.ahp.resource.options;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/options")
public class OptionsController {
    public static final String OPTIONS_TYPE = "application/vnd.options.v1+json";

    public final OptionsService optionsService;

    public OptionsController(OptionsService optionsService) {
        this.optionsService = optionsService;
    }

    @GetMapping(produces = OPTIONS_TYPE)
    public ResponseEntity<List<OptionsResource>> getAll() {
        List<OptionsResource> optionsResources = OptionsService.getAllOptionsResources();
        return ResponseEntity.ok(optionsResources);
    }

    @GetMapping(path = "/{id}", produces = OPTIONS_TYPE)
    public ResponseEntity<OptionsResource> getById(@PathVariable UUID id) {
        return ResponseEntity.of(OptionsService.getOptionsResource(id));
    }

    // TODO: Add Validation
    // - Validate analysisId exists in Analysis Table and is not null (update sql)
    // - Validate name is not null
    // - Validate rank is unique with analysis criterion (ie matching analysisIds)
    // - ID is ignored, not sure if there is anything to do for this.
    @PostMapping(consumes = OPTIONS_TYPE, produces = OPTIONS_TYPE)
    public ResponseEntity<OptionsResource> create(@RequestBody OptionsResource optionsResource) {

        optionsResource = optionsService.createOptionsResource(optionsResource);

        UriComponents location = UriComponentsBuilder.fromPath("options/{uuid}")
                .buildAndExpand(optionsResource.getId());
        return ResponseEntity.created(location.toUri()).body(optionsResource);
    }
}
