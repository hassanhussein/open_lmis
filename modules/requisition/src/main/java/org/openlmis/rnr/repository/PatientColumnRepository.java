package org.openlmis.rnr.repository;

import org.openlmis.rnr.domain.PatientColumn;
import org.openlmis.rnr.repository.mapper.PatientColumnMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class PatientColumnRepository {


    @Autowired
    PatientColumnMapper mapper;

    public List<PatientColumn> getPatientColumnsByProgramId(Long programId) {
        return mapper.getAllPatientColumnsByProgramId(programId);
    }
}
