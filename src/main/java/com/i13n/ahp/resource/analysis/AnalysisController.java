package com.i13n.ahp.resource.analysis;

import com.i13n.ahp.resource.criteria.CriteriaResource;
import com.i13n.ahp.resource.criteria.CriteriaService;
import com.i13n.ahp.resource.options.OptionsResource;
import com.i13n.ahp.resource.options.OptionsService;
import com.i13n.ahp.resource.optionsCriterion.OptionsCriterionResource;
import com.i13n.ahp.resource.optionsCriterion.OptionsCriterionService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.*;

@RestController
@RequestMapping("/analysis")
public class AnalysisController {
    public static final String ANALYSIS_TYPE = "application/vnd.analysis.v1+json";
    public static final String ANALYSIS_RETURN_TYPE = "application/vnd.analysis_return.v1+json";

    private final AnalysisService analysisService;

    public AnalysisController(AnalysisService analysisService) {
        this.analysisService = analysisService;
    }

    @GetMapping(produces = ANALYSIS_TYPE)
    public ResponseEntity<List<AnalysisResource>> getAll() {
        List<AnalysisResource> analysisResources = AnalysisService.getAllAnalysisResources();
        return ResponseEntity.ok(analysisResources);
    }

    // TODO: Return the full AnalysisOutputResource Object
    @GetMapping(path = "/{id}", produces = ANALYSIS_TYPE)
    public ResponseEntity<AnalysisResource> getById(@PathVariable UUID id) {
        return ResponseEntity.of(AnalysisService.getAnalysisResource(id));
    }

    @PostMapping(consumes = ANALYSIS_TYPE, produces = ANALYSIS_RETURN_TYPE)
    public ResponseEntity<AnalysisOutputResource> create(@RequestBody AnalysisInputResource inputResource) {

        AnalysisOutputResource analysisOutputResource = analysisService.createAnalysisOutputResource(inputResource);

        UriComponents location = UriComponentsBuilder.fromPath("analysis/{{uuid}}")
                .buildAndExpand(analysisOutputResource.getAnalysisId());
        return ResponseEntity.created(location.toUri()).body(analysisOutputResource);    }
}
