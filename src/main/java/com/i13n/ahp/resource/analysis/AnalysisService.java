package com.i13n.ahp.resource.analysis;

import com.i13n.ahp.model.Analysis;
import com.i13n.ahp.model.AnalysisMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class AnalysisService {
    private static AnalysisMapper analysisMapper;

    public AnalysisService(AnalysisMapper analysisMapper) {
        this.analysisMapper = analysisMapper;
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
        analysisMapper.insert(analysis);
        return analysis;
    }

    public AnalysisResource createAnalysisResource(AnalysisResource analysisResource) {
        Analysis analysis = createAnalysis(analysisResourceToAnalysis(analysisResource));
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

}
