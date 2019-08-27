package org.openlmis.vaccine.repository.mapper.warehouse.asn;

import org.apache.ibatis.annotations.*;
import org.openlmis.vaccine.domain.wms.AsnLot;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AsnLotMapper {

    @Insert(" INSERT INTO asn_lots (asndetailid, lotnumber, serialnumber, expirydate, manufacturingdate, quantity, " +
            "createdBy, createdDate,modifiedBy,modifiedDate) " +
            " VALUES(#{asnLineItem.id}, #{lotnumber}, #{serialnumber}, #{expirydate}, #{manufacturingdate}, #{quantity}, #{createdBy}, NOW(),#{modifiedBy}, NOW()) ")
    @Options(useGeneratedKeys = true)
    Integer insert(AsnLot asnLot);

    @Update(" update asn_lots set  asndetailid = #{asnLineItem.id}, lotnumber = #{lotnumber}, serialnumber = #{serialnumber}, expirydate = #{expirydate}, " +
            " manufacturingdate = #{manufacturingdate}, quantity = #{quantity}, "+
            " modifiedDate = now(), " +
            " modifiedBy = #{modifiedBy}  where id = #{id} ")
    void update(AsnLot asnLot);
    @Select("select * from asn_lots where id = #{id}")
    AsnLot  getById(@Param("id") Long id);

    @Select(" select * from asn_lots")
    List<AsnLot> getAll();

    @Select("select * from asn_lots where asndetailid = #{id}")
    List<AsnLot>  getByAsnDetail(@Param("id") Long id);
}
