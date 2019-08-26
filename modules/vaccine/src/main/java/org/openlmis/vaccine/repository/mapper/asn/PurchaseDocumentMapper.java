package org.openlmis.vaccine.repository.mapper.asn;

import org.apache.ibatis.annotations.*;
import org.openlmis.vaccine.domain.wms.PurchaseDocument;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PurchaseDocumentMapper {

    @Insert("insert into purchase_documents(asnid, documenttype, filelocation, createdDate,createdBy, modifiedBy, modifiedDate) values(" +
            "#{wms.id},#{documentType.id}, #{filelocation}, COALESCE(#{createdDate}, NOW()), #{createdBy}, #{modifiedBy}," +
            "COALESCE(#{modifiedDate}, CURRENT_TIMESTAMP))")
    @Options(useGeneratedKeys = true)
    Integer insert(PurchaseDocument purchaseDocument);

    @Update("update  purchase_documents set documenttype = #{documenttype.id}, filelocation = #{filelocation}, " +
            " modifiedBy = #{modifiedBy}, modifiedDate = (COALESCE(#{modifiedDate}, NOW())) WHERE id = #{id}")
    void update(PurchaseDocument purchaseDocument);

    @Select("SELECT * FROM purchase_documents")
    @Results(value = {
            @Result(property = "documentType", column = "documenttype", javaType = Integer.class,
                    one = @One(select = "org.openlmis.vaccine.repository.mapper.wms.DocumentTypeMappper.getById")),
            @Result(property = "wms", column = "asnid", javaType = Integer.class,
                    one = @One(select = "org.openlmis.vaccine.repository.mapper.wms.asnMapper.getById"))
    })
    List<PurchaseDocument> getAllPurchaseDocuments();

    @Select("SELECT * FROM purchase_documents where id = #{id}")
    @Results(value = {
            @Result(property = "documentType", column = "documenttype", javaType = List.class,
                    one = @One(select = "org.openlmis.vaccine.repository.mapper.wms.DocumentTypeMappper.getById"))
    })
    PurchaseDocument getById(@Param("id") Long id);

}
