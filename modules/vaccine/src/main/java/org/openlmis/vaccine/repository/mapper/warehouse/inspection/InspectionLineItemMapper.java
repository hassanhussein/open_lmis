package org.openlmis.vaccine.repository.mapper.warehouse.inspection;

import org.apache.ibatis.annotations.*;
import org.openlmis.core.domain.Product;
import org.openlmis.vaccine.domain.wms.InspectionLineItem;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface InspectionLineItemMapper {

    @Select(" SELECT * FROM INSPECTION_LINE_ITEMS WHERE inspectionId = #{inspectionId} ")
    @Results(value = {
            @Result(property = "id", column = "id"),
            @Result(property = "productId", column = "productId"),
            @Result(property = "product", javaType = Product.class, column = "productId",
                    one = @One(select = "org.openlmis.core.repository.mapper.ProductMapper.getById")),
            @Result(property = "inspectionLotList", javaType = List.class, column = "inspectionLineItemId",
                    many = @Many(select = "org.openlmis.vaccine.repository.mapper.warehouse.inspection.InspectionLotMapper.getByLineItemId"))
    })
    List<InspectionLineItem> getLineItemsByInspectionId(@Param("inspectionId") Long inspectionId);


}
