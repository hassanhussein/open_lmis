package org.openlmis.rnr.repository.mapper;

import org.apache.ibatis.annotations.*;
import org.openlmis.core.dto.SourceOfFundDTO;
import org.openlmis.rnr.domain.Rnr;
import org.openlmis.rnr.domain.SourceOfFunds;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FacilitySourceOfFundMapper {

    @Insert("INSERT INTO public.requisition_source_of_funds(\n" +
            "            rnrId, name, quantity, createdBy, createdDate, modifiedBy, modifiedDate)\n" +
            "    VALUES (#{rnrId}, #{name}, #{quantity}, #{createdBy}, NOW(), #{modifiedBy}, NOW()) ")
    @Options(useGeneratedKeys = true)
    public Integer insert(SourceOfFunds sourceOfFunds);


    @Select("select * from requisition_source_of_funds where rnrId = #{rnrId}")
    List<SourceOfFunds> getByRnrId(Long rnrId);

    @Select("SELECT * FROM source_of_funds WHERE lower(name) = lower(#{name})")
    SourceOfFundDTO getSourceOfFundsByName(@Param("name") String name);

    @Delete("DELETE FROM requisition_source_of_funds WHERE id = #{sourceOfFundId}")
    void delete(Long sourceOfFundId);

    @Select("SELECT * FROM source_of_funds ORDER BY displayOrder")
    List<SourceOfFundDTO> getSourceOfFunds();

    @Delete("DELETE FROM requisition_source_of_funds WHERE rnrId = #{rnrId}")
    void deleteByLineItemId(Long rnrId);


    @Insert({"INSERT INTO public.requisition_source_of_funds(\n",
            "            rnrId, name, quantity, createdBy, createdDate, modifiedBy, modifiedDate)\n" ,
            "    VALUES (#{rnrId}, #{name}, #{quantity}, #{createdBy}, NOW(), #{modifiedBy}, NOW()) "})
    @Options(useGeneratedKeys = true)
    public Integer insertFacilitySourceOfFund(SourceOfFunds sourceOfFunds);

    @Delete(" DELETE FROM requisition_source_of_funds WHERE rnrId = #{rnrId}")
    void deleteFundSourceByRrnrAndName(@Param("name") String name, @Param("rnrId") Long rnrId);
}
