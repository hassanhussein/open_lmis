package org.openlmis.ivdform.repository;

import org.openlmis.ivdform.domain.ChildVisitAgeGroup;
import org.openlmis.ivdform.repository.mapper.ChildVisitAgeGroupMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;


@Component
public class ChildVisitAgeGroupRepository {
    @Autowired
    ChildVisitAgeGroupMapper mapper;

    public List<ChildVisitAgeGroup> getAll() {
        return mapper.getAll();
    }

}
