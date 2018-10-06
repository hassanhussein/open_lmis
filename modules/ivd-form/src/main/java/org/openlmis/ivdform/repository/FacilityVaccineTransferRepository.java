package org.openlmis.ivdform.repository;

import org.openlmis.ivdform.domain.reports.FacilityVaccineTransfer;
import org.openlmis.ivdform.repository.mapper.reports.FacilityVaccineTransferMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class FacilityVaccineTransferRepository {
@Autowired
private FacilityVaccineTransferMapper mapper;

    public Integer insert(FacilityVaccineTransfer transfer){
        return mapper.insert(transfer);
    }

    public void update(FacilityVaccineTransfer transfer){
        mapper.update(transfer);
    }

    public List<FacilityVaccineTransfer> getByLineItem(Long lineItemId){
        return mapper.getByLineItem(lineItemId);
    }


}
