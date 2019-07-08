package org.openlmis.core.repository;

import org.openlmis.core.domain.Patient;
import org.openlmis.core.repository.mapper.PatientMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class PatientRepository {

    @Autowired
    PatientMapper mapper;

    public List<Patient> getByProgram(Long programId) {
        return mapper.getByProgram(programId);
    }

}
