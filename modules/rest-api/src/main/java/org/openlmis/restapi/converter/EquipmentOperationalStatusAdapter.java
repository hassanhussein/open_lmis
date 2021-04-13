/*
 * This program is part of the OpenLMIS logistics management information system platform software.
 * Copyright © 2013 VillageReach
 *
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU Affero General Public License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version.
 *  
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU Affero General Public License for more details.
 * You should have received a copy of the GNU Affero General Public License along with this program.  If not, see http://www.gnu.org/licenses.  For additional information contact info@OpenLMIS.org. 
 */
package org.openlmis.restapi.converter;

import org.openlmis.equipment.domain.EquipmentInventoryStatus;
import org.openlmis.equipment.service.EquipmentOperationalStatusService;
import org.openlmis.restapi.domain.EquipmentStatusReport;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

@Component
public class EquipmentOperationalStatusAdapter implements
        ReporableEntityConverter<EquipmentInventoryStatus, EquipmentStatusReport> {
    public static final String FUNCTIONAL = "FUNCTIONAL";
    @Autowired
    private EquipmentOperationalStatusService operationalStatusService;

    @Override
    public EquipmentInventoryStatus convert(EquipmentStatusReport reportableModel) {
        EquipmentInventoryStatus equipmentOperationalStatus = this.getStatusFromEquipmentLineItem(reportableModel);
        return equipmentOperationalStatus;
    }

    private EquipmentInventoryStatus getStatusFromEquipmentLineItem(EquipmentStatusReport reportableModel) {
        EquipmentInventoryStatus status = new EquipmentInventoryStatus();
        status.setId(reportableModel.getOperationalStatusId());
        status.setInventoryId(reportableModel.getInventoryId());

        if (reportableModel.getOperationalStatusId() == null)
            status.setStatusId(operationalStatusService.getByCode(FUNCTIONAL).getId());
        else status.setStatusId(reportableModel.getOperationalStatusId());

        return status;
    }
}
