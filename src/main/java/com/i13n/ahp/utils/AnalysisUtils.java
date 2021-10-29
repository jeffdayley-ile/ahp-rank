package com.i13n.ahp.utils;

import com.i13n.ahp.model.Analysis;
import com.i13n.ahp.resource.analysis.AnalysisResource;

public class AnalysisUtils {

    public static Analysis toAnalysis(AnalysisResource analysisResource) {
        Analysis analysis = new Analysis();
        analysis.setId(analysisResource.getId());
        return analysis;
    }

    public static AnalysisResource toAnalysisResource(Analysis analysis) {
        AnalysisResource analysisResource = new AnalysisResource();
        analysisResource.setId(analysis.getId());
        return analysisResource;
    }

}
