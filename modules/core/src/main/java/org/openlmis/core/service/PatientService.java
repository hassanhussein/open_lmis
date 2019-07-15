package org.openlmis.core.service;

import org.openlmis.core.domain.Patient;
import org.openlmis.core.repository.PatientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PatientService {

    @Autowired
    PatientRepository repository;


    public  List<Patient> getByProgram(Long programId)
    {
        return repository.getByProgram(programId);
    }

}
