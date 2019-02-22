package org.openlmis.core.service;

import org.openlmis.core.domain.FacilityOperator;
import org.openlmis.core.domain.FacilityType;
import org.openlmis.core.repository.FacilityTypeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class FacilityTypeService {

    private static final String FACILITY_TYPE_MAPPING = "FACILITY_TYPE_ELMIS_MAPPING";
    private static final String FACILITY_OWNER_MAPPING = "FACILITY_OWNER_ELMIS_MAPPING";
    @Autowired
    private FacilityTypeRepository repository;

    @Autowired
    private ConfigurationSettingService settingService;

    @Autowired
    private ELMISInterfaceService interfaceService;


    public FacilityType getFacilityTypeByCode(String code){
        return repository.getFacilityTypeByCode(code);
    }

    public FacilityType getFacilityTypeByMappedCode(String code) {
         String interFaceKey = settingService.getByKey(FACILITY_TYPE_MAPPING).getValue();
        return repository.getFacilityTypeByMappedCode(code, interfaceService.getByName(interFaceKey).getId());
    }

     public FacilityOperator getFacilityTypeByMappedOwner(String code) {
        String interFaceKey = settingService.getByKey(FACILITY_OWNER_MAPPING).getValue();
        return repository.getFacilityTypeByMappedOwner(code, interfaceService.getByName(interFaceKey).getId());
    }


}
