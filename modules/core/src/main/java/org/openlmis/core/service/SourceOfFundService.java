package org.openlmis.core.service;

import org.openlmis.core.dto.SourceOfFundDTO;
import org.openlmis.core.repository.SourceOfFundRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SourceOfFundService {

    @Autowired
    SourceOfFundRepository repository;

    public SourceOfFundDTO getExisting(SourceOfFundDTO fundDTO) {
        return repository.getByCode(fundDTO.getCode());
    }

    public void save(SourceOfFundDTO fundDTO) {
        if (fundDTO.getId() != null) {
            repository.update(fundDTO);
            return;
        }
        repository.insert(fundDTO);
    }

    public void saveSDPFund(SourceOfFundDTO fundDTO, Long userId){

        SourceOfFundDTO fundDTO1 = repository.getByCode(fundDTO.getCode());
        if(fundDTO1 == null){
            repository.insert(fundDTO);
        }else {
            fundDTO.setId(fundDTO1.getId());
            repository.update(fundDTO);
        }
    }



}
