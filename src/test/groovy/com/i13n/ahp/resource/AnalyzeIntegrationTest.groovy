package com.i13n.ahp.resource

import com.fasterxml.jackson.databind.ObjectMapper
import com.i13n.ahp.model.AHPOptionsMapper
import com.i13n.ahp.model.AnalysisMapper
import com.i13n.ahp.model.CriteriaMapper
import com.i13n.ahp.resource.analysis.AnalysisController
import com.i13n.ahp.resource.analysis.AnalysisResource
import com.i13n.ahp.resource.criteria.CriteriaController
import com.i13n.ahp.resource.criteria.CriteriaResource
import com.i13n.ahp.resource.options.OptionsController
import com.i13n.ahp.resource.options.OptionsResource
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
        String analysisJsonResult = analysisMvcResult.response.getContentAsString();
        AnalysisResource observedAnalysisResource = objectMapper.readValue(analysisJsonResult, AnalysisResource);

        assertNotNull(observedAnalysisResource.getId())
        UUID analysisId = observedAnalysisResource.getId();

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
        

    }
}
