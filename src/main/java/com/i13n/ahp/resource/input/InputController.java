package com.i13n.ahp.resource.input;

import com.i13n.ahp.resource.analysis.AnalysisResource;
import com.i13n.ahp.resource.analysis.AnalysisService;
import com.i13n.ahp.resource.criteria.CriteriaResource;
import com.i13n.ahp.resource.criteria.CriteriaService;
import com.i13n.ahp.resource.options.OptionsResource;
import com.i13n.ahp.resource.options.OptionsService;
import com.i13n.ahp.resource.optionsCriterion.OptionsCriterionResource;
import com.i13n.ahp.resource.optionsCriterion.OptionsCriterionService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.*;


@RestController
@RequestMapping("/input")
public class InputController {
    public static final String AHP_INPUT_TYPE = "application/vnd.ahpinput.v1+json";
    public static final String AHP_INPUT_RETURN_TYPE = "application/vnd.ahpinputreturn.v1+json";

//    public final AHPInputService ahpInputService;
    private final AnalysisService analysisService;
    private final CriteriaService criteriaService;
    private final OptionsService optionsService;
    private final OptionsCriterionService optionsCriterionService;

    public InputController(
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

    @PostMapping(consumes = AHP_INPUT_TYPE, produces = AHP_INPUT_RETURN_TYPE)
    public ResponseEntity<InputReturnResource> create(@RequestBody InputResource ahpInputResource) {
        // Return value
        InputReturnResource inputReturnResource = new InputReturnResource();

        // Create the analysis
        AnalysisResource analysisResource = new AnalysisResource();
        analysisResource = analysisService.createAnalysisResource(analysisResource);
        UUID analysisId = analysisResource.getId();
        inputReturnResource.setAnalysisId(analysisId);

        // Create the criteria
        HashMap<String, UUID> criteriaUuids = new HashMap<String, UUID>();
        List<CriteriaResource> criteriaResourceList = new ArrayList<>();
        List<String> criteriaNames = ahpInputResource.getCriteria();
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
        inputReturnResource.setCriteria(criteriaResourceList);

        // Create the Options
        HashMap<String, UUID> optionsUuids = new HashMap<String, UUID>();
        List<OptionsResource> optionsResourceList = new ArrayList<>();
        List<String> optionNames = ahpInputResource.getOptions();
        optionNames.forEach(optionName -> {
            OptionsResource optionsResource = new OptionsResource();
            optionsResource.setAnalysisId(analysisId);
            optionsResource.setName(optionName);

            optionsResource = optionsService.createOptionsResource(optionsResource);
            optionsUuids.put(optionName, optionsResource.getId());
            optionsResourceList.add(optionsResource);
        });
        inputReturnResource.setOptions(optionsResourceList);

        // Create the options Criterion
        Map<String, List<String>> optionsCriterionMap = ahpInputResource.getOptionsCriterion();
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
        inputReturnResource.setOptionsCriterion(optionsCriterionResourceList);

        UriComponents location = UriComponentsBuilder.fromPath("analysis/{{uuid}}")
                .buildAndExpand(inputReturnResource.getAnalysisId());
        return ResponseEntity.created(location.toUri()).body(inputReturnResource);
    }


}
