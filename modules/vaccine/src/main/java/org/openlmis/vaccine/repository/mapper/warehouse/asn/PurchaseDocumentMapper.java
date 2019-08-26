package org.openlmis.vaccine.repository.mapper.warehouse.asn;

import org.apache.ibatis.annotations.*;
import org.openlmis.vaccine.domain.wms.PurchaseDocument;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PurchaseDocumentMapper {

    @Insert("insert into purchase_documents(asnid, documenttype, filelocation, createdDate,createdBy, modifiedBy, modifiedDate) values(" +
            "#{warehouse.id},#{documentType.id}, #{filelocation}, COALESCE(#{createdDate}, NOW()), #{createdBy}, #{modifiedBy}," +
            "COALESCE(#{modifiedDate}, CURRENT_TIMESTAMP))")
    @Options(useGeneratedKeys = true)
    Integer insert(PurchaseDocument purchaseDocument);

    @Update("update  purchase_documents set documenttype = #{documenttype.id}, filelocation = #{filelocation}, " +
            " modifiedBy = #{modifiedBy}, modifiedDate = (COALESCE(#{modifiedDate}, NOW())) WHERE id = #{id}")
    void update(PurchaseDocument purchaseDocument);

    @Select("SELECT * FROM purchase_documents")
    @Results(value = {
            @Result(property = "documentType", column = "documenttype", javaType = Integer.class,
                    one = @One(select = "org.openlmis.vaccine.repository.mapper.warehouse.DocumentTypeMappper.getById")),
            @Result(property = "warehouse", column = "asnid", javaType = Integer.class,
                    one = @One(select = "org.openlmis.vaccine.repository.mapper.warehouse.asnMapper.getById"))
    })
    List<PurchaseDocument> getAllPurchaseDocuments();

    @Select("SELECT * FROM purchase_documents where id = #{id}")
    @Results(value = {
            @Result(property = "documentType", column = "documenttype", javaType = List.class,
                    one = @One(select = "org.openlmis.vaccine.repository.mapper.warehouse.DocumentTypeMappper.getById"))
    })
    PurchaseDocument getById(@Param("id") Long id);

}
