package org.openlmis.vaccine.repository.mapper.warehouse;

import org.apache.ibatis.annotations.*;
import org.openlmis.vaccine.domain.wms.Transfer;
import org.openlmis.vaccine.dto.AdjustmentReasonExDTO;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TransferMapper {

    @Insert("INSERT INTO public.wms_transfers(\n" +
            "           fromWarehouseId, toWarehouseId, fromBin, toBin, productId, \n" +
            "            transferDate, reason, lotId, quantity,notify, createdBy, \n" +
            "            createdDate, modifiedBy, modifiedDate)\n" +
            "    VALUES ( #{fromWarehouseId}, #{toWarehouseId}, #{fromBin}, #{toBin}, #{productId}, \n" +
            "            NOW(), #{reason}, #{lotId}, #{quantity}, #{notify}, #{createdBy}, NOW(), \n" +
            "            #{modifiedBy}, now());")
    @Options(useGeneratedKeys = true)
    Integer insert(Transfer transfer);

    @Update("UPDATE public.wms_transfers\n" +
            "   SET fromWarehouseId=#{fromWarehouseId}, toWarehouseId=#{toWarehouseId}, fromBin=#{fromBin}, toBin=#{toBin}, \n" +
            "       productId=#{productId}, transferDate=#{transferDate}, reason=#{reason}, lotId=#{lotId}, quantity=#{quantity}, notify=true, \n" +
            "        modifiedBy=#{modifiedBy}, modifiedDate=#{modifiedDate}\n" +
            " WHERE id = #{id};\n")
    void update(Transfer transfer);


    @Insert("INSERT INTO public.wms_reasons(\n" +
            "             code, name)\n" +
            "    VALUES (#{code}, #{name}); ")
    @Options(useGeneratedKeys = true)
    Integer insertReasons(AdjustmentReasonExDTO reason);

    @Update("UPDATE wms_reasons SET code = #{code}, name=#{name} WHERE id =#{id}")
    void updateReason(AdjustmentReasonExDTO reason);

    @Select(" SELECT * FROM wms_reasons WHERE lower(code) = lower(#{code})")
    AdjustmentReasonExDTO getReasonByCode(@Param("code") String code);

    @Select(" SELECT * FROM wms_reasons ")
    List<AdjustmentReasonExDTO> getTransferReasons();
}
