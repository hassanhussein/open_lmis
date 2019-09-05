package org.openlmis.vaccine.repository.mapper.warehouse.asn;

import org.apache.ibatis.annotations.*;
import org.apache.ibatis.session.RowBounds;
import org.openlmis.vaccine.domain.wms.Asn;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public interface AsnMapper {
    @Insert(" INSERT INTO asns (ponumber,podate,supplierid,asnnumber,asndate,blawbnumber,flightvesselnumber,portofarrival,expectedarrivaldate, " +
            "clearingagent,status,note,createdBy, createdDate,modifiedBy,modifiedDate)  VALUES(#{ponumber}, #{podate}, #{supplierid}, #{asnnumber}, #{asndate}, #{blawbnumber}, " +
            "#{flightvesselnumber}, #{portofarrival}, #{expectedarrivaldate}, #{clearingagent},#{status},#{note}, #{createdBy}, NOW(),#{modifiedBy}, NOW()) ")
    @Options(useGeneratedKeys = true)
    Long insert(Asn asn);

    @Update(" update asns set  ponumber = #{ponumber}, " +
            " podate = #{podate}, supplierid = #{supplierid}, asnnumber =#{asnnumber}, asndate = #{asndate}," +
            " blawbnumber = #{blawbnumber}, flightvesselnumber = #{flightvesselnumber}, portofarrival =#{portofarrival}, expectedarrivaldate = #{expectedarrivaldate}," +
            " clearingagent = #{clearingagent}, status = #{status}, note =#{note}, " +
            " modifiedDate = now(), modifiedBy = #{modifiedBy}  where id = #{id} ")
    void update(Asn asn);

    @Select("select * from asns where id = #{id}")
    @Results(value = {
            @Result(column = "id", property = "id"),
            @Result(property = "asnLineItems", column = "id", javaType = List.class,
                    many = @Many(select = "org.openlmis.vaccine.repository.mapper.warehouse.asn.AsnLineItemMapper.getByAsnId")),
            @Result(property = "purchaseDocuments", column = "id", javaType = List.class,
                    many = @Many(select = "org.openlmis.vaccine.repository.mapper.warehouse.asn.PurchaseDocumentMapper.getByAsnId"))
    })
    Asn getById(@Param("id") Long id);

    @Select(" select * from asns")
    @Results(value = {
            @Result(column = "id", property = "id"),
            @Result(property = "asnLineItems", column = "id", javaType = List.class,
                    many = @Many(select = "org.openlmis.vaccine.repository.mapper.warehouse.asn.AsnLineItemMapper.getByAsnId")),
            @Result(property = "purchaseDocuments", column = "id", javaType = List.class,
                    many = @Many(select = "org.openlmis.vaccine.repository.mapper.warehouse.asn.PurchaseDocumentMapper.getByAsnId"))
    })
    List<Asn> getAll();

    @Select("SELECT COUNT(*) FROM asns A " +
            "WHERE (LOWER(A.asnnumber) LIKE '%' || LOWER(#{searchParam}) || '%')")
    Integer getTotalSearchResultCountByAsnumber(String searchParam);
    @Select("SELECT COUNT(*) FROM asns A " +
            " WHERE (LOWER(A.ponumber) LIKE '%' || LOWER(#{searchParam}) || '%')")
    Integer getTotalSearchResultCountByPonumber(String searchParam);

    @Select("SELECT COUNT(*) FROM asns A INNER JOIN manufacturers M ON M.id = A.supplierid AND " +
            "(LOWER(M.name) LIKE '%' || LOWER(#{searchParam}) || '%')")
    Integer getTotalSearchResultCountBySupplier(String searchParam);
    @Select("SELECT COUNT(*) FROM asns ")
    Integer getTotalSearchResultCountAll();

    @SelectProvider(type = SelectAsn.class, method = "getAsnBySearchParam")
    @Results(value = {
            @Result(property = "asnLineItems", column = "id", javaType = List.class,
                    many = @Many(select = "org.openlmis.vaccine.repository.mapper.warehouse.asn.AsnLineItemMapper.getByAsnId")),
            @Result(property = "purchaseDocuments", column = "id", javaType = List.class,
                    many = @Many(select = "org.openlmis.vaccine.repository.mapper.warehouse.asn.PurchaseDocumentMapper.getByAsnId"))
    })
    List<Asn> search(@Param(value = "searchParam") String searchParam, @Param(value = "column") String column,
                     RowBounds rowBounds);
    class SelectAsn {
        @SuppressWarnings(value = "unused")
        public static String getAsnCountBy(Map<String, Object> params) {
            StringBuilder sql = new StringBuilder();
            sql.append("SELECT COUNT(*) FROM asns L WHERE ");
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
                sql.append("(LOWER(A.asnnumber) LIKE '%' || LOWER(#{searchParam}) || '%') ");

            }
            else if(column.equalsIgnoreCase("ponumber")) {
                sql.append(" (LOWER(A.ponumber) LIKE '%' || LOWER(#{searchParam}) || '%') ");
            }
            else if(column.equalsIgnoreCase("supplier")) {
                sql.append(" (LOWER(M.name) LIKE '%' || LOWER(#{searchParam}) || '%') ");
            }
            sql.append("ORDER BY LOWER(A.ponumber)");
            return sql.toString();
        }

        private static StringBuilder createQuery(StringBuilder sql, Map<String, Object> params) {
            String searchParam = (String) params.get("searchParam");
            String column = (String) params.get("column");
            if(column.equalsIgnoreCase("asnumber")) {
                sql.append("(LOWER(A.asnnumber) LIKE LOWER('%" + searchParam + "%') ");
            }
            else if(column.equalsIgnoreCase("ponumber")) {
                sql.append("LOWER(A.ponumber) LIKE LOWER('%" + searchParam + "%'))");
            }
            else if(column.equalsIgnoreCase("supplier")) {
                sql.append("LOWER(M.name) LIKE LOWER('%" + searchParam + "%'))");
            }
            return sql;
        }
    }
}
