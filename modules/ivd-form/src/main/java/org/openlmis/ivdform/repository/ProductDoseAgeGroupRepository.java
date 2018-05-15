package org.openlmis.ivdform.repository;

import org.openlmis.ivdform.domain.VaccineDose;
import org.openlmis.ivdform.domain.VaccineProductDoseAgeGroup;
import org.openlmis.ivdform.repository.mapper.ProductDoseAgeGroupMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ProductDoseAgeGroupRepository {
    @Autowired
    private ProductDoseAgeGroupMapper mapper;

    public void insert(VaccineProductDoseAgeGroup dose) {
        mapper.insert(dose);
    }

    public void update(VaccineProductDoseAgeGroup dose) {
        mapper.update(dose);
    }

    public List<VaccineProductDoseAgeGroup> getDosesForProduct(Long programId, Long productId) {
        return mapper.getDoseSettingByProductForAgeGroup(programId, productId);
    }

    public List<VaccineDose> getAllDoses() {
        return mapper.getAllDoses();
    }

    public List<VaccineProductDoseAgeGroup> getProgramProductDoses(Long programId) {
        return mapper.getProgramProductDosesAgeGroups(programId);
    }


    public void deleteAllByProgram(Long programId) {
        mapper.deleteAllByProgram(programId);
    }
}
