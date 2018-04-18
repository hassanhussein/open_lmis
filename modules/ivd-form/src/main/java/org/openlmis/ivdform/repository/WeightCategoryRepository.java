package org.openlmis.ivdform.repository;

import org.openlmis.ivdform.domain.WeightCategory;
import org.openlmis.ivdform.repository.mapper.WeightCategoryMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class WeightCategoryRepository {

    @Autowired
    WeightCategoryMapper mapper;


    public List<WeightCategory> getAll(){
        return mapper.getAll();
    }


}
