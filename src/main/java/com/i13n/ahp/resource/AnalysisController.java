package com.i13n.ahp.resource;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/analysis")
public class AnalysisController {
    public static final String ANALYSIS_TYPE = "application/vnd.analysis.v1+json";

    public final AnalysisService analysisService;

    public AnalysisController(AnalysisService analysisService) {
        this.analysisService = analysisService;
    }

    // TODO: Get all analysis
    @GetMapping(produces = ANALYSIS_TYPE)
    public ResponseEntity<List<AnalysisResource>> getAll() {
        List<AnalysisResource> analysisResources = AnalysisService.getAllAnalysisResources();
        return ResponseEntity.ok(analysisResources);
    }

    @GetMapping(path = "/{id}", produces = ANALYSIS_TYPE)
    public ResponseEntity<AnalysisResource> getById(@PathVariable UUID id) {
        return ResponseEntity.of(AnalysisService.getAnalysisResource(id));
    }

    @PostMapping(produces = ANALYSIS_TYPE)
    public ResponseEntity<AnalysisResource> create() {

        AnalysisResource analysisResource = new AnalysisResource();
        analysisResource = analysisService.createAnalysisResource(analysisResource);

        UriComponents location = UriComponentsBuilder.fromPath("analysis/{uuid}")
                .buildAndExpand(analysisResource.getId());
        return ResponseEntity.created(location.toUri()).body(analysisResource);
    }
}
