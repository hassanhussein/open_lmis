package org.openlmis.lookupapi.model;

import lombok.Data;

@Data
public class FacilityMsdCodeDTO {

    private String msdCode;

    private String facIdNumber;

    private boolean activatedByMsd;

    private String activatedDate;


}
