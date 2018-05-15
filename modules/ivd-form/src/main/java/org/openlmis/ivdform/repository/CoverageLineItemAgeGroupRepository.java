package org.openlmis.ivdform.repository;


import org.openlmis.ivdform.domain.CoverageLineItemAgeGroup;
import org.openlmis.ivdform.domain.reports.VaccineCoverageAgeGroupLineItem;
import org.openlmis.ivdform.repository.mapper.CoverageLineItemAgeGroupMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class CoverageLineItemAgeGroupRepository {

    @Autowired
    CoverageLineItemAgeGroupMapper mapper;

    public List<CoverageLineItemAgeGroup> getAll() {
        return mapper.getAll();
    }


    public CoverageLineItemAgeGroup getBy(Long id) {
        return mapper.getBy(id);
    }

    public void insert(VaccineCoverageAgeGroupLineItem item) {
        mapper.insert(item);
    }

    public void update(VaccineCoverageAgeGroupLineItem item) {
        mapper.update(item);
    }

    public VaccineCoverageAgeGroupLineItem getByParams(Long reportId, Long productId, Long doseId) {
        return mapper.getCoverageByReportProductDosage(reportId, productId, doseId);
    }

    public VaccineCoverageAgeGroupLineItem getById(Long id) {
        return mapper.getById(id);
    }
}
