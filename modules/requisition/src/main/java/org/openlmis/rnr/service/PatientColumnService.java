package org.openlmis.rnr.service;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.openlmis.rnr.domain.PatientTemplate;
import org.openlmis.rnr.repository.PatientColumnRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@NoArgsConstructor
@AllArgsConstructor
public class PatientColumnService {


    @Autowired
    PatientColumnRepository repository;

    public PatientTemplate getPatientTemplateByProgramId(Long programId) {
        return new PatientTemplate(programId, repository.getPatientColumnsByProgramId(programId));
    }
}
