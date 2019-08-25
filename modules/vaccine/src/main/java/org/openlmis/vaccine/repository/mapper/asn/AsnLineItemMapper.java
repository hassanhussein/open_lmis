package org.openlmis.vaccine.repository.mapper.asn;

import org.apache.ibatis.annotations.*;
import org.openlmis.vaccine.domain.asn.AsnLineItem;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AsnLineItemMapper {

    @Insert(" INSERT INTO asn_details (asnid, productid, expirydate, manufacturingdate, quantityexpected, lotflag, " +
            "createdBy, createdDate,modifiedBy,modifiedDate) " +
            " VALUES(#{asn.id}, #{productid}, #{expirydate}, #{manufacturingdate}, #{quantityexpected}, #{lotflag}, #{createdBy}, NOW(), #{modifiedBy}, NOW()) ")
    @Options(useGeneratedKeys = true)
    Integer insert(AsnLineItem asnLineItem);

    @Update(" update asn_details set  asnid = #{asn.id}, productid = #{productid}, expirydate = #{expirydate}, " +
            " manufacturingdate = #{manufacturingdate}, quantityexpected = #{quantityexpected}, lotflag = #{lotflag}, "+
            " modifiedDate = now(), " +
            " modifiedBy = #{modifiedBy}  where id = #{id} ")
    void update(AsnLineItem asnLineItem);

    @Select("select * from asn_details where id = #{id}")
    AsnLineItem  getById(@Param("id") Long id);

    @Select(" select * from asn_details")
    List<AsnLineItem> getAll();
}
