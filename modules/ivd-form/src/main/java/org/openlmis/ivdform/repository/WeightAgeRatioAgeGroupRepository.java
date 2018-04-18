package org.openlmis.ivdform.repository;

import org.openlmis.ivdform.domain.WeightAgeRatioAgeGroup;
import org.openlmis.ivdform.repository.mapper.WeightAgeRatioAgeGroupMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class WeightAgeRatioAgeGroupRepository {

    @Autowired
    WeightAgeRatioAgeGroupMapper mapper;

    public List<WeightAgeRatioAgeGroup>getAll(){
        return mapper.getAll();
    }

}
