package org.openlmis.vaccine.repository.mapper.asn;

import org.apache.ibatis.annotations.*;
import org.openlmis.vaccine.domain.wms.DocumentType;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DocumentTypeMapper {
    @Insert(" INSERT INTO document_types (name, description,createdBy, createdDate,modifiedBy,modifiedDate) " +
            " VALUES(#{name}, #{description}, #{createdBy}, NOW(),#{modifiedBy}, NOW()) ")
    @Options(useGeneratedKeys = true)
    Integer insert(DocumentType documentType);

    @Update(" update document_types set  name = #{name}, " +
            " description = #{description}, modifiedDate = now(), " +
            " modifiedBy = #{modifiedBy}  where id = #{id} ")
    void update(DocumentType documentType);
    @Select("select * from document_types where id = #{id}")
    DocumentType  getById(@Param("id") Long id);

    @Select(" select * from document_types")
    List<DocumentType> getAll();
}
