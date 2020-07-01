package org.openlmis.vaccine.repository.mapper.warehouse.inspection;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.openlmis.vaccine.service.warehouse.VvmStatus;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface VvmStatusMapper {

    @Select(" SELECT * FROM wms_vvm_statuses ")
    List<VvmStatus>getAllVVM();

    @Select(" SELECT * FROM wms_vvm_statuses WHERE vvmId = #{vvmId}")
    VvmStatus getByVvmId(@Param("vvmId") Long vvmId);
}
