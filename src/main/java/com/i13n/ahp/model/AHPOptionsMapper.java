package com.i13n.ahp.model;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.UUID;

@Mapper
@Component
// Prefixed with AHP to ovoid confusion with other OptionMappers Interfaces and classes in ibatis
public interface AHPOptionsMapper {
    List<Options> findAll();

    Options findById(UUID uuid);

    int insert(@Param("options") Options options);
}
