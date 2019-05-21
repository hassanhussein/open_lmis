package org.openlmis.restapi.domain;

import lombok.Data;



@Data
public class SDIssue {

    private String issueType;
    private String summary;
    private String description;
    private String raiseOnBehalfOf;
}
