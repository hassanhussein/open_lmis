package org.openlmis.ivdform.repository.reports;

import org.openlmis.ivdform.domain.reports.TtStatusLineItem;
import org.openlmis.ivdform.repository.mapper.reports.TtStatusLineItemMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class TtStatusLineItemRepository {
    @Autowired
    TtStatusLineItemMapper mapper;

    public void insert(TtStatusLineItem lineItem){
        mapper.insert(lineItem);
    }

    public void update(TtStatusLineItem lineItem){
        mapper.update(lineItem);
    }
}
