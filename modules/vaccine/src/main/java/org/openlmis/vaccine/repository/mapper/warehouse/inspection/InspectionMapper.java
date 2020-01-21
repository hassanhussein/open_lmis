package org.openlmis.vaccine.repository.mapper.warehouse.inspection;

import org.apache.ibatis.annotations.*;
import org.apache.ibatis.session.RowBounds;
import org.openlmis.core.domain.Pagination;
import org.openlmis.core.domain.Product;
import org.openlmis.core.domain.SupplyPartner;
import org.openlmis.core.domain.User;
import org.openlmis.vaccine.domain.wms.Inspection;
import org.openlmis.vaccine.domain.wms.Port;
import org.openlmis.vaccine.domain.wms.Receive;
import org.openlmis.vaccine.domain.wms.dto.InspectionDTO;
import org.openlmis.vaccine.domain.wms.dto.VvmStatusDTO;
import org.openlmis.vaccine.dto.CurrencyDTO;
import org.openlmis.vaccine.repository.mapper.warehouse.receive.ReceiveMapper;
import org.springframework.security.access.method.P;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public interface InspectionMapper {

    @Select("\n" +
            "select count(*) from inspections i\n" +
            "JOIN receives r on i.receiveId = r.id \n" +
            "JOIN receive_LINE_ITEMS LI on r.id = li.receiveId\n" +
            "JOIN Asns A ON a.id = r.asnId \n" +
            "WHERE (LOWER(asnNumber) LIKE '%' || LOWER(#{searchParam}) || '%')")
    Integer getTotalSearchResultCountByAsnNumber(@Param("searchParam") String searchParam);

    @Select("\n" +
            "select count(*) from inspections i\n" +
            "JOIN receives r on i.receiveId = r.id \n" +
            "JOIN receive_LINE_ITEMS LI on r.id = li.receiveId\n" +
            "JOIN Asns A ON a.id = r.asnId \n" +
            "WHERE (A.poNumber::text LIKE '%' || #{searchParam} || '%')")
    Integer getTotalSearchResultCountByAsnDate(@Param("searchParam") String searchParam);

    Integer getTotalSearchResultCountByReceiptNumber(@Param("searchParam") String searchParam);

    Integer getTotalSearchResultCountByReceiptDate(@Param("searchParam") String searchParam);

    @Select("\n" +
            "select count(*) from inspections i\n" +
            "JOIN receives r on i.receiveId = r.id \n" +
            "JOIN receive_LINE_ITEMS LI on r.id = li.receiveId\n" +
            "JOIN Asns A ON a.id = r.asnId \n")
    Integer getTotalSearchResultCountAll();

    @Select("select i.id,asnNumber,A.modifiedDate asnDate, i.receiptNumber,r.modifiedDate receiptDate, i.status from inspections i\n" +
            "JOIN receives r on i.receiveid = r.id \n" +
            "JOIN receive_LINE_ITEMS LI on r.id = li.receiveId\n" +
            "JOIN Asns A ON a.id = r.asnId ")
    List<InspectionDTO> getAll();

    @SelectProvider(type = InspectionMapper.SelectInspection.class, method = "getSearchBy")
    List<InspectionDTO> searchBy(@Param(value = "searchParam") String searchParam, @Param(value = "column") String column,
                                 RowBounds rowBounds);

    @Select(" SELECT * FROM inspections where id = #{id}")
    @Results(value = {
            @Result(column = "id", property = "id"),
            @Result(property = "receive", javaType = Receive.class, column = "receiveId",
                    one = @One(select = "org.openlmis.vaccine.repository.mapper.warehouse.receive.ReceiveMapper.getById")),
            @Result(property = "receiveId", column = "receiveId"),
            @Result(property = "user", javaType = User.class, column = "modifiedBy",
                    one = @One(select = "org.openlmis.core.repository.mapper.UserMapper.getById")),
            @Result(property = "inspectedBy", column = "inspectedBy"),
            @Result(property = "lineItems", javaType = List.class, column = "id",
                    many = @Many(select = "org.openlmis.vaccine.repository.mapper.warehouse.inspection.InspectionLineItemMapper.getLineItemsByInspectionId"))
    })
    Inspection getById(@Param("id") Long id);

    @Insert("INSERT INTO public.inspections(\n" +
            "            receiveId, inspectionDate, inspectionNote, inspectedBy, status, \n" +
            "            createBy, createdDate, modifiedBy, modifiedDate,receiptNumber,descriptionOfInspection,isShippedProvided, isShipped, shippedComment, " +
            " shippedProvidedComment,conditionOfBox, labelAttachedComment )\n" +
            "    VALUES (#{receiveId}, #{inspectionDate}, #{inspectionNote}, #{inspectedBy}, #{status}, \n" +
            "            #{createBy}, NOW(), #{modifiedBy}, NOW(), #{receiptNumber}, #{descriptionOfInspection}, #{isShippedProvided}, #{isShipped}," +
            " #{shippedComment}, #{shippedProvidedComment}, #{conditionOfBox}, #{labelAttachedComment});\n")
    Integer insert(Inspection inspection);

    @Update("UPDATE public.inspections\n" +
            "   SET  receiveId=#{receive.id}, inspectionDate=#{inspectionDate}, inspectionNote=#{inspectionNote}, inspectedBy=#{inspectedBy}, \n" +
            "       status=#{status},receiptNumber = #{receiptNumber}, modifiedBy=#{modifiedBy}, modifiedDate=NOW(), " +
            " descriptionOfInspection=#{descriptionOfInspection}, isShippedProvided = #{isShippedProvided}, isShipped=#{isShipped}," +
            " shippedComment=#{shippedComment}, shippedProvidedComment=#{shippedProvidedComment}, conditionOfBox=#{conditionOfBox}, labelAttachedComment=#{labelAttachedComment} \n" +
            " WHERE id = #{id};\n")
    void update(Inspection inspection);

    @Select("select * from fn_increment_stock(#{id}::int); ")
    Integer updateStockCard(@Param("id") Long id);

    @Select(" select * from inspections i\n" +
            "JOIN inspection_line_items l on i.id = l.inspectionid\n" +
            "\n" +
            "WHERE i.MODIFIEDDATE::date <= #{endDate}::date and i.modifieddate::date > #{startDate}::date and status = 'DRAFT' ")
    List<HashMap<String, Object>> getBy(@Param("product") String product,@Param("startDate") String startDate, @Param("endDate") String endDate, String year);

    class SelectInspection {

        public static String getSearchBy(Map<String, Object> params) {
            StringBuilder sql = new StringBuilder();
            sql.append("WITH Q AS (select i.id,asnNumber,r.poNumber,A.modifiedDate asnDate, receiptNumber,r.modifiedDate receiptDate, i.status from inspections i\n" +
                    "JOIN receives r on i.receiveid = r.id \n" +
                    "JOIN receive_LINE_ITEMS LI on r.id = li.receiveid\n" +
                    "JOIN Asns A ON a.id = r.asnId )\n" +
                    "SELECT * FROM Q WHERE ");
            return createQuery(sql, params).toString();
        }

        private static StringBuilder createQuery(StringBuilder sql, Map<String, Object> params) {

            String searchParam = (String) params.get("searchParam");
            String column = (String) params.get("column");
            if (column.equalsIgnoreCase("asnNumber")) {
                sql.append("(LOWER(Q.asnNumber) LIKE LOWER('%" + searchParam + "%') ) ");
            } else if (column.equalsIgnoreCase("poNumber")) {
                sql.append("(Q.poNumber LIKE '%" + searchParam + "%')");
            }
            return sql;
        }
    }

    @Select("SELECT * FROM vvm_statuses")
    List<VvmStatusDTO> getAllVvmStatuses();
}
