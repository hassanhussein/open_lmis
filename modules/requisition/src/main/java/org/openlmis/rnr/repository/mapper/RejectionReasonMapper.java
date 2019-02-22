package org.openlmis.rnr.repository.mapper;

import org.apache.ibatis.annotations.*;
import org.openlmis.rnr.domain.RejectionReason;
import org.openlmis.core.dto.RejectionReasonDTO;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public interface RejectionReasonMapper {

    @Insert("INSERT INTO public.rejection_reasons(\n" +
            "            rnrid, name, code, createdby, createddate, modifiedby, modifieddate)\n" +
            "    VALUES ( #{rnrId}, #{name}, #{code}, #{createdBy},NOW(), #{modifiedBy}, NOW());")
    @Options(useGeneratedKeys = true)
    int insert(RejectionReason rejectionReason);

    @Select("SELECT * FROM rejection_reasons WHERE rnrId = #{rnrId} ORDER BY createdDate")
    @Results(value = {
            @Result(property = "id", column = "id"),
            @Result(property = "author.id", column = "createdBy")
    })
    List<RejectionReason> getByRnrId(Long rnrId);

    @Select("select * from rejections ")
    List<Map<String,Object>>getAllRejections();

    @Insert("INSERT INTO public.rejection_reasons(\n" +
            "            rnrid, name, code, createdBy, createdDate, modifiedBy, modifiedDate)\n" +
            "    VALUES ( #{rnrId}, #{name}, #{code}, #{createdBy},NOW(), #{modifiedBy}, NOW());")
    @Options(useGeneratedKeys = true)
    int insertUploaded(RejectionReasonDTO rejectionReason);

    @Update("update rejection_reasons set name = #{name}, code = #{code} where id = #{id}")
    void update(RejectionReasonDTO rejectionReasonDTO);

}
