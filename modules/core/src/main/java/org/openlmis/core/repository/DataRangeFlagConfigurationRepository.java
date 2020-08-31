package org.openlmis.core.repository;


import lombok.NoArgsConstructor;
import org.openlmis.core.domain.DataRangeFlagConfiguration;
import org.openlmis.core.repository.mapper.DataRangeFlagConfigurationMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@NoArgsConstructor
public class DataRangeFlagConfigurationRepository {
    @Autowired
    private DataRangeFlagConfigurationMapper mapper;

    public void addDataRangeFlagConfigurationRepository(DataRangeFlagConfiguration flagConfiguration) {
        if (flagConfiguration.getId() != null && flagConfiguration.getId() != 0) {
            mapper.update(flagConfiguration);
        } else {
            mapper.insert(flagConfiguration);
        }
    }

    public void updateDataRangeFlagConfigurationRepository(DataRangeFlagConfiguration flagConfiguration) {
        mapper.update(flagConfiguration);

    }

    public void deleteDataRangeFlagConfigurationRepository(DataRangeFlagConfiguration flagConfiguration) {
        mapper.delete(flagConfiguration);

    }

    public DataRangeFlagConfiguration getById(Long id) {
        return mapper.getById(id);

    }

    public List<DataRangeFlagConfiguration> loadAll() {
        return mapper.getAll();

    }
}
