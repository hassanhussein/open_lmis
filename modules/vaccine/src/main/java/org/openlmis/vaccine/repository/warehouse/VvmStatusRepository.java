package org.openlmis.vaccine.repository.warehouse;

import org.openlmis.vaccine.repository.mapper.warehouse.inspection.VvmStatusMapper;
import org.openlmis.vaccine.service.warehouse.VvmStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class VvmStatusRepository {

    @Autowired
    private VvmStatusMapper mapper;

    public VvmStatus getVvmStatus(Long vvmStatusId) {
        return mapper.getByVvmId(vvmStatusId);
    }

    public List<VvmStatus> getAll() {
        return mapper.getAllVVM();
    }
}
