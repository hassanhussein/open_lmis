package org.openlmis.ivdform.repository.reports;

import org.openlmis.ivdform.domain.reports.CTCLineItem;
import org.openlmis.ivdform.repository.mapper.reports.CTCLineItemMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class CTCLineItemRepository {

    @Autowired
    CTCLineItemMapper mapper;

    public void  insert(CTCLineItem lineItem){
        mapper.insert(lineItem);
    }

    public void update(CTCLineItem lineItem){
        mapper.update(lineItem);
    }

}
