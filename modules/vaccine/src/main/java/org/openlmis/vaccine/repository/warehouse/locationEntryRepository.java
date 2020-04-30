package org.openlmis.vaccine.repository.warehouse;

import org.openlmis.stockmanagement.domain.StockCardEntry;
import org.openlmis.stockmanagement.domain.StockCardEntryKV;
import org.openlmis.vaccine.domain.wms.LocationEntry;
import org.openlmis.vaccine.repository.mapper.warehouse.LocationEntryMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class locationEntryRepository {

    @Autowired
    private LocationEntryMapper mapper;

   /* public Integer insert(LocationEntry entry) {
        return mapper.insert(entry);
    }*/

    public void insert(LocationEntry entry) {
        mapper.insert(entry);
        for (StockCardEntryKV item : entry.getKeyValues()) {
            mapper.insertLocationEntryKeyValue(entry, item.getKeyColumn(), item.getValueColumn());
        }
    }
}
