package org.openlmis.core.service;


import lombok.NoArgsConstructor;
import org.openlmis.core.domain.DataRangeFlagConfiguration;
import org.openlmis.core.repository.DataRangeFlagConfigurationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@NoArgsConstructor
public class DataRangeFlagConfigurationService {
    @Autowired
    private DataRangeFlagConfigurationRepository repository;

    public void addDataRangeFlagConfigurationRepository(DataRangeFlagConfiguration flagConfiguration) {
        repository.addDataRangeFlagConfigurationRepository(flagConfiguration);

    }

    public void updateDataRangeFlagConfigurationRepository(DataRangeFlagConfiguration flagConfiguration) {
        repository.updateDataRangeFlagConfigurationRepository(flagConfiguration);
    }

    public void deleteDataRangeFlagConfigurationRepository(DataRangeFlagConfiguration flagConfiguration) {
        repository.deleteDataRangeFlagConfigurationRepository(flagConfiguration);

    }

    public DataRangeFlagConfiguration getById(Long id) {
        return  repository.getById(id);

    }

    public List<DataRangeFlagConfiguration> loadAll() {
        return repository.loadAll();

    }
}
