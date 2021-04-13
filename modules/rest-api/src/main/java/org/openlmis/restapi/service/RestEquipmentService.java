
/*
 * This program is part of the OpenLMIS logistics management information system platform software.
 * Copyright © 2013 VillageReach
 *
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU Affero General Public License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version.
 *  
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU Affero General Public License for more details.
 * You should have received a copy of the GNU Affero General Public License along with this program.  If not, see http://www.gnu.org/licenses.  For additional information contact info@OpenLMIS.org. 
 */
package org.openlmis.restapi.service;

import org.openlmis.equipment.domain.EquipmentInventory;
import org.openlmis.equipment.domain.EquipmentInventoryStatus;
import org.openlmis.equipment.service.EquipmentInventoryService;
import org.openlmis.restapi.converter.ModelConversionProcessor;
import org.openlmis.restapi.domain.FacilityEquipmentStatusReport;
import org.openlmis.rnr.domain.EquipmentLineItem;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RestEquipmentService {
    @Autowired
    EquipmentInventoryService inventoryService;

    @Autowired
    private ModelConversionProcessor conversionProcessor;

    public EquipmentInventory addEquipmentInventory(EquipmentInventory inventory, Long userId) {
        inventoryService.uploadEquipmentInventory(inventory);
        return inventory;
    }

    public void addEquipmentStatus(FacilityEquipmentStatusReport report, Long aLong) {

        List<EquipmentInventoryStatus> equipmentInventoryStatusList = null;

        equipmentInventoryStatusList = (List<EquipmentInventoryStatus>)
                conversionProcessor.process(report.getReportList(),
                        "EquipmentInventoryStatus");
        inventoryService.addEquipmentStatus(equipmentInventoryStatusList);
    }

}
