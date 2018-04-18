package org.openlmis.ivdform.repository.reports;

import org.openlmis.ivdform.domain.reports.LLINLineItem;
import org.openlmis.ivdform.repository.mapper.reports.LLINMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class LLINRepository {
    @Autowired
    LLINMapper mapper;

    public void  insert(LLINLineItem lineItem){
        mapper.insert(lineItem);
    }

    public void update(LLINLineItem lineItem){
        mapper.update(lineItem);
    }

}
