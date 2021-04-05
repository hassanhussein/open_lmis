package org.openlmis.analytics.dto;

import lombok.Data;
import org.openlmis.core.dto.GeographicZoneGeometry;

@Data
public class Features {
    public String type;
    public Properties properties;
    public String geometry;
}
