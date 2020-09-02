package org.openlmis.core.dto;

import lombok.Data;

@Data
public class ExpectedArrivalDTO {

    private String poNumber;

    private String firstName;

    private String expectedArrivalDate;
    private String clearingAgent;
    private String supplierName;

    private String portOfArrival;

    private String poDate;

}
