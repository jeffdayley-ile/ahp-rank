package com.i13n.ahp.resource.options;

import com.i13n.ahp.model.AHPOptionsMapper;
import com.i13n.ahp.model.Options;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class OptionsService {
    private static AHPOptionsMapper optionsMapper;

    public OptionsService(AHPOptionsMapper optionsMapper) {
        this.optionsMapper = optionsMapper;
    }

    public static Optional<Options> getOptions(UUID id) {
        return Optional.ofNullable(optionsMapper.findById(id));
    }

    public static Optional<OptionsResource> getOptionsResource(UUID id) {
        return getOptions(id)
                .map(OptionsService::optionsToOptionsResource);
    }

    public static List<OptionsResource> getAllOptionsResources() {
        List<Options> options = optionsMapper.findAll();
        List<OptionsResource> optionsResources = options.stream()
                .map(OptionsService::optionsToOptionsResource)
                .collect(Collectors.toList());
        return optionsResources;
    }

    public Options createOptions(Options options) {
        options.setId(UUID.randomUUID());
        optionsMapper.insert(options);
        return options;
    }

    public OptionsResource createOptionsResource(OptionsResource optionsResource) {
        Options options = createOptions(optionsResourceToOptions(optionsResource));
        optionsResource.setId(options.getId());
        return optionsResource;
    }

    private static Options optionsResourceToOptions(OptionsResource optionsResource) {
        Options options = new Options();
        options.setId(optionsResource.getId());
        options.setAnalysisId(optionsResource.getAnalysisId());
        options.setName(optionsResource.getName());
        options.setRank(optionsResource.getRank());
        options.setTotalScore(optionsResource.getTotalScore());
        return options;
    }

    private static OptionsResource optionsToOptionsResource(Options options) {
        OptionsResource optionsResource = new OptionsResource();
        optionsResource.setId(options.getId());
        optionsResource.setAnalysisId(options.getAnalysisId());
        optionsResource.setName(options.getName());
        optionsResource.setRank(options.getRank());
        optionsResource.setTotalScore(options.getTotalScore());
        return optionsResource;
    }

}
