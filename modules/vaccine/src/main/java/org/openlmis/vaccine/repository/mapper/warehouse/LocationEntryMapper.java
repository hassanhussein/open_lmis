package org.openlmis.vaccine.repository.mapper.warehouse;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Update;
import org.openlmis.vaccine.domain.wms.LocationEntry;
import org.springframework.stereotype.Repository;

@Repository
public interface LocationEntryMapper {

    @Insert(" INSERT INTO public.lot_location_entries(\n" +
            "             lotOnHandId, locationId, type, quantity, createdBy, createdDate, \n" +
            "            modifiedBy, modifiedDate)\n" +
            "    VALUES ( #{lotOnHandId}, #{locationId}, #{type}, #{quantity}, #{createdBy}, NOW(), \n" +
            "            #{modifiedBy}, NOW());\n ")
    @Options(useGeneratedKeys = true)
    int insert(LocationEntry entry);

    @Update(" UPDATE public.lot_location_entries SET lotOnHandId=#{lotOnHandId},locationId=#{locationId},type=#{type},quantity=#{quantity},modifiedDate=NOW(),modifiedBy=#{modifiedBy} WHERE id=#{id}    ")
    void update(LocationEntry entry);

    @Insert("INSERT INTO location_stock_card_entry_key_values (stockcardentryid" +
            ", keycolumn" +
            ", valuecolumn" +
            ", createdBy" +
            ", createdDate" +
            ", modifiedBy" +
            ", modifiedDate)" +
            " VALUES (#{entry.id}" +
            ", #{key}" +
            ", #{value}" +
            ", #{entry.createdBy}" +
            ", NOW()" +
            ", #{entry.modifiedBy}" +
            ", NOW())")
    int insertLocationEntryKeyValue(@Param("entry") LocationEntry entry, @Param("key")String key, @Param("value")String value);


}
