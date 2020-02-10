package org.openlmis.lookupapi.model;

import lombok.Data;

@Data
public class ResponseMessage {

    private String message;

    private String operatingStatus;

    private String facilityCode;

    private String facilityName;

}
