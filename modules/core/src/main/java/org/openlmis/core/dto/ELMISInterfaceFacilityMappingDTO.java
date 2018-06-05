package org.openlmis.core.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.openlmis.core.domain.BaseModel;
import org.openlmis.upload.Importable;
import org.openlmis.upload.annotation.ImportField;

import static com.fasterxml.jackson.databind.annotation.JsonSerialize.Inclusion.NON_EMPTY;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@JsonSerialize(include = NON_EMPTY)
@JsonIgnoreProperties(ignoreUnknown = true)
public class ELMISInterfaceFacilityMappingDTO extends BaseModel implements Importable {

    @ImportField(mandatory = true, name = "Interface Id")
    private Long interfaceId;
    @ImportField(mandatory = true, name = "Data Set Name")
    private String dataSetname;
    @ImportField(mandatory = true, name = "Data Set Id")
    private String dataSetId;
    @ImportField(mandatory = true, name = "Vims Code")
    private String elmisCode;

}
