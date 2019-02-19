package org.openlmis.core.repository.mapper;

import org.apache.ibatis.annotations.*;
import org.openlmis.core.dto.RejectionReasonDTO;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RejectionReasonDTOMapper {

    @Select("select * from rejections ")
    List<RejectionReasonDTO> getAllRejections();

    @Insert("INSERT INTO public.rejection_reasons(\n" +
            "            rnrid, name, code, createdBy, createdDate, modifiedBy, modifiedDate)\n" +
            "    VALUES ( #{rnrId}, #{name}, #{code}, #{createdBy},NOW(), #{modifiedBy}, NOW());")
    @Options(useGeneratedKeys = true)
    int insertUploaded(RejectionReasonDTO rejectionReason);

    @Update("update rejection_reasons set name = #{name}, code = #{code} where id = #{id}")
    void update(RejectionReasonDTO rejectionReasonDTO);

    @Select(" select * from rejection_reasons where code = #{code} ")
    RejectionReasonDTO getByCode(@Param("code") String code);
}
