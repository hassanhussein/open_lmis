package org.openlmis.ivdform.repository;

import org.openlmis.ivdform.domain.TtStatusCategory;
import org.openlmis.ivdform.repository.mapper.TtStatusCategoryMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class TtStatusCategoryRepository {

    @Autowired
    TtStatusCategoryMapper mapper;

    public List<TtStatusCategory> getAll() {
        return mapper.getAll();
    }
}
