package org.openlmis.core.repository.mapper;

import org.apache.ibatis.annotations.*;
import org.openlmis.core.dto.RejectionReasonDTO;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RejectionReasonDTOMapper {

    @Select("select * from rejections ")
    List<RejectionReasonDTO> getAllRejections();

    @Insert("INSERT INTO public.rejections(\n" +
            "            name, code, createdBy, createdDate, modifiedBy, modifiedDate,rejectionCategoryId)\n" +
            "    VALUES (  #{name}, #{code}, #{createdBy},NOW(), #{modifiedBy}, NOW(),#{rejectionCategory.id});")
    @Options(useGeneratedKeys = true)
    int insertUploaded(RejectionReasonDTO rejectionReason);

    @Update("update rejections set name = #{name}, code = #{code} , rejectionCategoryId = #{rejectionCategory.id} where id = #{id} ")
    void update(RejectionReasonDTO rejectionReasonDTO);

    @Select(" select * from rejections where code = #{code} ")
    RejectionReasonDTO getByCode(@Param("code") String code);

    @Select(" select * from rejections where id = #{id} ")
    RejectionReasonDTO getById(@Param("id") Long id);

    @Select(" select * from rejections where rejectionCategoryId = #{rejectionCategoryId}")
    List<RejectionReasonDTO> getByRejectionCategory(@Param("rejectionCategoryId") Long rejectionCategoryId);

}
