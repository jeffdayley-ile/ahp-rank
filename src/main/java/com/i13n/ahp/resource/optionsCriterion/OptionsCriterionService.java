package com.i13n.ahp.resource.optionsCriterion;

import com.i13n.ahp.model.OptionsCriterion;
import com.i13n.ahp.model.OptionsCriterionMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class OptionsCriterionService {
    private static OptionsCriterionMapper optionsCriterionMapper;

    public OptionsCriterionService(OptionsCriterionMapper optionsCriterionMapper) {
        this.optionsCriterionMapper = optionsCriterionMapper;
    }

    public static Optional<OptionsCriterion> getOptionsCriterion(UUID id) {
        return Optional.ofNullable(optionsCriterionMapper.findById(id));
    }

    public static Optional<OptionsCriterionResource> getOptionsCriterionResource(UUID id) {
        return getOptionsCriterion(id)
                .map(OptionsCriterionService::optionsCriterionToOptionsCriterionResource);
    }

    public static List<OptionsCriterionResource> getAllOptionsCriterionResourcesByAnalysisId(UUID analysisId) {
        List<OptionsCriterion> optionsCriterion = optionsCriterionMapper.findByAnalysisId(analysisId);
        List<OptionsCriterionResource> optionsCriterionResources = optionsCriterion.stream()
                .map(OptionsCriterionService::optionsCriterionToOptionsCriterionResource)
                .collect(Collectors.toList());
        return optionsCriterionResources;
    }

    public static List<OptionsCriterionResource> getAllOptionsCriterionResources() {
        List<OptionsCriterion> optionsCriterion = optionsCriterionMapper.findAll();
        List<OptionsCriterionResource> optionsCriterionResources = optionsCriterion.stream()
                .map(OptionsCriterionService::optionsCriterionToOptionsCriterionResource)
                .collect(Collectors.toList());
        return optionsCriterionResources;
    }

    public OptionsCriterion createOptionsCriterion(OptionsCriterion optionsCriterion) {
        optionsCriterion.setId(UUID.randomUUID());
        optionsCriterionMapper.insert(optionsCriterion);
        return optionsCriterion;
    }

    public OptionsCriterionResource createOptionsCriterionResource(OptionsCriterionResource optionsCriterionResource) {
        OptionsCriterion optionsCriterion = createOptionsCriterion(optionsCriterionResourceToOptionsCriterion(optionsCriterionResource));
        optionsCriterionResource.setId(optionsCriterion.getId());
        return optionsCriterionResource;
    }

    private static OptionsCriterion optionsCriterionResourceToOptionsCriterion(OptionsCriterionResource optionsCriterionResource) {
        OptionsCriterion optionsCriterion = new OptionsCriterion();
        optionsCriterion.setId(optionsCriterionResource.getId());
        optionsCriterion.setAnalysisId(optionsCriterionResource.getAnalysisId());
        optionsCriterion.setOptionsId(optionsCriterionResource.getOptionsId());
        optionsCriterion.setCriterionId(optionsCriterionResource.getCriterionId());
        optionsCriterion.setRank(optionsCriterionResource.getRank());
        optionsCriterion.setScore(optionsCriterionResource.getScore());
        return optionsCriterion;
    }

    private static OptionsCriterionResource optionsCriterionToOptionsCriterionResource(OptionsCriterion optionsCriterion) {
        OptionsCriterionResource optionsCriterionResource = new OptionsCriterionResource();
        optionsCriterionResource.setId(optionsCriterion.getId());
        optionsCriterionResource.setAnalysisId(optionsCriterion.getAnalysisId());
        optionsCriterionResource.setOptionsId(optionsCriterion.getOptionsId());
        optionsCriterionResource.setCriterionId(optionsCriterion.getCriterionId());
        optionsCriterionResource.setRank(optionsCriterion.getRank());
        optionsCriterionResource.setScore(optionsCriterion.getScore());
        return optionsCriterionResource;
    }

}
