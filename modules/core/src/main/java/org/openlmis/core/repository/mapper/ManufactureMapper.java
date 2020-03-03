package org.openlmis.core.repository.mapper;

import org.apache.ibatis.annotations.*;
import org.openlmis.core.domain.Manufacturer;
import org.springframework.stereotype.Repository;

@Repository
public interface ManufactureMapper {

    @Insert("INSERT INTO public.manufacturers(\n" +
            "             name, webSite, contactPerson, primaryPhone, email, description, \n" +
            "            specialization, geographicCoverage, registrationDate, createdBy, \n" +
            "            createdDate, modifiedBy, modifiedDate, code)\n" +
            "    VALUES ( #{name}, #{webSite}, #{contactPerson}, #{primaryPhone}, #{email}, #{description}, \n" +
            "            #{specialization}, #{geographicCoverage}, #{registrationDate}::date, #{createdBy}, \n" +
            "            NOW(), #{modifiedBy}, NOW(), #{code});")
    @Options(useGeneratedKeys = true)
    Integer insert(Manufacturer manufacturer);

    @Update("UPDATE public.manufacturers\n" +
            "   SET name=#{name}, webSite=#{webSite}, contactPerson=#{contactPerson}, primaryPhone=#{primaryPhone}, email=#{email}, \n" +
            "       description=#{description}, specialization=#{specialization}, geographicCoverage=#{geographicCoverage}, \n" +
            "       registrationDate=#{registrationDate}::date,modifiedBy=#{modifiedBy}, modifiedDate=NOW(), code=#{code}\n" +
            " WHERE id = #{id};\n ")
    void update(Manufacturer manufacturer);

    @Select("SELECT * FROM  manufacturers where id = #{id}")
    Manufacturer getById(@Param("id") Long id);

    @Select("SELECT * FROM  manufacturers where lower(code) = lower(#{code})")
    Manufacturer getByCode(@Param("code") String code);
}
