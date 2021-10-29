package com.i13n.ahp.resource.analysis;

import com.i13n.ahp.model.Analysis;
import com.i13n.ahp.model.AnalysisMapper;
import com.i13n.ahp.model.Options;
import com.i13n.ahp.resource.criteria.CriteriaResource;
import com.i13n.ahp.resource.criteria.CriteriaService;
import com.i13n.ahp.resource.options.OptionsResource;
import com.i13n.ahp.resource.options.OptionsService;
import com.i13n.ahp.resource.optionsCriterion.OptionsCriterionResource;
import com.i13n.ahp.resource.optionsCriterion.OptionsCriterionService;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Service
public class AnalysisService {
    private static AnalysisMapper analysisMapper;
    private CriteriaService criteriaService;
    private OptionsService optionsService;
    private OptionsCriterionService optionsCriterionService;

    public AnalysisService(
            AnalysisMapper analysisMapper,
            CriteriaService criteriaService,
            OptionsService optionsService,
            OptionsCriterionService optionsCriterionService
    ) {
        this.analysisMapper = analysisMapper;
        this.criteriaService = criteriaService;
        this.optionsService = optionsService;
        this.optionsCriterionService = optionsCriterionService;
    }

    public static Optional<Analysis> getAnalysis(UUID id) {
        return Optional.ofNullable(analysisMapper.findById(id));
    }

    public static Optional<AnalysisResource> getAnalysisResource(UUID id) {
        return getAnalysis(id)
                .map(AnalysisService::analysisToAnalysisResource);
    }

    public static List<AnalysisResource> getAllAnalysisResources() {
        List<Analysis> analyses = analysisMapper.findAll();
        List<AnalysisResource> analysisResources = analyses.stream()
                .map(AnalysisService::analysisToAnalysisResource)
                .collect(Collectors.toList());
        return analysisResources;
    }

    public Analysis createAnalysis(Analysis analysis) {
        analysis.setId(UUID.randomUUID());
        analysisMapper.insert(analysis);
        return analysis;
    }

    public AnalysisOutputResource createAnalysisOutputResource(AnalysisInputResource analysisInputResource) {
        // Return value
        AnalysisOutputResource analysisOutputResource = new AnalysisOutputResource();

        // Create the analysis
        AnalysisResource analysisResource = new AnalysisResource();
        analysisResource = createAnalysisResource(analysisResource);
        UUID analysisId = analysisResource.getId();
        analysisOutputResource.setAnalysisId(analysisId);

        // Create the criteria
        HashMap<String, UUID> criteriaUuids = new HashMap<String, UUID>();
        List<CriteriaResource> criteriaResourceList = new ArrayList<>();
        List<String> criteriaNames = analysisInputResource.getCriteria();
        Integer criteriaSize = criteriaNames.size();
        for (int criteriaRank = 1; criteriaRank <= criteriaSize; criteriaRank++) {
            String criteriaName = criteriaNames.get(criteriaRank - 1);
            CriteriaResource criteriaResource = new CriteriaResource();
            criteriaResource.setAnalysisId(analysisId);
            criteriaResource.setName(criteriaName);
            criteriaResource.setRank(criteriaRank);
            criteriaResource.setScore(normalizedReciprocalRank(criteriaRank, criteriaSize));

            criteriaResource = criteriaService.createCriteriaResource(criteriaResource);
            criteriaUuids.put(criteriaName, criteriaResource.getId());
            criteriaResourceList.add(criteriaResource);
        }

        analysisOutputResource.setCriteria(criteriaResourceList);

        // Create the Options
        HashMap<String, UUID> optionsUuids = new HashMap<String, UUID>();
        List<OptionsResource> optionsResourceList = new ArrayList<>();
        List<String> optionNames = analysisInputResource.getOptions();
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
        Map<String, List<String>> optionsCriterionMap = analysisInputResource.getOptionsCriterion();
        List<OptionsCriterionResource> optionsCriterionResourceList = new ArrayList<>();
        optionsCriterionMap.forEach((criteriaName, rankedCriteria) -> {
            UUID criteriaId = criteriaUuids.get(criteriaName);
            Integer rankedCriteriaSize = rankedCriteria.size();
            for(int optionCriteriaRank = 1; optionCriteriaRank <= rankedCriteriaSize; optionCriteriaRank++) {
                String optionName = rankedCriteria.get(optionCriteriaRank - 1);
                UUID optionId = optionsUuids.get(optionName);

                OptionsCriterionResource optionsCriterionResource = new OptionsCriterionResource();
                optionsCriterionResource.setAnalysisId(analysisId);
                optionsCriterionResource.setOptionsId(optionId);
                optionsCriterionResource.setCriterionId(criteriaId);
                optionsCriterionResource.setRank(optionCriteriaRank);
                optionsCriterionResource.setScore(normalizedReciprocalRank(optionCriteriaRank, rankedCriteriaSize));

                optionsCriterionResource = optionsCriterionService
                        .createOptionsCriterionResource(optionsCriterionResource);

                optionsCriterionResourceList.add(optionsCriterionResource);
            }
        });
        analysisOutputResource.setOptionsCriterion(optionsCriterionResourceList);

        analysisOutputResource = finalizeScoring(analysisOutputResource);

        return analysisOutputResource;
    }

    public AnalysisResource createAnalysisResource(AnalysisResource analysisResource) {
        Analysis analysis = createAnalysis(analysisResourceToAnalysis(analysisResource));
        analysisResource.setId(analysis.getId());
        return analysisResource;
    }

    private static Analysis analysisResourceToAnalysis(AnalysisResource analysisResource) {
        Analysis analysis = new Analysis();
        analysis.setId(analysisResource.getId());
        return analysis;
    }

    private static AnalysisResource analysisToAnalysisResource(Analysis analysis) {
        AnalysisResource analysisResource = new AnalysisResource();
        analysisResource.setId(analysis.getId());
        return analysisResource;
    }

    // TODO: Add unit tests for normalizedReciprocalRank
    private Float normalizedReciprocalRank(Integer itemRank, Integer countOfRanks ) {
        Integer sumOfRanks = IntStream
                .rangeClosed(1,countOfRanks)
                .boxed()
                .reduce(0, Integer::sum);

        Float rankReciprocal = 1 / Float.valueOf(itemRank);
        Float normalizedReciprocal = rankReciprocal / sumOfRanks;

        return normalizedReciprocal;
    }

    // TODO: remove reliance on prior scoring of criteria and optionsCriterion
    // TODO: Add a winning option variable to the AnalysisOutputResource object
    private static AnalysisOutputResource finalizeScoring(AnalysisOutputResource analysisOutputResource) {

        List<OptionsResource> scoredOptionList = new ArrayList<>();
        List<OptionsCriterionResource> optionsCriterionResourceList = analysisOutputResource.getOptionsCriterion();
        analysisOutputResource.getOptions().forEach(optionsResource -> {
            UUID optionId = optionsResource.getId();
            List<OptionsCriterionResource> ocFilteredByOptionId = analysisOutputResource.getOptionsCriterion().stream()
                    .filter( oc -> oc.getOptionsId().equals(optionId) )
                    .collect(Collectors.toList());

            List<Float> scoreProducts = new ArrayList<>();
            analysisOutputResource.getCriteria().forEach(criteriaResource -> {
                UUID criteriaId = criteriaResource.getId();
                Float criteriaScore = criteriaResource.getScore();
                // TODO: This should only return 1 score.  We should ensure this happens
                Double optionsCriterionScore = ocFilteredByOptionId.stream()
                        .filter(oc -> oc.getCriterionId().equals(criteriaId))
                        .mapToDouble(oc -> oc.getScore())
                        .findFirst()
                        .getAsDouble();


                scoreProducts.add(criteriaScore * optionsCriterionScore.floatValue());
            });

            Double optionTotalScore = scoreProducts.stream()
                    .mapToDouble(score -> score)
                    .reduce(Double::sum)
                    .getAsDouble();

            optionsResource.setTotalScore(optionTotalScore.floatValue());
            scoredOptionList.add(optionsResource);
        });

        analysisOutputResource.setOptions(scoredOptionList);
        return analysisOutputResource;
    }

}
