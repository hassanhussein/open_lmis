package org.openlmis.core.repository;

import org.apache.ibatis.annotations.Param;
import org.openlmis.core.dto.OtherFundsDTO;
import org.openlmis.core.repository.mapper.OtherFundsMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class OtherFundsRespository {

    @Autowired
    private OtherFundsMapper mapper;

    public List<OtherFundsDTO> getFundingSources(Long facilityId) {
        return mapper.getFundingSources(facilityId);
    }

}
