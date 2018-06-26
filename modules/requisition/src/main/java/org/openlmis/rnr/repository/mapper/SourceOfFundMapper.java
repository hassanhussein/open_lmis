package org.openlmis.rnr.repository.mapper;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.openlmis.rnr.dto.SourceOfFundDTO;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SourceOfFundMapper {

    @Insert("INSERT INTO public.source_of_funds(\n" +
            "            name, displayOrder, createddate)\n" +
            "    VALUES (#{name},#{displayOrder}, NOW());")
    @Options(useGeneratedKeys = true)
    Integer Insert(SourceOfFundDTO dto);

    @Select(" SELECT * FROM source_of_funds ")
    List<SourceOfFundDTO> getAll();

    @Update("update source_of_funds set name = #{name}, displayOrder = #{displayOrder} where id = #{id}")
    void update(SourceOfFundDTO dto);

}
