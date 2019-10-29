package org.openlmis.vaccine.repository.mapper.warehouse.asn;

import org.apache.ibatis.annotations.*;
import org.apache.ibatis.session.RowBounds;
import org.openlmis.vaccine.domain.wms.ASNDocument;
import org.openlmis.core.domain.SupplyPartner;
import org.openlmis.vaccine.domain.wms.Asn;
import org.openlmis.vaccine.domain.wms.Port;
import org.openlmis.vaccine.dto.CurrencyDTO;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public interface AsnMapper {
    @Insert(" INSERT INTO asns (ponumber,podate,supplierid,asnnumber,asndate,blawbnumber,flightvesselnumber,portofarrival,expectedarrivaldate, " +
            "clearingagent, expecteddeliverydate, status,note,createdBy, createdDate,modifiedBy,modifiedDate, active,currencyId)  VALUES(#{ponumber}, #{podate}, #{supplierid}, #{asnnumber}, #{asndate}, #{blawbnumber}, " +
            "#{flightvesselnumber}, #{portofarrival}, #{expectedarrivaldate}, #{clearingagent}, #{expecteddeliverydate}, #{status},#{note}, #{createdBy}, NOW(),#{modifiedBy}, NOW(),true, #{currencyId}) ")
    @Options(useGeneratedKeys = true)
    Long insert(Asn asn);

    @Update(" update asns set  ponumber = #{ponumber}, " +
            " podate = #{podate}, supplierid = #{supplierid}, asnnumber =#{asnnumber}, asndate = #{asndate}," +
            " blawbnumber = #{blawbnumber}, flightvesselnumber = #{flightvesselnumber}, portofarrival =#{portofarrival}, expecteddeliverydate = #{expecteddeliverydate}, expectedarrivaldate = #{expectedarrivaldate}," +
            " clearingagent = #{clearingagent}, status = #{status}, note =#{note}, " +
            " modifiedDate = now(), modifiedBy = #{modifiedBy}, currencyId = #{currencyId}  where id = #{id} ")
    void update(Asn asn);

    @Select("select * from asns where id = #{id}")
    @Results(value = {
            @Result(column = "id", property = "id"),
            @Result(property = "asnLineItems", column = "id", javaType = List.class,
                    many = @Many(select = "org.openlmis.vaccine.repository.mapper.warehouse.asn.AsnLineItemMapper.getByAsnId")),
            @Result(property = "supplier", column = "supplierid", javaType = SupplyPartner.class,
                    one = @One(select = "org.openlmis.core.repository.mapper.SupplyPartnerMapper.getById")),
            @Result(property = "purchaseDocuments", column = "id", javaType = List.class,
                    many = @Many(select = "org.openlmis.vaccine.repository.mapper.warehouse.asn.PurchaseDocumentMapper.getByAsnId")),
            @Result(property = "port", column = "portofarrival", javaType = Port.class,
                    one = @One(select = "org.openlmis.vaccine.repository.mapper.warehouse.asn.PortMapper.getById")),
            @Result(property = "currency", column = "currencyId", javaType = CurrencyDTO.class,
                    one = @One(select = "org.openlmis.vaccine.repository.mapper.warehouse.asn.CurrencyMapper.getById"))

    })
    Asn getById(@Param("id") Long id);

    @Select(" select * from asns where active = true ")
    @Results(value = {
            @Result(column = "id", property = "id"),
            @Result(property = "asnLineItems", column = "id", javaType = List.class,
                    many = @Many(select = "org.openlmis.vaccine.repository.mapper.warehouse.asn.AsnLineItemMapper.getByAsnId")),
            @Result(property = "supplier", column = "supplierid", javaType = SupplyPartner.class,
                    one = @One(select = "org.openlmis.core.repository.mapper.SupplyPartnerMapper.getById")),
            @Result(property = "purchaseDocuments", column = "id", javaType = List.class,
                    many = @Many(select = "org.openlmis.vaccine.repository.mapper.warehouse.asn.PurchaseDocumentMapper.getByAsnId")),
            @Result(property = "port", column = "portofarrival", javaType = Port.class,
                    one = @One(select = "org.openlmis.vaccine.repository.mapper.warehouse.asn.PortMapper.getById")),
            @Result(property = "currency", column = "currencyId", javaType = CurrencyDTO.class,
                    one = @One(select = "org.openlmis.vaccine.repository.mapper.warehouse.asn.CurrencyMapper.getById"))

    })
    List<Asn> getAll();

    @Select("SELECT COUNT(*) FROM asns A " +
            "WHERE (LOWER(A.asnnumber) LIKE '%' || LOWER(#{searchParam}) || '%') AND A.ACTIVE = TRUE ")
    Integer getTotalSearchResultCountByAsnumber(String searchParam);
    @Select("SELECT COUNT(*) FROM asns A " +
            " WHERE (LOWER(A.ponumber) LIKE '%' || LOWER(#{searchParam}) || '%') AND A.ACTIVE = TRUE ")
    Integer getTotalSearchResultCountByPonumber(String searchParam);

    @Select("SELECT COUNT(*) FROM asns A INNER JOIN manufacturers M ON M.id = A.supplierid " +
            " WHERE (LOWER(M.name) LIKE '%' || LOWER(#{searchParam}) || '%') AND A.ACTIVE = TRUE ")
    Integer getTotalSearchResultCountBySupplier(String searchParam);
    @Select("SELECT COUNT(*) FROM asns ")
    Integer getTotalSearchResultCountAll();

    @SelectProvider(type = SelectAsn.class, method = "getAsnBySearchParam")
    @Results(value = {
            @Result(column = "id", property = "id"),
            @Result(property = "asnLineItems", column = "id", javaType = List.class,
                    many = @Many(select = "org.openlmis.vaccine.repository.mapper.warehouse.asn.AsnLineItemMapper.getByAsnId")),
            @Result(property = "supplier", column = "supplierid", javaType = SupplyPartner.class,
                    one = @One(select = "org.openlmis.core.repository.mapper.SupplyPartnerMapper.getById")),
            @Result(property = "purchaseDocuments", column = "id", javaType = List.class,
                    many = @Many(select = "org.openlmis.vaccine.repository.mapper.warehouse.asn.PurchaseDocumentMapper.getByAsnId"))
    })
    List<Asn> search(@Param(value = "searchParam") String searchParam, @Param(value = "column") String column,
                     RowBounds rowBounds);

    @Delete("delete from asns where id = #{id}")
    void deleteById(@Param("id") Long id);


    @Insert({"INSERT INTO wms_documents ",
            "( documentType, url, createdDate,createdBy) ",
            "VALUES",
            "( #{documentType}, #{fileUrl},#{createdDate}, #{createdBy})"})
    @Options(useGeneratedKeys = true, keyProperty = "id", keyColumn = "id")
    void uploadDocument(ASNDocument asnDocument);

    @Delete("DELETE FROM asn_lots WHERE ASNDETAILID IN ( SELECT ID FROM asn_DETAILS WHERE AsnId = #{id}  )")
    void deleteByAsnDetail(@Param("id") Long id);

    @Delete("DELETE FROM ASN_DETAILS cascade where asnId = #{asnId} ")
    void deleteByAsn(@Param("asnId") Long asnId);

    @Update("update asns set active = false where id = #{id}")
    void disableAsnBy(@Param("id") Long id);

    class SelectAsn {
        @SuppressWarnings(value = "unused")
        public static String getAsnCountBy(Map<String, Object> params) {
            StringBuilder sql = new StringBuilder();
            sql.append("SELECT COUNT(*) FROM asns L WHERE active = true and ");
            return createQuery(sql, params).toString();
        }

        @SuppressWarnings(value = "unused")
        public static String getAsnBySearchParam(Map<String, Object> params){
            StringBuilder sql = new StringBuilder();
            String searchParam = (String) params.get("searchParam");
            String column = (String) params.get("column");
            sql.append("SELECT A.*, M.* FROM asns A ");
            sql.append("INNER JOIN manufacturers M on M.id = A.supplierid WHERE ");
            if(column.equalsIgnoreCase("asnumber")) {
                sql.append("(LOWER(A.asnnumber) LIKE '%' || LOWER(#{searchParam}) || '%') AND A.ACTIVE = TRUE ");

            }
            else if(column.equalsIgnoreCase("ponumber")) {
                sql.append(" (LOWER(A.ponumber) LIKE '%' || LOWER(#{searchParam}) || '%') AND A.ACTIVE = TRUE ");
            }
            else if(column.equalsIgnoreCase("supplier")) {
                sql.append(" (LOWER(M.name) LIKE '%' || LOWER(#{searchParam}) || '%') AND A.ACTIVE = TRUE ");
            }
            sql.append("ORDER BY LOWER(A.ponumber)");
            return sql.toString();
        }

        private static StringBuilder createQuery(StringBuilder sql, Map<String, Object> params) {
            String searchParam = (String) params.get("searchParam");
            String column = (String) params.get("column");
            if(column.equalsIgnoreCase("asnumber")) {
                sql.append("(LOWER(A.asnnumber) LIKE LOWER('%" + searchParam + "%') AND A.ACTIVE = TRUE ");
            }
            else if(column.equalsIgnoreCase("ponumber")) {
                sql.append("LOWER(A.ponumber) LIKE LOWER('%" + searchParam + "%') AND A.ACTIVE = TRUE )");
            }
            else if(column.equalsIgnoreCase("supplier")) {
                sql.append("LOWER(M.name) LIKE LOWER('%" + searchParam + "%') AND A.ACTIVE = TRUE )");
            }
            return sql;
        }
    }

    @Select(" select * from  currencies")
    List<CurrencyDTO> getAllCurrencies();


    @Select("select * from asns where asnNumber = #{asnNumber} LIMIT 1")
    @Results(value = {
            @Result(column = "id", property = "id"),
            @Result(property = "asnLineItems", column = "id", javaType = List.class,
                    many = @Many(select = "org.openlmis.vaccine.repository.mapper.warehouse.asn.AsnLineItemMapper.getByAsnId")),
            @Result(property = "supplier", column = "supplierid", javaType = SupplyPartner.class,
                    one = @One(select = "org.openlmis.core.repository.mapper.SupplyPartnerMapper.getById")),
            @Result(property = "purchaseDocuments", column = "id", javaType = List.class,
                    many = @Many(select = "org.openlmis.vaccine.repository.mapper.warehouse.asn.PurchaseDocumentMapper.getByAsnId")),
            @Result(property = "port", column = "portofarrival", javaType = Port.class,
                    one = @One(select = "org.openlmis.vaccine.repository.mapper.warehouse.asn.PortMapper.getById")),
            @Result(property = "currency", column = "currencyId", javaType = CurrencyDTO.class,
                    one = @One(select = "org.openlmis.vaccine.repository.mapper.warehouse.asn.CurrencyMapper.getById"))

    })
    Asn getByAsnNumber(@Param("asnNumber") String asnNumber);
}
