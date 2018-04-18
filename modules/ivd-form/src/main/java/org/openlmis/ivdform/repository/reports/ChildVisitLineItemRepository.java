package org.openlmis.ivdform.repository.reports;

import org.openlmis.ivdform.domain.reports.ChildVisitLineItem;
import org.openlmis.ivdform.repository.mapper.reports.ChildVisitLineItemMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ChildVisitLineItemRepository {

    @Autowired
    ChildVisitLineItemMapper mapper;

    public void insert(ChildVisitLineItem lineItem){
        mapper.insert(lineItem);
    }

    public void update(ChildVisitLineItem lineItem){
        mapper.update(lineItem);
    }

}
