package org.openlmis.ivdform.repository.reports;

import org.openlmis.ivdform.domain.reports.HEIDLineItem;
import org.openlmis.ivdform.repository.mapper.reports.HEIDLineItemMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class HEIDLineItemRepository {
  @Autowired
  HEIDLineItemMapper mapper;

  public void  insert(HEIDLineItem lineItem){
        mapper.insert(lineItem);
    }

    public void update(HEIDLineItem lineItem){
        mapper.update(lineItem);
    }


}
