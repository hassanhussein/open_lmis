package org.openlmis.lookupapi.mapper;

import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;

@Repository
public interface SCPortalInterfaceMapper {

    @Select(" SELECT 'COVID19' as \"program\", \n" +
            "p.code as \"product_code\", pc.name as category,\n" +
            "p.description\n" +
            " FROM program_products pP\n" +
            "\n" +
            "JOIN products p ON p.id = pp.productId \n" +
            "join programs pr ON pr.id = pp.programId and pr.id =1\n" +
            "join product_categories pc ON pp.productcategoryid = pc.id\n" +
            "LIMIT 100 ")
    List<HashMap<String, Object>> getAllProducts();

    @Select(" SELECT f.id as \"facility_id\", productcode as \"product_code\"\n" +
            ", 2 as \"level\", stockinhand as \"quantity\", r.modifieddate as \"updated_at\" \n" +
            "from requisitions r\n" +
            "JOIN requisition_line_items i on r.id = i.rnrid\n" +
            "JOIN facilities F on r.facilityId = F.ID\n" +
            "limit 100 ")

    List<HashMap<String, Object>> getStockInHand();
}
