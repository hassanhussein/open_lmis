package org.openlmis.vaccine.repository.mapper.warehouse.inspection;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Select;
import org.openlmis.vaccine.domain.wms.InspectionLot;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface InspectionLotMapper {

    @Select(" SELECT * FROM inspection_lots WHERE inspectionLineItemId = #{lineItemId} ")
    @Result(property = "inspectionLineItemId", column = "inspectionLineItemId")
    List<InspectionLot> getByLineItemId(@Param("lineItemId") Long lineItemId);

}
