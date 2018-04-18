package org.openlmis.ivdform.repository.reports;

import org.openlmis.ivdform.domain.reports.WeightAgeRatioLineItem;
import org.openlmis.ivdform.repository.mapper.reports.WeightAgeRatioLineItemMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class WeightAgeRatioLineItemRepository {
    @Autowired
    private WeightAgeRatioLineItemMapper mapper;

    public void insert(WeightAgeRatioLineItem lineItem){
        mapper.insert(lineItem);
    }

    public void update(WeightAgeRatioLineItem lineItem){
        mapper.update(lineItem);
    }


}
