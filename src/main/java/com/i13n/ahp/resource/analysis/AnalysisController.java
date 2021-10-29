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
    private final CriteriaService criteriaService;
    private final OptionsService optionsService;
    private final OptionsCriterionService optionsCriterionService;

    public AnalysisController(
            AnalysisService analysisService,
            CriteriaService criteriaService,
            OptionsService optionsService,
            OptionsCriterionService optionsCriterionService
    ) {
        this.analysisService = analysisService;
        this.criteriaService = criteriaService;
        this.optionsService = optionsService;
        this.optionsCriterionService = optionsCriterionService;
    }

    @GetMapping(produces = ANALYSIS_TYPE)
    public ResponseEntity<List<AnalysisResource>> getAll() {
        List<AnalysisResource> analysisResources = AnalysisService.getAllAnalysisResources();
        return ResponseEntity.ok(analysisResources);
    }

    @GetMapping(path = "/{id}", produces = ANALYSIS_TYPE)
    public ResponseEntity<AnalysisResource> getById(@PathVariable UUID id) {
        return ResponseEntity.of(AnalysisService.getAnalysisResource(id));
    }

    @PostMapping(consumes = ANALYSIS_TYPE, produces = ANALYSIS_RETURN_TYPE)
    public ResponseEntity<AnalysisOutputResource> create(@RequestBody AnalysisInputResource inputResource) {

        // Return value
        AnalysisOutputResource analysisOutputResource = new AnalysisOutputResource();

        // Create the analysis
        AnalysisResource analysisResource = new AnalysisResource();
        analysisResource = analysisService.createAnalysisResource(analysisResource);
        UUID analysisId = analysisResource.getId();
        analysisOutputResource.setAnalysisId(analysisId);

        // Create the criteria
        HashMap<String, UUID> criteriaUuids = new HashMap<String, UUID>();
        List<CriteriaResource> criteriaResourceList = new ArrayList<>();
        List<String> criteriaNames = inputResource.getCriteria();
        for (int criteriaRank = 1; criteriaRank <= criteriaNames.size(); criteriaRank++) {
            String criteriaName = criteriaNames.get(criteriaRank - 1);
            CriteriaResource criteriaResource = new CriteriaResource();
            criteriaResource.setAnalysisId(analysisId);
            criteriaResource.setName(criteriaName);
            criteriaResource.setRank(criteriaRank);

            criteriaResource = criteriaService.createCriteriaResource(criteriaResource);
            criteriaUuids.put(criteriaName, criteriaResource.getId());
            criteriaResourceList.add(criteriaResource);
        }
        analysisOutputResource.setCriteria(criteriaResourceList);

        // Create the Options
        HashMap<String, UUID> optionsUuids = new HashMap<String, UUID>();
        List<OptionsResource> optionsResourceList = new ArrayList<>();
        List<String> optionNames = inputResource.getOptions();
        optionNames.forEach(optionName -> {
            OptionsResource optionsResource = new OptionsResource();
            optionsResource.setAnalysisId(analysisId);
            optionsResource.setName(optionName);

            optionsResource = optionsService.createOptionsResource(optionsResource);
            optionsUuids.put(optionName, optionsResource.getId());
            optionsResourceList.add(optionsResource);
        });
        analysisOutputResource.setOptions(optionsResourceList);

        // Create the options Criterion
        Map<String, List<String>> optionsCriterionMap = inputResource.getOptionsCriterion();
        List<OptionsCriterionResource> optionsCriterionResourceList = new ArrayList<>();
        optionsCriterionMap.forEach((criteriaName, rankedCriteria) -> {
            UUID criteriaId = criteriaUuids.get(criteriaName);
            for(int optionCriteriaRank = 1; optionCriteriaRank <= rankedCriteria.size(); optionCriteriaRank++) {
                String optionName = rankedCriteria.get(optionCriteriaRank - 1);
                UUID optionId = optionsUuids.get(optionName);

                OptionsCriterionResource optionsCriterionResource = new OptionsCriterionResource();
                optionsCriterionResource.setAnalysisId(analysisId);
                optionsCriterionResource.setOptionsId(optionId);
                optionsCriterionResource.setCriterionId(criteriaId);
                optionsCriterionResource.setRank(optionCriteriaRank);

                optionsCriterionResource = optionsCriterionService
                        .createOptionsCriterionResource(optionsCriterionResource);

                optionsCriterionResourceList.add(optionsCriterionResource);
            }
        });
        analysisOutputResource.setOptionsCriterion(optionsCriterionResourceList);

        UriComponents location = UriComponentsBuilder.fromPath("analysis/{{uuid}}")
                .buildAndExpand(analysisOutputResource.getAnalysisId());
        return ResponseEntity.created(location.toUri()).body(analysisOutputResource);    }
}
