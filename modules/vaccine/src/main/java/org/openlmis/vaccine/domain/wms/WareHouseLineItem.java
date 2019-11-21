package org.openlmis.vaccine.domain.wms;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.openlmis.core.domain.BaseModel;

import static com.fasterxml.jackson.databind.annotation.JsonSerialize.Inclusion.NON_EMPTY;

@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
@JsonSerialize(include = NON_EMPTY)
@JsonIgnoreProperties(ignoreUnknown = true)
public class WareHouseLineItem extends BaseModel {

    private WareHouse wareHouse;

    private Long warehouseId;

    private Zone zone;

    private Long zoneId;

    private String aisleCode;

    private String binLocationFrom;

    private String binLocationTo;

    private String beamLevelFrom;

    private String beamLevelTo;

}
