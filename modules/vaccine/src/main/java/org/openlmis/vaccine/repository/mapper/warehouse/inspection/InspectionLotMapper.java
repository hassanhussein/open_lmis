package org.openlmis.vaccine.repository.mapper.warehouse.inspection;

import org.apache.ibatis.annotations.*;
import org.openlmis.vaccine.domain.wms.InspectionLot;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface InspectionLotMapper {

    @Select(" SELECT * FROM inspection_lots WHERE inspectionLineItemId = #{lineItemId} ")
    @Results(value = {
            @Result(column = "id", property = "id"),
            @Result(property = "inspectionLineItemId", column = "inspectionLineItemId"),

            @Result(property = "problems", javaType = List.class, column = "id",
                    many = @Many(select = "org.openlmis.vaccine.repository.mapper.warehouse.inspection.InspectionLotProblemMapper.getByLot"))
    })
    List<InspectionLot> getByLineItemId(@Param("lineItemId") Long lineItemId);

    @Update(" UPDATE public.inspection_lots\n" +
            "   SET  inspectionLineItemId=#{inspectionLineItemId}, lotNumber=#{lotNumber}, countedQuantity=#{countedQuantity}, \n" +
            "       passQuantity=#{passQuantity}, passLocationId=#{passLocationId}, failQuantity=#{failQuantity}, failReason=#{failReason}, \n" +
            "       failLocationId=#{failLocationId}, vvmStatus=#{vvmStatus}, modifiedBy=#{modifiedBy}, \n" +
            "       modifiedDate=NOW(), expiryDate=#{expiryDate}, receivedQuantity=#{receivedQuantity}\n" +
            " WHERE id = #{id} ")
    void update(InspectionLot lot);

}
