package org.openlmis.core.repository.mapper;

import org.apache.ibatis.annotations.*;
import org.openlmis.core.dto.SourceOfFundDTO;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SourceOfFundMapper {

    @Insert("INSERT INTO public.source_of_funds(\n" +
            "            name, displayOrder, createddate, code,programId)\n" +
            "    VALUES (#{name},#{displayOrder}, NOW(),#{code}, #{programId});")
    @Options(useGeneratedKeys = true)
    Integer Insert(SourceOfFundDTO dto);

    @Select(" SELECT * FROM source_of_funds where programId = #{program} order by displayOrder")
    List<SourceOfFundDTO> getAll(@Param("program") Long program);

    @Update("update source_of_funds set code=#{code}, name = #{name}, displayOrder = #{displayOrder} " +
            " , programId = #{programId} where id = #{id}")
    void update(SourceOfFundDTO dto);

    @Select(" SELECT * FROM source_of_funds where lower(code) = lower(#{code})")
    SourceOfFundDTO getByCode(@Param("code")String code);


}
