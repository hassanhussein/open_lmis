package org.openlmis.rnr.search.strategy;

import org.openlmis.core.domain.Facility;
import org.openlmis.core.domain.ProcessingPeriod;
import org.openlmis.core.domain.Program;
import org.openlmis.core.service.FacilityService;
import org.openlmis.core.service.ProcessingScheduleService;
import org.openlmis.core.service.ProgramService;
import org.openlmis.rnr.domain.Rnr;
import org.openlmis.rnr.repository.RequisitionRepository;
import org.openlmis.rnr.search.criteria.RequisitionSearchCriteria;
import org.openlmis.rnr.service.RequisitionPermissionService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import static org.openlmis.core.domain.RightName.VIEW_REQUISITION;

/**
 * This class is a strategy to search for emergency requisitions only based on date range.
 */
public class EmergencyOnlyRequisitionSearch extends RequisitionSearchStrategy {

    ProgramService programService;
    FacilityService facilityService;
    RequisitionSearchCriteria criteria;
    RequisitionRepository requisitionRepository;
    ProcessingScheduleService processingScheduleService;

    public EmergencyOnlyRequisitionSearch(RequisitionSearchCriteria criteria,
                                          ProcessingScheduleService processingScheduleService,
                                          RequisitionRepository requisitionRepository,
                                          ProgramService programService,
                                          FacilityService facilityService) {
        this.criteria = criteria;
        this.processingScheduleService = processingScheduleService;
        this.requisitionRepository = requisitionRepository;
        this.programService = programService;
        this.facilityService = facilityService;
    }

    @Override
    List<Rnr> findRequisitions() {
        List<Facility> facilities = new ArrayList<>();

        Long userId = criteria.getUserId();

        List<Program> programs = programService.getProgramsForUserByRights(userId, VIEW_REQUISITION);

        for(Program p : programs) {
            facilities.addAll(facilityService.getUserSupervisedFacilities(userId, p.getId(), VIEW_REQUISITION));
        }

        facilities.add(facilityService.getHomeFacility(userId));
        facilities.remove(null);

        List<ProcessingPeriod> periods = processingScheduleService.getPeriodsForDateRange(
                criteria.getRangeStart(), criteria.getRangeEnd());

        return requisitionRepository.getPostSubmitEmergencyOnlyRequisitions((List<Facility>)facilities, programs,
                periods);
    }
}
