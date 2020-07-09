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
import org.openlmis.vaccine.domain.wms.dto.PutAwayDTO;
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

    @Select("select r.poNumber ,i.id,asnNumber,A.modifiedDate asnDate, i.receiveNumber receiptNumber,r.modifiedDate receiptDate, i.status,i.status as customStatus from inspections i\n" +
            "JOIN receives r on i.receiveid = r.id \n" +
            "JOIN receive_LINE_ITEMS LI on r.id = li.receiveId\n" +
            "JOIN Asns A ON a.id = r.asnId ")
    List<InspectionDTO> getAll();

    @SelectProvider(type = InspectionMapper.SelectInspection.class, method = "getSearchBy")
    List<InspectionDTO> searchBy(@Param(value = "searchParam") String searchParam, @Param(value = "column") String column,
                                 RowBounds rowBounds);

    @SelectProvider(type = InspectionMapper.SelectInspection1.class, method = "getSearchForPutAway")
    List<PutAwayDTO> searchPutAwayBy(@Param(value = "searchParam") String searchParam, @Param(value = "column") String column,
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
            " shippedProvidedComment,conditionOfBox, labelAttachedComment,invoiceNumber, varNumber )\n" +
            "    VALUES (#{receiveId}, #{inspectionDate}, #{inspectionNote}, #{inspectedBy}, #{status}, \n" +
            "            #{createBy}, NOW(), #{modifiedBy}, NOW(), #{receiptNumber}, #{descriptionOfInspection}, #{isShippedProvided}, #{isShipped}," +
            " #{shippedComment}, #{shippedProvidedComment}, #{conditionOfBox}, #{labelAttachedComment},#{invoiceNumber}, #{varNumber});\n")
    Integer insert(Inspection inspection);

    @Update("UPDATE public.inspections\n" +
            "   SET  receiveId=#{receive.id}, inspectionDate=#{inspectionDate}, inspectionNote=#{inspectionNote}, inspectedBy=#{inspectedBy}, \n" +
            "       status=#{status},receiptNumber = #{receiptNumber}, modifiedBy=#{modifiedBy}, modifiedDate=NOW(), " +
            " descriptionOfInspection=#{descriptionOfInspection}, isShippedProvided = #{isShippedProvided}, isShipped=#{isShipped}," +
            " shippedComment=#{shippedComment}, shippedProvidedComment=#{shippedProvidedComment}, conditionOfBox=#{conditionOfBox}, labelAttachedComment=#{labelAttachedComment}, invoiceNumber=#{invoiceNumber}, varNumber=#{varNumber} \n" +
            " WHERE id = #{id};\n")
    void update(Inspection inspection);

    @Select("select * from fn_increment_stock(#{id}::int); ")
    Integer updateStockCard(@Param("id") Long id);

    @Select("select \n" +
            "r.country as hdr_country,\n" +
            "RECEIPTNUMBER as hdr_reportnumber,\n" +
            "to_char(now(), 'DD/MM/YYYY') as hdr_dateofreport,\n" +
            "'AAABBCC'||', '||to_char(r.receivedate, 'DD/MM/YYYY') as hdr_nameofcoldstore,\n" +
            "'CVS'||', '|| to_char(inspectionDate, 'DD/MM/YYYY') as hdr_completiontime,\n" +
            "i.id as ins_id,\n" +
            "i.receiveid as ins_receiveid,\n" +
            "i.inspectiondate as ins_inspectiondate,\n" +
            "i.inspectionnote as ins_inspectionnote,\n" +
            "i.inspectedby as ins_inspectedby,\n" +
            "i.status as ins_status,\n" +
            "i.createby as ins_createby,\n" +
            "i.createddate as ins_createddate,\n" +
            "i.modifiedby as ins_modifiedby,\n" +
            "i.modifieddate as ins_modifieddate,\n" +
            "i.receiptnumber as ins_receiptnumber,\n" +
            "i.descriptionofinspection as ins_descriptionofinspection,\n" +
            "i.isshippedprovided as ins_isshippedprovided,\n" +
            "i.isshipped as ins_isshipped,\n" +
            "i.shippedcomment as ins_shippedcomment,\n" +
            "i.shippedprovidedcomment as ins_shippedprovidedcomment,\n" +
            "i.conditionofbox as ins_conditionofbox,\n" +
            "i.labelattachedcomment as ins_labelattachedcomment,\n" +
            "ii.id as insi_id,\n" +
            "ii.inspectionid as insi_inspectionid,\n" +
            "ii.productid as insi_productid,\n" +
            "ii.quantitycounted as insi_quantitycounted,\n" +
            "ii.boxcounted as insi_boxcounted,\n" +
            "ii.passquantity as insi_passquantity,\n" +
            "ii.passlocationid as insi_passlocationid,\n" +
            "ii.failquantity as insi_failquantity,\n" +
            "ii.failreason as insi_failreason,\n" +
            "ii.faillocationid as insi_faillocationid,\n" +
            "ii.lotflag as insi_lotflag,\n" +
            "ii.dryiceflag as insi_dryiceflag,\n" +
            "ii.vvmflag as insi_vvmflag,\n" +
            "ii.cccardflag as insi_cccardflag,\n" +
            "ii.electronicdeviceflag as insi_electronicdeviceflag,\n" +
            "ii.createdby as insi_createdby,\n" +
            "ii.createddate as insi_createddate,\n" +
            "ii.modifiedby as insi_modifiedby,\n" +
            "ii.modifieddate as insi_modifieddate,\n" +
            "ii.othermonitor as insi_othermonitor,\n" +
            "ii.icepackflag as insi_icepackflag,\n" +
            "ii.nocoolantflag as insi_nocoolantflag,\n" +
            "r.id as rec_id,\n" +
            "r.purchaseorderid as rec_purchaseorderid,\n" +
            "r.ponumber as rec_ponumber,\n" +
            "r.podate as rec_podate,\n" +
            "r.supplierid as rec_supplierid,\n" +
            "r.asnid as rec_asnid,\n" +
            "r.receivedate as rec_receivedate,\n" +
            "r.blawbnumber as rec_blawbnumber,\n" +
            "r.country as rec_country,\n" +
            "r.flightvesselnumber as rec_flightvesselnumber,\n" +
            "r.portofarrival as rec_portofarrival,\n" +
            "r.expectedarrivaldate as rec_expectedarrivaldate,\n" +
            "r.actualarrivaldate as rec_actualarrivaldate,\n" +
            "r.clearingagent as rec_clearingagent,\n" +
            "r.shippingagent as rec_shippingagent,\n" +
            "r.status as rec_status,\n" +
            "r.note as rec_note,\n" +
            "r.notetosupplier as rec_notetosupplier,\n" +
            "r.description as rec_description,\n" +
            "r.createdby as rec_createdby,\n" +
            "r.createddate as rec_createddate,\n" +
            "r.modifiedby as rec_modifiedby,\n" +
            "r.modifieddate as rec_modifieddate,\n" +
            "r.isforeignprocurement as rec_isforeignprocurement,\n" +
            "r.currencyid as rec_currencyid,\n" +
            "ri.id as reci_id,\n" +
            "ri.receiveid as reci_receiveid,\n" +
            "ri.productid as reci_productid,\n" +
            "ri.expirydate as reci_expirydate,\n" +
            "ri.manufacturingdate as reci_manufacturingdate,\n" +
            "ri.quantitycounted as reci_quantitycounted,\n" +
            "ri.unitprice as reci_unitprice,\n" +
            "ri.boxcounted as reci_boxcounted,\n" +
            "ri.lotflag as reci_lotflag,\n" +
            "ri.createdby as reci_createdby,\n" +
            "ri.createddate as reci_createddate,\n" +
            "ri.modifiedby as reci_modifiedby,\n" +
            "ri.modifieddate as reci_modifieddate,\n" +
            "a.id as asn_id,\n" +
            "a.purchaseorderid as asn_purchaseorderid,\n" +
            "a.ponumber as asn_ponumber,\n" +
            "a.podate as asn_podate,\n" +
            "a.supplierid as asn_supplierid,\n" +
            "a.asnnumber as asn_asnnumber,\n" +
            "a.asndate as asn_asndate,\n" +
            "a.blawbnumber as asn_blawbnumber,\n" +
            "a.flightvesselnumber as asn_flightvesselnumber,\n" +
            "a.portofarrival as asn_portofarrival,\n" +
            "a.expectedarrivaldate as asn_expectedarrivaldate,\n" +
            "a.clearingagent as asn_clearingagent,\n" +
            "a.status as asn_status,\n" +
            "a.note as asn_note,\n" +
            "a.createdby as asn_createdby,\n" +
            "a.createddate as asn_createddate,\n" +
            "a.modifiedby as asn_modifiedby,\n" +
            "a.modifieddate as asn_modifieddate,\n" +
            "a.expecteddeliverydate as asn_expecteddeliverydate,\n" +
            "a.active as asn_active,\n" +
            "a.currencyid as asn_currencyid,\n" +
            "ai.id as asni_id,\n" +
            "ai.asnid as asni_asnid,\n" +
            "ai.productid as asni_productid,\n" +
            "ai.expirydate as asni_expirydate,\n" +
            "ai.manufacturingdate as asni_manufacturingdate,\n" +
            "ai.quantityexpected as asni_quantityexpected,\n" +
            "ai.lotflag as asni_lotflag,\n" +
            "ai.createdby as asni_createdby,\n" +
            "ai.createddate as asni_createddate,\n" +
            "ai.modifiedby as asni_modifiedby,\n" +
            "ai.modifieddate as asni_modifieddate,\n" +
            "ai.unitprice as asni_unitprice,\n" +
            " p.primaryName product\n"+
            "from inspections i\n" +
            "JOIN inspection_line_items ii on i.id = ii.inspectionid\n" +
            "JOIN Receives r on i.receiveId = r.id\n" +
            "JOIN receive_line_items ri ON ri.receiveid = r.id\n" +
            "JOIN asns a ON r.asnId = a.id\n" +
            "JOIN asn_details ai ON a.id = ai.asnId " +
            " JOIN products p on ii.productID = p.id\n" +
            "WHERE i.MODIFIEDDATE::date <= #{endDate}::date and i.modifieddate::date > #{startDate}::date")
    List<HashMap<String, Object>> getBy(@Param("product") String product,@Param("startDate") String startDate, @Param("endDate") String endDate, String year);

    @Select("SELECT varNumber FROM inspections order by id desc limit 1")
    String generateVarNumber();

    @Select(" SELECT COUNT(1) FROM ( SELECT DISTINCT ON(r.poNumber) * FROM inspections i\n" +
            "                    JOIN receives r on i.receiveid = r.id \n" +
            "                    JOIN receive_LINE_ITEMS LI on r.id = li.receiveid\n" +
            "                    JOIN Asns A ON a.id = r.asnId\n" +
            "                    JOIN inspection_line_items item on i.id = item.inspectionId\n" +
            "                    JOIN inspection_lots Lot ON item.id = lot.inspectionlineitemId \n" +
            "                    Join wms_locations loc ON lot.passLocationId = loc.ID\n" +
            "                    Join wms_location_types lt On loc.typeId = lt.id\n" +
            "                    JOIN warehouses house On loc.warehouseId = house.id\n" +
            "                    where (R.poNumber::text LIKE '%' || #{searchParam} || '%') and i.status='IN-PUTAWAY'" +
            "       )as k ")
    Integer getTotalSearchResultCountForPutAway(@Param("searchParam") String searchParam);

    @Select(" select distinct on(r.poNumber) r.poNumber, i.id,asnNumber,A.modifiedDate asnDate, receiptNumber,r.modifiedDate receiptDate, i.status,\n" +
            "loc.name binLocation, house.name warehouseName, inspectionDate\n" +
            "from inspections i\n" +
            "                JOIN receives r on i.receiveid = r.id \n" +
            "                    JOIN receive_LINE_ITEMS LI on r.id = li.receiveid\n" +
            "                    JOIN Asns A ON a.id = r.asnId\n" +
            "                    JOIN inspection_line_items item on i.id = item.inspectionId\n" +
            "                    JOIN inspection_lots Lot ON item.id = lot.inspectionlineitemId \n" +
            "                    Join wms_locations loc ON lot.passLocationId = loc.ID\n" +
            "                    Join wms_location_types lt On loc.typeId = lt.id\n" +
            "                    JOIN warehouses house On loc.warehouseId = house.id" +
            "                     where I.STATUS = 'IN-PUTAWAY' ")
    List<PutAwayDTO> searchedAllPutAway();
    @Update(" update inspections set status = #{status} WHERE id=#{id} ")
    void updateStatus(@Param("status") String status,@Param("id") Long inspectionId);

    class SelectInspection {

        public static String getSearchBy(Map<String, Object> params) {
            StringBuilder sql = new StringBuilder();
            sql.append("WITH Q AS (select i.id,asnNumber,r.poNumber,A.modifiedDate asnDate, r.receiveNumber as receiptNumber,r.modifiedDate receiptDate, i.status,i.status as customStatus from inspections i\n" +
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

        class SelectInspection1 {
        public static String getSearchForPutAway(Map<String, Object> params) {
            StringBuilder sql = new StringBuilder();
            sql.append("WITH Q AS (select i.id,asnNumber,r.poNumber,A.modifiedDate asnDate, receiptNumber,r.modifiedDate receiptDate, i.status,\n" +
                    "loc.name binLocation, house.name warehouseName, inspectionDate\n" +
                    "from inspections i\n" +
                    "                JOIN receives r on i.receiveid = r.id \n" +
                    "                    JOIN receive_LINE_ITEMS LI on r.id = li.receiveid\n" +
                    "                    JOIN Asns A ON a.id = r.asnId\n" +
                    "                    JOIN inspection_line_items item on i.id = item.inspectionId\n" +
                    "                    JOIN inspection_lots Lot ON item.id = lot.inspectionlineitemId \n" +
                    "                    Join wms_locations loc ON lot.passLocationId = loc.ID\n" +
                    "                    Join wms_location_types lt On loc.typeId = lt.id\n" +
                    "                    JOIN warehouses house On loc.warehouseId = house.id\n" +
                    "                  \n" +
                    "                     )\n" +
                    "                    select  distinct on(q.poNumber) q.poNumber, * from q where status='IN-PUTAWAY' AND  ");
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
