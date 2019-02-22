package org.openlmis.core.repository;

import org.openlmis.core.dto.RejectionReasonDTO;
import org.openlmis.core.repository.mapper.RejectionReasonDTOMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class RejectionReasonDTORepository {
    @Autowired
    private RejectionReasonDTOMapper mapper;


    public List<RejectionReasonDTO> getAll(){
        return mapper.getAllRejections();
    }

    public Integer insert(RejectionReasonDTO rejectionReasonDTO){
        return mapper.insertUploaded(rejectionReasonDTO);
    }

    public void update(RejectionReasonDTO rejectionReasonDTO){
        mapper.update(rejectionReasonDTO);
    }

    public RejectionReasonDTO getByCode(String code) {
         return mapper.getByCode(code);
    }
}
