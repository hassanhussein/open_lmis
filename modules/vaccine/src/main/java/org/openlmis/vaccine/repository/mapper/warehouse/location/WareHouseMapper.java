package org.openlmis.vaccine.repository.mapper.warehouse.location;


import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Update;
import org.openlmis.vaccine.domain.wms.WareHouse;
import org.springframework.stereotype.Repository;

@Repository
public interface WareHouseMapper {

    @Insert("INSERT INTO public.wms_warehouses(\n" +
            "            siteId, code, name, productTypeId, active, createdBy, createdDate, \n" +
            "            modifiedBy, modifiedDate)\n" +
            "    VALUES (#{site.id}, #{code}, #{name}, #{productTypeId}, #{active}, #{createdBy}, NOW(), \n" +
            "            #{modifiedBy}, now());\n ")
    @Options(useGeneratedKeys = true)
    Integer insert(WareHouse house);

    @Update("UPDATE public.wms_warehouses\n" +
            "   SET  siteId=#{site.id}, code=#{code}, name=#{name}, productTypeId=#{productTypeId}, active=#{active},\n" +
            "        modifiedBy=#{modifiedBy}, modifiedDate=NOW()\n" +
            " WHERE id=#{id}")
    void update(WareHouse house);
}
