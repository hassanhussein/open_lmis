package org.openlmis.lookupapi.model;

import lombok.Data;

@Data
public class ResponseMessage {

    private String message;

    private String operatingStatus;

    private String facilityCode;

    private String facilityName;

  /*  @Override
    public String toString() {

        return new StringBuilder()
                .append("facilityCode:"+facilityCode)
                .append("facilityName:"+facilityName)
                .append("operatingStatus:"+operatingStatus)
                .append("message:"+"Facility Received Successful")
                .toString();
    }*/

}
