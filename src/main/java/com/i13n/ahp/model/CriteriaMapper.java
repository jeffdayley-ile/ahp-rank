package com.i13n.ahp.model;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.UUID;

@Mapper
@Component
public interface CriteriaMapper {
    List<Criteria> findAll();

    Criteria findById(UUID uuid);

    int insert(@Param("criteria") Criteria criteria);
}
