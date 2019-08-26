package org.openlmis.vaccine.domain.asn;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.openlmis.core.domain.BaseModel;

@Data
@EqualsAndHashCode(callSuper = false)
@JsonIgnoreProperties(ignoreUnknown = true)
public class Port extends BaseModel {

    private  String code;

    private String name;

    private String description;

    private boolean active;
}
