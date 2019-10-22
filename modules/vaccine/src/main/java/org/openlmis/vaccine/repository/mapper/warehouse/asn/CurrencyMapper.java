package org.openlmis.vaccine.repository.mapper.warehouse.asn;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.openlmis.vaccine.dto.CurrencyDTO;
import org.springframework.stereotype.Repository;

@Repository
public interface CurrencyMapper {

    @Select("select * from currencies where id = #{id}")
    CurrencyDTO getById(@Param("id") Long id);

}
