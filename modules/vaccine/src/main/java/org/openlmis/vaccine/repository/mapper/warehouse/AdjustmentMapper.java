package org.openlmis.vaccine.repository.mapper.warehouse;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Options;
import org.openlmis.vaccine.domain.wms.Adjustment;
import org.openlmis.vaccine.domain.wms.Transfer;
import org.springframework.stereotype.Repository;

@Repository
public interface AdjustmentMapper {

    @Insert("INSERT INTO public.lot_location_entries(\n" +
            "           lotid, type, quantity, locationid,vvmid,stockCardId,transferlogs, \n" +
            "            createdBy, \n" +
            "            createdDate, modifiedBy, modifiedDate)\n" +
            "    VALUES ( #{lotId}, #{type},#{quantity}, #{locationid},#{vvmId},#{stockCardId},#{transferLogs}, \n" +
            "             #{createdBy}, NOW(), \n" +
            "            #{modifiedBy}, now());")
    @Options(useGeneratedKeys = true)
    Integer insert(Adjustment adjustment);
}
