package org.openlmis.rnr.repository.mapper;

import org.apache.ibatis.annotations.*;
import org.openlmis.core.domain.Product;
import org.openlmis.core.domain.ProductCategory;
import org.openlmis.rnr.domain.MonitoringReportLineItem;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MonitoringLineItemMapper {

@Insert("INSERT INTO public.monitoring_report_line_items(\n" +
        "             reportId, productId, productCode,product, productCategoryId,dispensingUnit, packSize,stockOnHand, \n" +
        "            quantityRequested, createdBy, createdDate, modifiedBy, modifiedDate)\n" +
        "    VALUES ( #{reportId}, #{productId},#{productCode}, #{product}, #{productCategoryId},#{dispensingUnit},#{packSize}, #{stockOnHand}, \n" +
        "            #{quantityRequested}, #{createdBy}, NOW(), #{modifiedBy}, NOW());")
@Options(useGeneratedKeys = true)
Integer insert(MonitoringReportLineItem lineItem);

@Update("UPDATE public.monitoring_report_line_items\n" +
        "   SET productCode = #{productCode}, reportId=#{reportId}, productId=#{productId}, product=#{product},packSize=#{packSize},dispensingUnit=#{dispensingUnit}, productCategoryId=#{productCategoryId}, \n" +
        "       stockOnHand=#{stockOnHand}, quantityRequested=#{quantityRequested},  \n" +
        "       modifiedBy=#{modifiedBy}, modifiedDate=#{modifiedBy}\n" +
        " WHERE id = #{id}\n ")
void update(MonitoringReportLineItem lineItem);

    @Select("select * from monitoring_report_line_items where reportId = #{reportId} order by id")
    @Results(value = {
            @Result(property = "productId", column = "productId"),
            @Result(property = "products", column = "productId", javaType = Product.class,
                    one = @One(select = "org.openlmis.core.repository.mapper.ProductMapper.getById")),
            @Result(property = "productCategories", column = "productCategoryId", javaType = ProductCategory.class,
                    one = @One(select = "org.openlmis.core.repository.mapper.ProductCategoryMapper.getById"))
    })
    List<MonitoringReportLineItem> getLineItems(@Param("reportId") Long orderId);

}
