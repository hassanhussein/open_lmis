package org.openlmis.vaccine.repository.mapper.warehouse.asn;

import org.apache.ibatis.annotations.*;
import org.openlmis.vaccine.domain.wms.Document;
import org.openlmis.vaccine.domain.wms.PurchaseDocument;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PurchaseDocumentMapper {

    @Insert("insert into purchase_documents(asnId,receiveId, documentType, fileLocation, createdDate,createdBy, modifiedBy, modifiedDate,asnNumber,deleted,comment) values(" +
            "#{asn.id},#{receive.id},#{documentType.id}, #{fileLocation}, COALESCE(#{createdDate}, NOW()), #{createdBy}, #{modifiedBy}," +
            "COALESCE(#{modifiedDate}, CURRENT_TIMESTAMP), #{asnNumber} ,#{deleted},#{comment})")
    @Options(useGeneratedKeys = true)
    Integer insert(PurchaseDocument purchaseDocument);

    @Update("update  purchase_documents set documentType = #{documentType.id},asnNumber = #{asnNumber}, fileLocation = #{fileLocation}, " +
            " modifiedBy = #{modifiedBy},comment=#{comment},deleted=#{deleted} modifiedDate = (COALESCE(#{modifiedDate}, NOW())) WHERE id = #{id}")
    void update(PurchaseDocument purchaseDocument);

    @Select("SELECT * FROM purchase_documents")
    @Results(value = {
            @Result(property = "documentType", column = "documentType", javaType = Integer.class,

                    one = @One(select = "org.openlmis.vaccine.repository.mapper.warehouse.asn.DocumentTypeMappper.getById")),
            @Result(property = "asn", column = "asnid", javaType = Integer.class,
                    one = @One(select = "org.openlmis.vaccine.repository.mapper.warehouse.asn.AsnMapper.getById")),
            @Result(property = "receive", column = "receiveId", javaType = Integer.class,
                    one = @One(select = "org.openlmis.vaccine.repository.mapper.warehouse.receive.ReceiveMapper.getById"))
    })
    List<PurchaseDocument> getAllPurchaseDocuments();

    @Select("SELECT * FROM purchase_documents where id = #{id}")
    @Results(value = {
            @Result(property = "documentType", column = "documenttype", javaType = Integer.class,
                    one = @One(select = "org.openlmis.vaccine.repository.mapper.warehouse.asn.DocumentTypeMappper.getById"))
    })
    PurchaseDocument getById(@Param("id") Long id);

    @Select("SELECT p.*, concat(u.firstname,' ',u.lastname)  createdByName FROM purchase_documents p left join users u on (u.id=p.createdby)  where asnid = #{id}")
    @Results(value = {
            @Result(property = "documentType", column = "documenttype", javaType = Integer.class,
                    one = @One(select = "org.openlmis.vaccine.repository.mapper.warehouse.asn.DocumentTypeMapper.getById"))
    })
    List<PurchaseDocument> getByAsnId(@Param("id") Long id);

    @Select("SELECT * FROM purchase_documents where receiveId = #{id}")
    @Results(value = {
            @Result(property = "documentType", column = "documenttype", javaType = Integer.class,
                    one = @One(select = "org.openlmis.vaccine.repository.mapper.warehouse.asn.DocumentTypeMapper.getById"))
    })
    List<PurchaseDocument> getByReceiveId(@Param("id") Long id);


    @Insert("INSERT INTO public.documents(\n" +
            "            asnNumber, documentType, fileLocation,createdDate,createdBy)\n" +
            "    VALUES ( #{asnNumber}, #{documentType.id}, #{fileLocation},COALESCE(#{createdDate}, NOW()),#{createdBy});\n")
    @Options(useGeneratedKeys = true)
    Integer insertDocument(Document document);

   @Update("update documents set asnNumber=#{asnNumber}, documentType = #{documentType.id}, fileLocation= #{fileLocation} where id = #{id}")
   void updateDocument(Document document);

    @Update("update  documents set deleted=true,comment=#{comment},deletionLocation=#{deletionLocation},deletedBy=#{deletedBy} where id = #{id} ")
    void updateDeleteDocument(Document document);


    @Select("SELECT d.*, concat(u.firstname,' ',u.lastname)  createdByName FROM documents d left join users u on (u.id=d.createdby) where asnNumber = #{asnNumber}")
    @Results(value = {
            @Result(property = "documentType", column = "documentType", javaType = Integer.class,
                    one = @One(select = "org.openlmis.vaccine.repository.mapper.warehouse.asn.DocumentTypeMapper.getById"))
    })
    List<Document> getByASNCode(@Param("asnNumber") String asnNumber);


    @Select(" SELECT * FROM documents where fileLocation= #{fileLocation} ")
    Document getByFileLocation(@Param("fileLocation") String fileLocation);

    @Delete("delete from documents where id = #{id} ")
    void deleteById(@Param("id") Long id);

    @Select("select * from documents where id = #{id}")
    Document getDocumentById(@Param("id") Long id);

    @Select(" SELECT * FROM purchase_documents where fileLocation= #{fileLocation} ")
    PurchaseDocument getDocumentByFileLocation(String code);

    @Delete("delete from purchase_documents where asnNumber = #{asnNumber} ")
    void deleteByAsnNumber(@Param("asnNumber") String asnNumber);
}
