package org.openlmis.ivdform.repository.reports;

import org.openlmis.ivdform.domain.reports.PmtctLineItem;
import org.openlmis.ivdform.repository.mapper.reports.PmctcLineItemMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class PmtctLineItemRepository {

    @Autowired
    PmctcLineItemMapper mapper;

    public void insert(PmtctLineItem lineItem){
        mapper.insert(lineItem);
    }
    public void update(PmtctLineItem lineItem){
        mapper.update(lineItem);
    }

}
