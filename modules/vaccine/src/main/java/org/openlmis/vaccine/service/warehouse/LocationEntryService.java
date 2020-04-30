package org.openlmis.vaccine.service.warehouse;

import org.openlmis.vaccine.domain.wms.LocationEntry;
import org.openlmis.vaccine.repository.warehouse.locationEntryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class LocationEntryService {

    @Autowired
    private locationEntryRepository repository;

    public void saveLocationEntry(LocationEntry entry) {
        repository.insert(entry);
    }
}
