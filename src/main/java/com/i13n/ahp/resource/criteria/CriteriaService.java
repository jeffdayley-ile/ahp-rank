package com.i13n.ahp.resource.criteria;

import com.i13n.ahp.model.Criteria;
import com.i13n.ahp.model.CriteriaMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class CriteriaService {
    private static CriteriaMapper criteriaMapper;

    public CriteriaService(CriteriaMapper criteriaMapper) {
        this.criteriaMapper = criteriaMapper;
    }

    public static Optional<Criteria> getCriteria(UUID id) {
        return Optional.ofNullable(criteriaMapper.findById(id));
    }

    public static Optional<CriteriaResource> getCriteriaResource(UUID id) {
        return getCriteria(id)
                .map(CriteriaService::criteriaToCriteriaResource);
    }

    public static List<CriteriaResource> getAllCriteriaResources() {
        List<Criteria> analyses = criteriaMapper.findAll();
        List<CriteriaResource> criteriaResources = analyses.stream()
                .map(CriteriaService::criteriaToCriteriaResource)
                .collect(Collectors.toList());
        return criteriaResources;
    }

    public Criteria createCriteria(Criteria criteria) {
        criteria.setId(UUID.randomUUID());
        criteriaMapper.insert(criteria);
        return criteria;
    }

    public CriteriaResource createCriteriaResource(CriteriaResource criteriaResource) {
        Criteria criteria = createCriteria(criteriaResourceToCriteria(criteriaResource));
        criteriaResource.setId(criteria.getId());
        return criteriaResource;
    }

    private static Criteria criteriaResourceToCriteria(CriteriaResource criteriaResource) {
        Criteria criteria = new Criteria();
        criteria.setId(criteriaResource.getId());
        criteria.setAnalysisId(criteriaResource.getAnalysisId());
        criteria.setName(criteriaResource.getName());
        criteria.setRank(criteriaResource.getRank());
        criteria.setScore(criteriaResource.getScore());
        return criteria;
    }

    private static CriteriaResource criteriaToCriteriaResource(Criteria criteria) {
        CriteriaResource criteriaResource = new CriteriaResource();
        criteriaResource.setId(criteria.getId());
        criteriaResource.setAnalysisId(criteria.getAnalysisId());
        criteriaResource.setName(criteria.getName());
        criteriaResource.setRank(criteria.getRank());
        criteriaResource.setScore(criteria.getScore());
        return criteriaResource;
    }

}
