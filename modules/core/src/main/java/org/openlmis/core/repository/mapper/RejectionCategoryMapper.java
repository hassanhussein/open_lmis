package org.openlmis.core.repository.mapper;

import org.apache.ibatis.annotations.*;
import org.openlmis.core.dto.RejectionCategoryDTO;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RejectionCategoryMapper {

    @Insert(" INSERT INTO public.rejection_categories(\n" +
            "             name, code, createdBy, createdDate, modifiedBy, modifiedDate)\n" +
            "    VALUES (#{name}, #{code}, #{createdBy}, NOW(), #{modifiedBy}, NOW()) ")
    @Options(useGeneratedKeys = true)
    Integer insert(RejectionCategoryDTO category);

    @Update(" UPDATE public.rejection_categories\n" +
            "   SET  name=#{name}, code=#{code}, modifiedBy=#{modifiedBy}, \n" +
            "       modifiedDate= NOW()\n" +
            " WHERE id = #{id} ")
    void update(RejectionCategoryDTO category);

    @Select(" select * from rejection_categories ")
    List<RejectionCategoryDTO> getAll();

    @Select(" select * from rejection_categories where lower(code) = lower(#{code}) ")
    RejectionCategoryDTO getByCode(@Param("code") String code);

    @Select(" select * from rejection_categories where id = #{id}")
    RejectionCategoryDTO getById(@Param("id") Long id);

}
