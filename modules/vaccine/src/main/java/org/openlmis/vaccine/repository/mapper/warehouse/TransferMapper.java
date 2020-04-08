package org.openlmis.vaccine.repository.mapper.warehouse;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Update;
import org.openlmis.vaccine.domain.wms.Transfer;
import org.springframework.stereotype.Repository;

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


}
