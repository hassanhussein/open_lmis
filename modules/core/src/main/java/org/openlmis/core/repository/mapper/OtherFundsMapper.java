package org.openlmis.core.repository.mapper;

import org.apache.ibatis.annotations.*;
import org.openlmis.core.dto.OtherFundsDTO;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OtherFundsMapper {

    @Select(" select  sof.name as name, sof.code as code, bli.allocatedbudget as amount from budget_line_items bli \n" +
            "join budgets b on b.id=bli.budgetid\n" +
            "join source_of_funds sof on sof.code=bli.fundsourcecode\n" +
            "join facilities f on f.id=b.facilityid\n" +
            "where b.facilityid=#{facility} ")
    List<OtherFundsDTO> getFundingSources(@Param("facility") Long facility);



}
