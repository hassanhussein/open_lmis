package org.openlmis.vaccine.repository.mapper.asn;

import org.apache.ibatis.annotations.*;
import org.openlmis.vaccine.domain.asn.Asn;
import org.springframework.stereotype.Repository;

import java.util.List;

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
            @Result(property = "asnLineItems", column = "asnid", javaType = List.class,
                    one = @One(select = "org.openlmis.vaccine.repository.mapper.asn.AsnLineItemMappper.getById")),
            @Result(property = "purchaseDocuments", column = "asnid", javaType = List.class,
                    one = @One(select = "org.openlmis.vaccine.repository.mapper.asn.PurchaseDocumentMapper.getById"))
    })
    Asn getById(@Param("id") Long id);

    @Select(" select * from asns")
    List<Asn> getAll();
}
