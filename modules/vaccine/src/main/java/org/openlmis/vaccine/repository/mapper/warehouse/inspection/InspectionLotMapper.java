package org.openlmis.vaccine.repository.mapper.warehouse.inspection;

import org.apache.ibatis.annotations.*;
import org.openlmis.vaccine.domain.wms.InspectionLot;
import org.openlmis.vaccine.domain.wms.VVMLots;
import org.openlmis.vaccine.dto.LocationDTO;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface InspectionLotMapper {

    @Select("SELECT *,failquantity as failedQuantity,failreason as failedReason FROM inspection_lots WHERE inspectionLineItemId = #{lineItemId} ")
    @Results(value = {
            @Result(column = "id", property = "id"),
            @Result(column = "passLocationId", property = "passLocationId"),
            @Result(property = "inspectionLineItemId", column = "inspectionLineItemId"),
            @Result(property = "location", column = "passLocationId", javaType = LocationDTO.class,
                    one = @One(select = "org.openlmis.vaccine.repository.mapper.warehouse.location.WmsLocationMapper.getByLocationId")),
            @Result(property = "problems", javaType = List.class, column = "id",
                    many = @Many(select = "org.openlmis.vaccine.repository.mapper.warehouse.inspection.InspectionLotProblemMapper.getByLot"))
    })
    List<InspectionLot> getByLineItemId(@Param("lineItemId") Long lineItemId);

    @Update(" UPDATE public.inspection_lots\n" +
            "   SET  inspectionLineItemId=#{inspectionLineItemId}, lotNumber=#{lotNumber}, countedQuantity=#{countedQuantity}, \n" +
            "       passQuantity=#{passQuantity}, passLocationId=#{passLocationId}, failQuantity=#{failedQuantity}, failReason=#{failedReason}, \n" +
            "       failLocationId=#{failLocationId}, vvmStatus=#{vvmStatus}, modifiedBy=#{modifiedBy}, \n" +
            "       modifiedDate=NOW(), expiryDate=#{expiryDate}, receivedQuantity=#{receivedQuantity}\n" +
            " WHERE id = #{id} ")
    void update(InspectionLot lot);

    @Insert("INSERT INTO public.inspection_lots(\n" +
            "           inspectionLineItemId, lotNumber, passQuantity, passLocationId,vvmStatus,expiryDate, \n" +
            "             createdDate,\n" +
            "             modifiedBy, modifiedDate)\n" +
            "    VALUES ( #{inspectionLineItemId}, #{lotNumber},#{quantity}, #{passLocationId},#{vvmId},#{expiryDate}, \n" +
            "           now(),  \n" +
            "            #{modifiedBy}, now()) on conflict(inspectionLineItemId,lotNumber,passLocationId,vvmStatus) DO UPDATE \n" +
            "SET passQuantity=#{quantity} ;")
    @Options(useGeneratedKeys = true)
    void updateOrSave(VVMLots lots);

}
