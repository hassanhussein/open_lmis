package org.openlmis.vaccine.repository;

import lombok.NoArgsConstructor;
import org.openlmis.vaccine.dto.StockRequirements_;
import org.openlmis.vaccine.dto.StockRequirements;
import org.openlmis.vaccine.repository.mapper.StockRequirementsMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@NoArgsConstructor
public class StockRequirementsRepository {

    @Autowired
    StockRequirementsMapper mapper;


    public List<StockRequirements_> getAll(Long programId, Long facilityId, int year) {
        return mapper.getAllByProgramAndFacility(programId, facilityId,year);
    }

    public StockRequirements getById(Long id) {
        return mapper.getById(id);
    }
    public StockRequirements_ getByProductId(Long programId, Long facilityId,Long productId, int year) {
        return mapper.getByProductId(programId,facilityId,productId,year);
    }


    public Integer update(StockRequirements_ stockRequirements) {
        return mapper.update(stockRequirements);
    }

    public Integer save(StockRequirements stockRequirements) {
        return mapper.save(stockRequirements);
    }

    public Integer deleteFacilityStockRequirements(Long programId, Long facilityId,Integer year){
        return mapper.deleteFacilityStockRequirements(programId,facilityId,year);
    }

}