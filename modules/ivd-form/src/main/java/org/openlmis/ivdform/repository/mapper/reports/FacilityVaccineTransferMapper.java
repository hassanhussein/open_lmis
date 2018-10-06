package org.openlmis.ivdform.repository.mapper.reports;

import org.apache.ibatis.annotations.*;
import org.openlmis.ivdform.domain.reports.FacilityVaccineTransfer;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FacilityVaccineTransferMapper {

    @Insert(" INSERT INTO public.facility_vaccine_transfers(\n" +
            "             facilityId, quantity, lineItemId, createdBy, createdDate, \n" +
            "            modifiedBy, modifiedDate)\n" +
            "    VALUES (#{facilityId}, #{quantity}, #{lineItemId}, #{createdBy}, NOW(), \n" +
            "            #{modifiedBy}, NOW()); ")
    @Options(useGeneratedKeys = true)
    Integer insert(FacilityVaccineTransfer transfer);

    @Update("UPDATE public.facility_vaccine_transfers\n" +
            "   SET  facilityId=#{facilityId}, quantity=#{quantity}, lineItemId=#{lineItemId}, createdBy=#{createdBy}, createdDate=NOW(), \n" +
            "       modifiedBy=#{modifiedBy}, modifiedDate=NOW()\n" +
            " WHERE id = #{id}\n")
    void update(FacilityVaccineTransfer transfer);

    @Select("select * from facility_vaccine_transfers where lineItemId = #{lineItemId}")
    List<FacilityVaccineTransfer>getByLineItem(@Param("lineItemId") Long lineItemId);


}
