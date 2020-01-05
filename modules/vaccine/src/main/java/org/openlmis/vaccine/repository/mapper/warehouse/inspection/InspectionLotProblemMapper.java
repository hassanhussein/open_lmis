package org.openlmis.vaccine.repository.mapper.warehouse.inspection;

import org.apache.ibatis.annotations.*;
import org.openlmis.vaccine.domain.wms.InspectionLotProblem;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface InspectionLotProblemMapper {

    @Insert("INSERT INTO public.inspection_lot_problems(\n" +
            "            inspectionLotId, boxNumber, lotNumber, isAlarma, isAlarmb, \n" +
            "            isalarmc, isalarmd, iscca, isccb, isccc, isccd, createdBy, createdDate, \n" +
            "            modifiedBy, modifieddate)\n" +
            "    VALUES ( #{inspectionLotId}, #{boxNumber}, #{lotNumber}, #{isAlarma}, #{isAlarmb}, \n" +
            "            #{isalarmc}, #{isalarmd}, #{iscca}, #{isccb}, #{isccc},#{isccd}, #{createdBy}, NOW(), #{modifiedBy}, \n" +
            "            NOW());\n")
    @Options(useGeneratedKeys = true)
    Integer insert(InspectionLotProblem problem);

    @Select(" SELECT * FROM inspection_lot_problems where inspectionLotId = #{lotId}")
    List<InspectionLotProblem>getByLot(@Param("lotId") Long lotId);

    @Update(" UPDATE public.inspection_lot_problems\n" +
            "   SET inspectionLotId=#{inspectionLotId}, boxNumber=#{boxNumber}, lotNumber=#{lotNumber}, isalarma=#{isalarma}, \n" +
            "       isalarmb=#{isalarmb}, isalarmc=#{isalarmc}, isalarmd=#{isalarmd}, iscca=#{iscca}, isccb=#{isccb}, isccc=#{isccc}, \n" +
            "       isccd=#{isccd},  modifiedBy=#{modifiedBy}, modifieddate=NOW()\n" +
            " WHERE id = #{id};")
    void update(InspectionLotProblem problem);

}
