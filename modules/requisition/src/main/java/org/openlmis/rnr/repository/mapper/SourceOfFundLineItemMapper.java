package org.openlmis.rnr.repository.mapper;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.openlmis.rnr.dto.SourceOfFundLineItemDTO;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SourceOfFundLineItemMapper {

    @Insert("INSERT INTO public.requisition_funding_source_line_items(\n" +
            "            rnrId, allocatedBudget, createdBy, createddate, modifiedBy, \n" +
            "            modifieddate)\n" +
            "    VALUES (#{rnrId}, #{allocatedBudget}, #{createdBy}, NOW(), #{modifiedBy}, \n" +
            "            NOW());")
    @Options(useGeneratedKeys = true)
    Integer Insert(SourceOfFundLineItemDTO dto);

    @Update("UPDATE public.requisition_funding_source_line_items\n" +
            "   SET  rnrid=#{rnrId}, allocatedbudget=#{allocatedBudget}, createdby=#{createdBy},modifiedby=#{modifiedBy}\n" +
            " WHERE id=#{id} ;\n")
    void update(SourceOfFundLineItemDTO dto);

    @Select("SELECT * FROM requisition_funding_source_line_items where rnrId= #{rnrId}")
    List<SourceOfFundLineItemDTO>getLineItems(Long rnrId);

}
