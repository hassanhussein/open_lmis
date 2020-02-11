package org.openlmis.vaccine.domain.wms;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.openlmis.core.domain.BaseModel;
import org.openlmis.core.domain.GeographicZone;

@EqualsAndHashCode(callSuper = false)
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Site extends BaseModel {

    private GeographicZone geographicZone;

    private Long geographicZoneId;

    private String name;

    private String code;

    private String region;

    private String longitude;

    private String latitude;

    private boolean active;

}
