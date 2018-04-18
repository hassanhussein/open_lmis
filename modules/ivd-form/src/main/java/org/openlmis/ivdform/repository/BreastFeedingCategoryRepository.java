package org.openlmis.ivdform.repository;

import org.openlmis.ivdform.domain.BreastFeedingCategory;
import org.openlmis.ivdform.repository.mapper.BreastFeedingCategoryMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class BreastFeedingCategoryRepository {

    @Autowired
    BreastFeedingCategoryMapper mapper;

    public List<BreastFeedingCategory> getAll() {
        return mapper.getAll();
    }

}
