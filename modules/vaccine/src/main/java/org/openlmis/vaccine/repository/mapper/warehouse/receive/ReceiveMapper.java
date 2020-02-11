package org.openlmis.vaccine.repository.mapper.warehouse.receive;

import org.apache.ibatis.annotations.*;
import org.apache.ibatis.session.RowBounds;
import org.openlmis.core.domain.SupplyPartner;
import org.openlmis.vaccine.domain.wms.Asn;
import org.openlmis.vaccine.domain.wms.Port;
import org.openlmis.vaccine.domain.wms.Receive;
import org.openlmis.vaccine.dto.CurrencyDTO;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public interface ReceiveMapper {

 @Insert("INSERT INTO public.receives(\n" +
         "             purchaseOrderId,poDate,supplierId, poNumber,asnId, \n" +
         "            receiveDate,blawBnumber,country,flightVesselNumber, \n" +
         "            portOfArrival,expectedArrivalDate,actualArrivalDate, \n" +
         "            clearingAgent,shippingAgent,status,note,noteToSupplier,description, \n" +
         "            isForeignProcurement,createdBy,createdDate,modifiedBy,modifiedDate,currencyId)\n" +
         "    VALUES (#{purchaseOrderId}, #{poDate},#{supplier.id}, #{poNumber},#{asnId}, \n" +
         "            #{receiveDate},#{blawBnumber},#{country},#{flightVesselNumber}, \n" +
         "            #{portOfArrival},#{expectedArrivalDate},#{actualArrivalDate}, \n" +
         "            #{clearingAgent}, #{shippingAgent},#{status},#{note},#{noteToSupplier},#{description},#{isForeignProcurement}, \n" +
         "            #{createdBy}, NOW(),#{modifiedBy},NOW(), #{currencyId});")
    @Options(useGeneratedKeys = true)
    Integer insert(Receive receive);

  @Update(" UPDATE public.receives\n" +
          "   SET  purchaseOrderId=#{purchaseOrderId}, poDate=#{poDate}, supplierId=#{supplier.id}, poNumber= #{poNumber}, \n" +
          "       asnId=#{asnId}, receiveDate=#{receiveDate}, blawBnumber=#{blawBnumber}, country=#{country}, flightVesselNumber=#{flightVesselNumber}, \n" +
          "       portOfArrival=#{portOfArrival}, expectedArrivalDate=#{expectedArrivalDate}, actualArrivalDate=#{actualArrivalDate}, \n" +
          "       clearingAgent=#{clearingAgent}, shippingAgent=#{shippingAgent},status=#{status}, note=#{note}, noteToSupplier=#{noteToSupplier}, \n" +
          "       description=#{description}, isForeignProcurement=#{isForeignProcurement}, modifiedBy=#{modifiedBy}, modifiedDate=NOW(), currencyId=#{currencyId}\n" +
          " WHERE id = #{id}")
  void update(Receive receive);


    @Select("select * from receives where id = #{id}")
    @Results(value = {
            @Result(column = "id", property = "id"),
            @Result(column = "asnId", property = "asnId"),
            @Result(property = "receiveLineItems", column = "id", javaType = List.class,
                    many = @Many(select = "org.openlmis.vaccine.repository.mapper.warehouse.receive.ReceiveLineItemMapper.getByReceiveId")),
            @Result(property = "supplier", column = "supplierId", javaType = SupplyPartner.class,
                    one = @One(select = "org.openlmis.core.repository.mapper.SupplyPartnerMapper.getById")),
           /* @Result(property = "purchaseDocuments", column = "id", javaType = List.class,
                    many = @Many(select = "org.openlmis.vaccine.repository.mapper.warehouse.receive.PurchaseDocumentMapper.getByReceiveId")),*/
            @Result(property = "port", column = "portofarrival", javaType = Port.class,
                    one = @One(select = "org.openlmis.vaccine.repository.mapper.warehouse.asn.PortMapper.getById")),

            @Result(property = "asn", column = "asnId", javaType = Asn.class,
                    one = @One(select = "org.openlmis.vaccine.repository.mapper.warehouse.asn.AsnMapper.getById")),

            @Result(property = "currency", column = "currencyId", javaType = CurrencyDTO.class,
                    one = @One(select = "org.openlmis.vaccine.repository.mapper.warehouse.asn.CurrencyMapper.getById"))
    })
    Receive getById(@Param("id") Long id);

    @Select(" select * from receives")
    @Results(value = {
            @Result(column = "id", property = "id"),
            @Result(property = "receiveLineItems", column = "id", javaType = List.class,
                    many = @Many(select = "org.openlmis.vaccine.repository.mapper.warehouse.receive.ReceiveLineItemMapper.getByReceiveId")),
            @Result(property = "supplier", column = "supplierid", javaType = SupplyPartner.class,
                    one = @One(select = "org.openlmis.core.repository.mapper.SupplyPartnerMapper.getById")),
            @Result(property = "currency", column = "currencyId", javaType = CurrencyDTO.class,
                    one = @One(select = "org.openlmis.vaccine.repository.mapper.warehouse.asn.CurrencyMapper.getById"))

            /*,
            @Result(property = "purchaseDocuments", column = "id", javaType = List.class,
                    many = @Many(select = "org.openlmis.vaccine.repository.mapper.warehouse.asn.PurchaseDocumentMapper.getByReceiveId"))*/
    })
    List<Receive> getAll();


/*
    Apply search
*/

    @Select("SELECT COUNT(*) FROM receives A " +
            "WHERE (LOWER(A.blawBnumber) LIKE '%' || LOWER(#{searchParam}) || '%')")
    Integer getTotalSearchResultCountByBlawBnumber(String searchParam);
    @Select("SELECT COUNT(*) FROM receives A " +
            " WHERE (LOWER(A.ponumber) LIKE '%' || LOWER(#{searchParam}) || '%')")
    Integer getTotalSearchResultCountByPonumber(String searchParam);

    @Select("SELECT COUNT(*) FROM receives A INNER JOIN manufacturers M ON M.id = A.supplierId AND " +
            "(LOWER(M.name) LIKE '%' || LOWER(#{searchParam}) || '%')")
    Integer getTotalSearchResultCountBySupplier(String searchParam);

    @Select("SELECT COUNT(*) FROM receives ")
    Integer getTotalSearchResultCountAll();

    @SelectProvider(type = ReceiveMapper.SelectReceive.class, method = "getReceiveBySearchParam")
    @Results(value = {
            @Result(column = "id", property = "id"),
            @Result(property = "receiveLineItems", column = "id", javaType = List.class,
                    many = @Many(select = "org.openlmis.vaccine.repository.mapper.warehouse.receive.ReceiveLineItemMapper.getByReceiveId")),
            @Result(property = "supplier", column = "supplierid", javaType = SupplyPartner.class,
                    one = @One(select = "org.openlmis.core.repository.mapper.SupplyPartnerMapper.getById")),
            @Result(property = "currency", column = "currencyId", javaType = CurrencyDTO.class,
                    one = @One(select = "org.openlmis.vaccine.repository.mapper.warehouse.asn.CurrencyMapper.getById")),

            @Result(property = "asn", column = "asnId", javaType = Asn.class,
                    one = @One(select = "org.openlmis.vaccine.repository.mapper.warehouse.asn.AsnMapper.getById")),

            /*,
            @Result(property = "purchaseDocuments", column = "id", javaType = List.class,
                    many = @Many(select = "org.openlmis.vaccine.repository.mapper.warehouse.asn.PurchaseDocumentMapper.getByAsnId"))*/
    })
    List<Receive> search(@Param(value = "searchParam") String searchParam, @Param(value = "column") String column,
                     RowBounds rowBounds);


    class SelectReceive {
        @SuppressWarnings(value = "unused")
        public static String getAsnCountBy(Map<String, Object> params) {
            StringBuilder sql = new StringBuilder();
            sql.append("SELECT COUNT(*) FROM receives L WHERE ");
            return createQuery(sql, params).toString();
        }

        @SuppressWarnings(value = "unused")
        public static String getReceiveBySearchParam(Map<String, Object> params){
            StringBuilder sql = new StringBuilder();
            String searchParam = (String) params.get("searchParam");
            String column = (String) params.get("column");
            sql.append("SELECT A.*, M.* FROM receives A ");
            sql.append("LEFT JOIN manufacturers M on M.id = A.supplierid WHERE ");
            if(column.equalsIgnoreCase("blawBnumber")) {
                sql.append("(LOWER(A.blawBnumber) LIKE '%' || LOWER(#{searchParam}) || '%') ");

            }
            else if(column.equalsIgnoreCase("poNumber")) {
                sql.append(" (LOWER(A.poNumber) LIKE '%' || LOWER(#{searchParam}) || '%') ");
            }
            else if(column.equalsIgnoreCase("supplier")) {
                sql.append(" (LOWER(M.name) LIKE '%' || LOWER(#{searchParam}) || '%') ");
            }
            sql.append("ORDER BY LOWER(A.poNumber)");
            return sql.toString();
        }

        private static StringBuilder createQuery(StringBuilder sql, Map<String, Object> params) {
            String searchParam = (String) params.get("searchParam");
            String column = (String) params.get("column");
            if(column.equalsIgnoreCase("blawBnumber")) {
                sql.append("(LOWER(A.blawBnumber) LIKE LOWER('%" + searchParam + "%') ");
            }
            else if(column.equalsIgnoreCase("poNumber")) {
                sql.append("LOWER(A.poNumber) LIKE LOWER('%" + searchParam + "%'))");
            }
            else if(column.equalsIgnoreCase("supplier")) {
                sql.append("LOWER(M.name) LIKE LOWER('%" + searchParam + "%'))");
            }
            return sql;
        }
    }

    @Select(" SELECT * FROM fn_create_inpsection(#{id}::int) ")
    Integer updateInspection(@Param("id") Long id);

    @Select("SELECT receiveNumber from receive_line_Items order by id desc limit 1")
    String getLastReceiptNumber();


}
