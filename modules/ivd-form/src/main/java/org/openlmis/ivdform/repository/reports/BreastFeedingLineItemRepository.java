package org.openlmis.ivdform.repository.reports;

import org.openlmis.ivdform.domain.reports.BreastFeedingLineItem;
import org.openlmis.ivdform.repository.mapper.reports.BreastFeedingLineItemMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class BreastFeedingLineItemRepository {

    @Autowired
    BreastFeedingLineItemMapper mapper;

    public void insert(BreastFeedingLineItem lineItem) {
        mapper.insert(lineItem);
    }

    public void update(BreastFeedingLineItem lineItem) {
        mapper.update(lineItem);
    }


}
