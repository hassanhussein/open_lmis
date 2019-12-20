package org.openlmis.vaccine.repository.mapper.warehouse.inspection;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.openlmis.vaccine.domain.wms.InspectionLot;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface InspectionLotMapper {

    @Select(" SELECT * FROM inspection_lots WHERE inspectionLineItemId = #{lineItemId} ")
    List<InspectionLot> getByLineItemId(@Param("lineItemId") Long lineItemId);

}
