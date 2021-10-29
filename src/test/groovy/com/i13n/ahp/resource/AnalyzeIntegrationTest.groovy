package com.i13n.ahp.resource

import com.fasterxml.jackson.databind.ObjectMapper
import com.i13n.ahp.model.AHPOptionsMapper
import com.i13n.ahp.model.AnalysisMapper
import com.i13n.ahp.model.CriteriaMapper
import com.i13n.ahp.model.OptionsCriterionMapper
import com.i13n.ahp.resource.analysis.AnalysisInputResource
import com.i13n.ahp.resource.analysis.AnalysisController
import com.i13n.ahp.resource.analysis.AnalysisOutputResource
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
    void testAutomaticAnalysis() {
        // Define Input
        AnalysisInputResource inputResource = new AnalysisInputResource();
        List<String> criteria = ["cost", "speed"]
        List<String> options = ["aws", "azure"]
        Map<String, List<String>> optionsCriterion = [
                "cost": ["aws", "azure"],
                "speed": ["azure", "aws"]
        ]

        inputResource.setCriteria(criteria)
        inputResource.setOptions(options)
        inputResource.setOptionsCriterion(optionsCriterion)

        // Post to endpoint
        MvcResult inputMvcResult = mockMvc.perform(post('/analysis')
                .contentType(AnalysisController.ANALYSIS_TYPE)
                .content(objectMapper.writeValueAsString(inputResource))
                .accept(AnalysisController.ANALYSIS_RETURN_TYPE)
        ).andReturn()

        assertEquals(SC_CREATED, inputMvcResult.response.status)

        String inputJsonResult = inputMvcResult.response.getContentAsString()
        AnalysisOutputResource inputReturnResource = objectMapper.readValue(inputJsonResult, AnalysisOutputResource)

        assertNotNull(inputReturnResource.getAnalysisId())
        assertEquals(inputResource.getCriteria().size(), inputReturnResource.getCriteria().size())
        assertEquals(inputResource.getOptions().size(), inputReturnResource.getOptions().size())
        assertEquals(inputResource.getOptions().size() * inputResource.getCriteria().size(),
            inputReturnResource.getOptionsCriterion().size())

    }

}
