package org.openlmis.core.domain;

import lombok.*;
import org.openlmis.upload.Importable;
import org.openlmis.upload.annotation.ImportField;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class Manufacturer extends BaseModel implements Importable {

    @ImportField(mandatory = true, name = "Code")
    private String code;
    @ImportField(name = "Name")
    private String name;
    @ImportField(name = "Website")
    private String webSite;
    @ImportField(name = "Contact Person")
    private String contactPerson;

    @ImportField(name = "Primary Phone")
    private String primaryPhone;
    @ImportField(name = "Email")
    private String email;

    @ImportField(name = "description")
    private String description;

    @ImportField(name = "specialization")
    private String specialization;

    @ImportField(name = "Geographic Coverage")
    private String geographicCoverage;

    @ImportField(name = "Registration Date")
    private String registrationDate;

}
