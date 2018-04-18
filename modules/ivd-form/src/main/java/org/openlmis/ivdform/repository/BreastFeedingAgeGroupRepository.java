package org.openlmis.ivdform.repository;

import org.openlmis.ivdform.domain.BreastFeedingAgeGroup;
import org.openlmis.ivdform.repository.mapper.BreastFeedingAgeGroupMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class BreastFeedingAgeGroupRepository {

    @Autowired
    BreastFeedingAgeGroupMapper mapper;

    public List<BreastFeedingAgeGroup>getAll(){
        return mapper.getAll();
    }

}
