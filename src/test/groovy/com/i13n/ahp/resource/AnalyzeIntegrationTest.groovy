package com.i13n.ahp.resource

import com.fasterxml.jackson.databind.ObjectMapper
import com.i13n.ahp.model.AHPOptionsMapper
import com.i13n.ahp.model.AnalysisMapper
import com.i13n.ahp.model.CriteriaMapper
import com.i13n.ahp.model.OptionsCriterionMapper
import com.i13n.ahp.resource.analysis.AnalysisController
import com.i13n.ahp.resource.analysis.AnalysisResource
import com.i13n.ahp.resource.criteria.CriteriaController
import com.i13n.ahp.resource.criteria.CriteriaResource
import com.i13n.ahp.resource.options.OptionsController
import com.i13n.ahp.resource.options.OptionsResource
import com.i13n.ahp.resource.optionsCriterion.OptionsCriterionController
import com.i13n.ahp.resource.optionsCriterion.OptionsCriterionResource
import org.junit.jupiter.api.Test
import org.mybatis.spring.boot.test.autoconfigure.AutoConfigureMybatis
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.autoconfigure.EnableAutoConfiguration
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.MvcResult

import static org.apache.http.HttpStatus.*
import static org.junit.jupiter.api.Assertions.*
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc()
@AutoConfigureMybatis
@EnableAutoConfiguration
@ActiveProfiles(['integration'])
class AnalyzeIntegrationTest {
    @Autowired
    MockMvc mockMvc

    @Autowired
    AnalysisMapper analysisMapper

    @Autowired
    CriteriaMapper criteriaMapper

    @Autowired
    AHPOptionsMapper optionsMapper

    @Autowired
    OptionsCriterionMapper optionsCriterionMapper

    @Autowired
    ObjectMapper objectMapper

    @Test
    void testSimpleAnalysis() {
        // Define Resource for input into analysis
        AnalysisResource analysisResource = new AnalysisResource()

        CriteriaResource criteriaResourceA = new CriteriaResource()
        criteriaResourceA.setName("cost")
        criteriaResourceA.setRank(2)

        CriteriaResource criteriaResourceB = new CriteriaResource()
        criteriaResourceB.setName("speed")
        criteriaResourceB.setRank(1)

        OptionsResource optionsResourceA = new OptionsResource()
        optionsResourceA.setName("ACME")

        OptionsResource optionsResourceB = new OptionsResource()
        optionsResourceB.setName("WYSYWIGS")

        // Insert the analysis object
        MvcResult analysisMvcResult = mockMvc.perform(post('/analysis')
                .contentType(AnalysisController.ANALYSIS_TYPE)
                .content(objectMapper.writeValueAsString(analysisResource))
                .accept(AnalysisController.ANALYSIS_TYPE)
            ).andReturn()

        // Verify the data was inserted properly
        assertEquals(SC_CREATED, analysisMvcResult.response.status)

        // Verify and collect the analysisId
        String analysisJsonResult = analysisMvcResult.response.getContentAsString()
        AnalysisResource observedAnalysisResource = objectMapper.readValue(analysisJsonResult, AnalysisResource)

        assertNotNull(observedAnalysisResource.getId())
        UUID analysisId = observedAnalysisResource.getId()

        // Create the Criteria using the analysisId
        criteriaResourceA.setAnalysisId(analysisId)
        criteriaResourceB.setAnalysisId(analysisId)

        MvcResult criteriaAMvcResult = mockMvc.perform(post('/criteria')
                .contentType(CriteriaController.CRITERIA_TYPE)
                .content(objectMapper.writeValueAsString(criteriaResourceA))
                .accept(CriteriaController.CRITERIA_TYPE)
            ).andReturn()
        MvcResult criteriaBMvcResult = mockMvc.perform(post('/criteria')
                .contentType(CriteriaController.CRITERIA_TYPE)
                .content(objectMapper.writeValueAsString(criteriaResourceB))
                .accept(CriteriaController.CRITERIA_TYPE)
        ).andReturn()

        assertEquals(SC_CREATED, criteriaAMvcResult.response.status)
        assertEquals(SC_CREATED, criteriaBMvcResult.response.status)

        // Verify and collect the criteriaId
        CriteriaResource observedCriteriaResourceA = objectMapper.readValue(
                criteriaAMvcResult.response.getContentAsString(),
                CriteriaResource
        )
        CriteriaResource observedCriteriaResourceB = objectMapper.readValue(
                criteriaBMvcResult.response.getContentAsString(),
                CriteriaResource
        )
        
        assertNotNull(observedCriteriaResourceA.getId())
        assertNotNull(observedCriteriaResourceB.getId())
        assertEquals(criteriaResourceA.getName(), observedCriteriaResourceA.getName())
        assertEquals(criteriaResourceB.getName(), observedCriteriaResourceB.getName())
        assertEquals(criteriaResourceA.getRank(), observedCriteriaResourceA.getRank())
        assertEquals(criteriaResourceB.getRank(), observedCriteriaResourceB.getRank())

        UUID criteriaAId = observedCriteriaResourceA.getId()
        UUID criteriaBId = observedCriteriaResourceB.getId()
        
        // Create the Options using the analysisId
        optionsResourceA.setAnalysisId(analysisId)
        optionsResourceB.setAnalysisId(analysisId)

        MvcResult optionsAMvcResult = mockMvc.perform(post('/options')
                .contentType(OptionsController.OPTIONS_TYPE)
                .content(objectMapper.writeValueAsString(optionsResourceA))
                .accept(OptionsController.OPTIONS_TYPE)
        ).andReturn()
        MvcResult optionsBMvcResult = mockMvc.perform(post('/options')
                .contentType(OptionsController.OPTIONS_TYPE)
                .content(objectMapper.writeValueAsString(optionsResourceB))
                .accept(OptionsController.OPTIONS_TYPE)
        ).andReturn()

        assertEquals(SC_CREATED, optionsAMvcResult.response.status)
        assertEquals(SC_CREATED, optionsBMvcResult.response.status)

        // Verify and collect the criteriaId
        OptionsResource observedOptionsResourceA = objectMapper.readValue(
                optionsAMvcResult.response.getContentAsString(),
                OptionsResource
        )
        OptionsResource observedOptionsResourceB = objectMapper.readValue(
                optionsBMvcResult.response.getContentAsString(),
                OptionsResource
        )

        assertNotNull(observedOptionsResourceA.getId())
        assertNotNull(observedOptionsResourceB.getId())
        assertEquals(optionsResourceA.getName(), observedOptionsResourceA.getName())
        assertEquals(optionsResourceB.getName(), observedOptionsResourceB.getName())
        assertEquals(optionsResourceA.getRank(), observedOptionsResourceA.getRank())
        assertEquals(optionsResourceB.getRank(), observedOptionsResourceB.getRank())

        // Create the OptionsCriterion and Rankings
        List<OptionsCriterionResource> optionsCriterionResourceList = []
        Map optionsCriterionRanking = [
                "${criteriaAId.toString()}": [
                        observedOptionsResourceB,
                        observedOptionsResourceA
                ],
                "${criteriaBId.toString()}": [
                        observedOptionsResourceA,
                        observedOptionsResourceB
                ]
        ]

        optionsCriterionRanking.each { entry ->
            UUID criterionId = UUID.fromString(entry.key)
            List<OptionsResource> rankedOptions = entry.value

            for (Integer rank = 1; rank <= rankedOptions.size(); rank++) {
                UUID optionId = rankedOptions.get(rank - 1).getId()
                OptionsCriterionResource optionsCriterionResource = new OptionsCriterionResource()

                optionsCriterionResource.setAnalysisId(analysisId)
                optionsCriterionResource.setCriterionId(criterionId)
                optionsCriterionResource.setOptionsId(optionId)
                optionsCriterionResource.setRank(rank)

                optionsCriterionResourceList.push(optionsCriterionResource)
            }
        }

        optionsCriterionResourceList.each {optionsCriterionResource ->
            MvcResult optionsCriterionMvcResult = mockMvc.perform(post('/options_criterion')
                    .contentType(OptionsCriterionController.OPTIONS_CRITERION_TYPE)
                    .content(objectMapper.writeValueAsString(optionsCriterionResource))
                    .accept(OptionsCriterionController.OPTIONS_CRITERION_TYPE)
            ).andReturn()

            assertEquals(SC_CREATED, optionsCriterionMvcResult.response.status)

            OptionsCriterionResource observedOptionsCriterionResource = objectMapper.readValue(
                    optionsCriterionMvcResult.response.getContentAsString(),
                    OptionsCriterionResource
            )

            assertNotNull(observedOptionsCriterionResource.getId())
            assertEquals(optionsCriterionResource.getAnalysisId(), observedOptionsCriterionResource.getAnalysisId())
            assertEquals(optionsCriterionResource.getOptionsId(), observedOptionsCriterionResource.getOptionsId())
            assertEquals(optionsCriterionResource.getCriterionId(), observedOptionsCriterionResource.getCriterionId())
            assertEquals(optionsCriterionResource.getRank(), observedOptionsCriterionResource.getRank())
            assertEquals(0.0f, observedOptionsCriterionResource.getScore())
        }
    }
}
